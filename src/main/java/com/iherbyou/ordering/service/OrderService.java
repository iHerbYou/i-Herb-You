package com.iherbyou.ordering.service;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.ordering.entity.OrderStatusHistory;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.OrderStatusHistoryRepository;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CodeService codeService;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final EntityManager em; // TODO: 주문 전용 조회용 리포지토리(Dao)로 교체

    // 상태 전이 가드: 현재 상태 -> 허용 대상 상태 목록
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.ofEntries(
            Map.entry(OrderStatus.PENDING, Set.of(OrderStatus.PAID, OrderStatus.FAILED, OrderStatus.CANCELED)),
            Map.entry(OrderStatus.PAID, Set.of(OrderStatus.PACKING, OrderStatus.REFUND_REQUESTED, OrderStatus.CANCELED)),
            Map.entry(OrderStatus.PACKING, Set.of(OrderStatus.SHIPPED)),
            Map.entry(OrderStatus.SHIPPED, Set.of(OrderStatus.COMPLETED, OrderStatus.REFUND_REQUESTED)),
            Map.entry(OrderStatus.COMPLETED, Set.of()),
            Map.entry(OrderStatus.CANCELED, Set.of()),
            Map.entry(OrderStatus.REFUND_REQUESTED, Set.of(OrderStatus.REFUNDED, OrderStatus.PARTIALLY_REFUNDED)),
            Map.entry(OrderStatus.REFUNDED, Set.of()),
            Map.entry(OrderStatus.PARTIALLY_REFUNDED, Set.of(OrderStatus.PARTIALLY_REFUNDED, OrderStatus.REFUNDED)),
            Map.entry(OrderStatus.FAILED, Set.of())
    );

    private static final int MAX_SOURCE_LENGTH = 80;
    private static final int MAX_ACTOR_LENGTH = 80;

    // 주문 생성 (장바구니 -> 주문 버튼)
    public Order createOrder(Long userId, OrderCreateDto dto) {
        // 1) 필수 로딩
        if (userId == null) {
            throw new IllegalArgumentException("userId is required for order creation");
        }

        User user = em.getReference(User.class, userId);
        Code statusCreated = requireCode(OrderStatus.PENDING);

        // 2) 할인값 정리
        int discount = (dto.getDiscount() == null) ? 0 : Math.max(0, dto.getDiscount());

        // 3) Order 뼈대 생성 (초기 금액 0)
        Order order = Order.builder()
                .user(user)
                .orderStatusCode(statusCreated)
                .customsInfo(dto.getCustomsInfo())
                .subtotal(0)
                .deliveryFee(0)     // 배송비는 아래 정책에서 계산
                .discount(discount) // 확정 할인액
                .totalPrice(0)
                .build();

        // 4) 아이템 추가 + 소계 계산
        int subtotal = 0;
        for (OrderCreateDto.Item it : dto.getItems()) {
            ProductVariant sku = em.getReference(ProductVariant.class, it.getProductVariantId());

            // TODO: 운영 환경에서는 클라 가격 대신 서버 가격(variant.getSalePrice())로 재검증 후 계산
            int unit    = it.getUnitPrice();
            int regular = (it.getRegularPrice() == null) ? unit : it.getRegularPrice();
            int lineSubtotal = unit * it.getQty();
            subtotal += lineSubtotal;

            OrderProduct op = OrderProduct.builder()
                    .productVariant(sku)
                    .qty(it.getQty())
                    .unitPriceAtOrder(unit)
                    .regularPriceAtOrder(regular)
                    .subtotal(lineSubtotal)
                    .build();

            // 양방향 연관관계 편의 메서드 (Order.addItem 이 있어야 함)
            order.addItem(op);
        }

        // 5) 배송비 정책 적용
        int effective = Math.max(0, subtotal - discount);
        int deliveryFee = (effective >= 50_000) ? 0 : 2_500;

        // 6) 금액 확정 및 합계 재계산
        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.recalcTotal(); // subtotal + deliveryFee - discount

        // 7) 저장 (orderProducts는 cascade=ALL로 함께 저장)
        return orderRepository.save(order);
    }

    // 마이페이지: 내 주문 목록 (최근순)
    @Transactional(readOnly = true)
    public Page<Order> listUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUser_IdOrderByOrderDateDesc(userId, pageable);
    }

    // 상세 조회 (상품까지 fetch)
    @Transactional(readOnly = true)
    public Order getOrderDetail(Long orderId, Long userId) {
        // TODO: JWT/세션 연동 완료 후 인증 컴포넌트 기반으로 소유자 검증 교체
        if (!orderRepository.existsByIdAndUser_Id(orderId, userId)) {
            throw new IllegalArgumentException("no permission");
        }
        return orderRepository.findWithDetailsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
    }

    /**
     * 주문 상태 전이 공통 메서드.
     * <p>
     * correlationId 를 제공하면 멱등하게 처리(이미 처리된 동일 콜백 무시)하고, 미제공 시에는 내부 UUID 를 생성해 전이 이력만 남깁니다.
     *
     * @param orderId        대상 주문 ID
     * @param targetStatus   전이하려는 상태
     * @param trigger        호출을 유발한 이벤트/서비스 (예: PAYMENT_PAID)
     * @param actor          변경 주체 (사용자 ID, 시스템 등)
     * @param correlationId  선택 멱등 키 (없으면 내부 생성)
     * @return 전이 후 주문 엔터티
     */
    public Order updateStatus(Long orderId, OrderStatus targetStatus, String trigger, String actor, String correlationId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId is required for status transition");
        }
        if (targetStatus == null) {
            throw new IllegalArgumentException("targetStatus is required");
        }

        ResolvedCorrelation resolvedCorrelation = resolveCorrelationId(correlationId);

        if (resolvedCorrelation.provided && orderStatusHistoryRepository.existsByOrder_IdAndToStatusAndCorrelationId(orderId, targetStatus, resolvedCorrelation.value)) {
            log.info("[OrderStatusChange][idempotent-existing-history] orderId={} nextStatus={} trigger={}", orderId, targetStatus, trigger);
            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        Code currentCode = order.getOrderStatusCode();
        OrderStatus currentStatus = currentCode != null ? OrderStatus.fromCodeValue(currentCode.getValue()) : null;

        if (currentStatus == targetStatus) {
            log.info("[OrderStatusChange][idempotent-current] orderId={} status={} trigger={}", orderId, targetStatus, trigger);
            return order;
        }

        ensureTransitionAllowed(currentStatus, targetStatus, orderId);

        Code nextCode = requireCode(targetStatus);
        order.setOrderStatusCode(nextCode);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .fromStatus(currentStatus)
                .toStatus(targetStatus)
                .source(sanitize(trigger, MAX_SOURCE_LENGTH))
                .actor(sanitize(actor, MAX_ACTOR_LENGTH))
                .correlationId(resolvedCorrelation.value)
                .build();

        try {
            orderStatusHistoryRepository.save(history);
        } catch (DataIntegrityViolationException e) {
            log.info("[OrderStatusChange][idempotent-concurrent] orderId={} nextStatus={} trigger={}", orderId, targetStatus, trigger);
            return order;
        }

        log.info(
                "[OrderStatusChange] orderId={} prevStatus={} nextStatus={} trigger={} actor={} correlation={}",
                orderId,
                currentStatus,
                targetStatus,
                Optional.ofNullable(trigger).orElse("UNKNOWN"),
                Optional.ofNullable(actor).orElse("system"),
                resolvedCorrelation.value
        );

        return order;
    }

    private void ensureTransitionAllowed(OrderStatus currentStatus, OrderStatus targetStatus, Long orderId) {
        if (currentStatus == null) {
            return;
        }
        Set<OrderStatus> allowedTargets = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowedTargets.contains(targetStatus)) {
            throw new IllegalStateException(
                    "invalid order status transition: orderId=" + orderId + " from=" + currentStatus + " to=" + targetStatus
            );
        }
    }

    private ResolvedCorrelation resolveCorrelationId(String correlationId) {
        if (!StringUtils.hasText(correlationId)) {
            String generated = UUID.randomUUID().toString();
            return new ResolvedCorrelation(generated, false);
        }
        String trimmed = correlationId.trim();
        String normalized = trimmed.length() > 100 ? trimmed.substring(0, 100) : trimmed;
        return new ResolvedCorrelation(normalized, true);
    }

    private String sanitize(String value, int maxLength) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.length() <= maxLength ? trimmed : trimmed.substring(0, maxLength);
    }

    private record ResolvedCorrelation(String value, boolean provided) {}

    private Code requireCode(int groupValue, int codeValue, String context) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("code not found: " + context);
        }
        return code;
    }

    private Code requireCode(OrderStatus orderStatus) {
        return requireCode(OrderStatus.GROUP_VALUE, orderStatus.getCodeValue(), "ORDER_STATUS:" + orderStatus);
    }


}

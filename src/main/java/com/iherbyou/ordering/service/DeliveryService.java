package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.DeliveryRegisterRequest;
import com.iherbyou.ordering.entity.Delivery;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.repository.DeliveryRepository;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.entity.UserAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final CodeService codeService;
    private final OrderService orderService;

    public Delivery registerTracking(Long userId, Long orderId, DeliveryRegisterRequest request) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required for delivery action");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        ensureOrderOwner(order, userId);
        ensureOrderAllowsTracking(order);

        String deliveryCompany = request.getDeliveryCompany();
        String trackingNumber = request.getTrackingNumber();

        LocalDateTime now = LocalDateTime.now();

        // 기존 배송 있으면 업데이트, 없으면 생성
        Delivery delivery = deliveryRepository.findByOrder_Id(orderId).orElse(null);

        if (delivery == null) {
            // 1) 상태코드: DELIVERY_STATUS.PREPARING (송장 등록과 동시에 배송 준비중으로 취급)
            Code ready = requireCode(20, 201, "DELIVERY_STATUS:PREPARING"); // 20=DELIVERY_STATUS, 201=PREPARING

            // 2) 필수값 세팅
            delivery = Delivery.builder()
                    .order(order)
                    .code(ready)
                    .deliveryCompany(deliveryCompany)
                    .trackingNumber(trackingNumber)
                    .delStartAt(now)
                    .build();

            // 3) 주소는 nullable=false면 반드시 세팅
            UserAddress defaultAddr = order.getUser().getAddresses().stream()
                    .filter(a -> a.isDefault())    // 기본 주소가 있으면
                    .findFirst()
                    .orElseGet(() -> order.getUser().getAddresses().isEmpty()
                            ? null
                            : order.getUser().getAddresses().get(0)); // 없으면 첫 번째

            if (defaultAddr == null) {
                // 주소가 필수(nullable=false)인데 못 구하면 예외로 막는 게 안전
                throw new IllegalStateException("no user address to attach for delivery");
            }
            delivery.setUserAddress(defaultAddr);
        } else {
            // 기존 건이면 필요한 필드만 갱신
            delivery.setDeliveryCompany(deliveryCompany);
            delivery.setTrackingNumber(trackingNumber);
            delivery.setDelStartAt(now);
        }

        order.setDelivery(delivery);

        Delivery saved = deliveryRepository.save(delivery);

        // 멱등 키: 동일한 송장 등록 요청이 들어와도 한 번만 상태 전환되도록 orderId + tracking 조합
        String correlationId = "DELIVERY_TRACKING:" + orderId + ":" +
                (trackingNumber != null && !trackingNumber.isBlank() ? trackingNumber : now.toString());
        orderService.updateStatus(orderId, OrderStatus.PACKING, "DELIVERY_TRACKING_REGISTERED", "user:" + userId, correlationId);
        log.info("[DeliveryTrackingRegistered] orderId={} deliveryId={} company={} tracking={} actor=user:{}",
                orderId, saved.getId(), deliveryCompany, trackingNumber, userId);

        return saved;
    }

    private void ensureOrderOwner(Order order, Long userId) {
        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied for order " + order.getId());
        }
    }

    private void ensureOrderAllowsTracking(Order order) {
        if (order.getOrderStatusCode() == null) {
            throw new IllegalStateException("order status is not set");
        }
        OrderStatus status = OrderStatus.fromCodeValue(order.getOrderStatusCode().getValue());
        if (status != OrderStatus.PAID && status != OrderStatus.PACKING) {
            throw new IllegalStateException("order status does not allow delivery registration: " + status);
        }
    }

    private Code requireCode(int groupValue, int codeValue, String context) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("code not found: " + context);
        }
        return code;
    }

    public Delivery changeStatus(Long orderId, Integer statusValue, String memo, LocalDateTime completeAt) {
        if (statusValue == null) {
            throw new IllegalArgumentException("status value is required");
        }

        Delivery delivery = deliveryRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new IllegalStateException("delivery not found for order: " + orderId));

        Code newStatus = requireCode(20, statusValue, "DELIVERY_STATUS:" + statusValue);

        delivery.setCode(newStatus);
        delivery.setDelMemo(memo);

        LocalDateTime appliedCompleteAt = completeAt;
        if (appliedCompleteAt == null && statusValue == 203) { // DELIVERED
            appliedCompleteAt = LocalDateTime.now();
        }

        if (statusValue == 201 || statusValue == 202) {
            // 진행 중 단계에서는 완료일을 비워 두어 UI 혼란을 줄임
            delivery.setDelCompleteAt(null);
        } else {
            delivery.setDelCompleteAt(appliedCompleteAt);
        }

        Delivery saved = deliveryRepository.save(delivery);

        // 주문 상태 전환: 배송 단계에 따라 결정
        if (statusValue == 201) { // PREPARING
            // 멱등 키: 배송 준비 단계 업데이트 중복 방지
            String correlationId = "DELIVERY_STATUS:" + orderId + ":201";
            orderService.updateStatus(orderId, OrderStatus.PACKING, "DELIVERY_STATUS_UPDATED", "system", correlationId);
        } else if (statusValue == 202) { // SHIPPING
            // 멱등 키: 배송중 전환 중복 방지
            String correlationId = "DELIVERY_STATUS:" + orderId + ":202";
            orderService.updateStatus(orderId, OrderStatus.SHIPPED, "DELIVERY_STATUS_UPDATED", "system", correlationId);
        } else if (statusValue == 203) { // DELIVERED
            // 멱등 키: 배송 완료 전환 중복 방지
            String correlationId = "DELIVERY_STATUS:" + orderId + ":203";
            orderService.updateStatus(orderId, OrderStatus.COMPLETED, "DELIVERY_STATUS_UPDATED", "system", correlationId);
        }

        log.info("[DeliveryStatusUpdated] orderId={} deliveryId={} statusValue={} actor=system", orderId, delivery.getId(), statusValue);
        return saved;
    }
}

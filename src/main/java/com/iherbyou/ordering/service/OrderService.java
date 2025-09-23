package com.iherbyou.ordering.service;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.ordering.common.CodeFinder;
import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CodeFinder codeFinder;
    private final EntityManager em; // 임시(getReference) — 추후 전용 Repo로 교체해도 됨

    // 주문 생성 (장바구니 -> 주문 버튼)
    public Order createOrder(OrderCreateDto dto) {
        // 1) 필수 로딩
        User user = em.getReference(User.class, dto.getUserId());
        Code statusCreated = codeFinder.get("ORDER_STATUS", "PENDING");

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

            // 운영에서는 클라 가격 대신 서버 가격(variant.getSalePrice())로 검증/계산 권장
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
        order.recalcTotal(); // subtotal + deliveryFee - discount -> totalPrice

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
        // 소유자 검사 (토큰 연동 전 임시)
        if (!orderRepository.existsByIdAndUser_Id(orderId, userId)) {
            throw new IllegalArgumentException("no permission");
        }
        return orderRepository.findWithDetailsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
    }

}

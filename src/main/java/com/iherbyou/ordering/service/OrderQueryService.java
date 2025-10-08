package com.iherbyou.ordering.service;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.dto.OrderItemDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    // 마이페이지: 내 주문 목록 (최근순)
    public Page<OrderSummaryDto> listUserOrders(Long userId, Pageable pageable) {
        Page<Order> page = orderRepository.findByUser_IdOrderByOrderDateDesc(userId, pageable);
        return page.map(this::toSummary);
    }

    // 주문 상세 (상품/배송 포함) - 소유자 검사
    public OrderDetailDto getOrderDetail(Long orderId, Long userId) {
        if (!orderRepository.existsByIdAndUser_Id(orderId, userId)) {
            throw new IllegalArgumentException("no permission");
        }
        Order o = orderRepository.findWithDetailsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
        return toDetail(o);
    }

    // ================== Mappers ==================

    private OrderSummaryDto toSummary(Order o) {
        return OrderSummaryDto.builder()
                .id(o.getId())
                .orderDate(o.getOrderDate())
                .subtotal(o.getSubtotal())
                .deliveryFee(o.getDeliveryFee())
                .discount(o.getDiscount())
                .totalPrice(o.getTotalPrice())
                .orderStatusKey(o.getOrderStatusCode().getValue())
                .build();
    }

    private OrderDetailDto toDetail(Order o) {
        List<OrderItemDto> items = (o.getOrderProducts() == null)
                ? Collections.emptyList()
                : o.getOrderProducts().stream().map(this::toItem).toList();

        return OrderDetailDto.builder()
                .id(o.getId())
                .orderStatusKey(o.getOrderStatusCode().getValue())
                .subtotal(o.getSubtotal())
                .deliveryFee(o.getDeliveryFee())
                .discount(o.getDiscount())
                .totalPrice(o.getTotalPrice())
                .customsInfo(o.getCustomsInfo())
                .orderDate(o.getOrderDate())
                // 배송 정보(있을 때만)
                .deliveryCompany(o.getDelivery() != null ? o.getDelivery().getDeliveryCompany() : null)
                .trackingNumber(o.getDelivery() != null ? o.getDelivery().getTrackingNumber() : null)
                .delStartAt(o.getDelivery() != null ? o.getDelivery().getDelStartAt() : null)
                .delCompleteAt(o.getDelivery() != null ? o.getDelivery().getDelCompleteAt() : null)
                .deliveryStatusKey(
                        (o.getDelivery() != null && o.getDelivery().getCode() != null)
                                ? o.getDelivery().getCode().getValue()
                                : null
                )
                .items(items)
                .build();
    }

    private OrderItemDto toItem(OrderProduct op) {
        ProductVariant pv = op.getProductVariant();
        String pName = (pv != null && pv.getProduct() != null) ? pv.getProduct().getName() : null;

        return OrderItemDto.builder()
                .orderProductId(op.getId())
                .productVariantId(pv != null ? pv.getId() : null)
                .productName(pName)
                .variantName(pv != null ? pv.getVariantName() : null)
                .qty(op.getQty())
                .unitPrice(op.getUnitPriceAtOrder())
                .subtotal(op.getSubtotal())
                .build();
    }

}

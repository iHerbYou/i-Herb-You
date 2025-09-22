package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.OrderProduct;
import com.iherbyou.ordering.dto.*;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;

    // 송장 등록
    @PostMapping("/{orderId}/delivery")
    public ResponseEntity<OrderDetailDto> register(@PathVariable Long orderId,
                                                   @RequestBody DeliveryRegisterRequest req) {

        deliveryService.registerTracking(orderId, req.getDeliveryCompany(), req.getTrackingNumber());

        // 등록 직후 상세 재조회 -> OrderDetailDto로 반환
        Order o = orderRepository.findWithDetailsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        return ResponseEntity.ok(toDetail(o));
    }

    // 간단 매퍼 (중복 최소화를 위해 컨트롤러 내부에 둠)
    private OrderDetailDto toDetail(Order o) {
        List<OrderItemDto> items = (o.getOrderProducts() == null)
                ? Collections.emptyList()
                : o.getOrderProducts().stream().map(this::toItem).toList();

        return OrderDetailDto.builder()
                .id(o.getId())
                .orderStatusKey(o.getOrderStatusCode().getCodeKey())
                .subtotal(o.getSubtotal())
                .deliveryFee(o.getDeliveryFee())
                .discount(o.getDiscount())
                .totalPrice(o.getTotalPrice())
                .customsInfo(o.getCustomsInfo())
                .orderDate(o.getOrderDate())
                .deliveryCompany(o.getDelivery() != null ? o.getDelivery().getDeliveryCompany() : null)
                .trackingNumber(o.getDelivery() != null ? o.getDelivery().getTrackingNumber() : null)
                .delStartAt(o.getDelivery() != null ? o.getDelivery().getDelStartAt() : null)
                .delCompleteAt(o.getDelivery() != null ? o.getDelivery().getDelCompleteAt() : null)
                .items(items)
                .build();
    }

    private OrderItemDto toItem(OrderProduct op) {
        var pv = op.getProductVariant();
        var pName = (pv != null && pv.getProduct() != null) ? pv.getProduct().getName() : null;

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
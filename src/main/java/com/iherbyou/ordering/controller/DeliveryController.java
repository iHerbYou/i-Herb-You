package com.iherbyou.ordering.controller;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.ordering.dto.*;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

// 배송 관련 API 컨트롤러 (송장 등록과 배송 상태 변경 담당)
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;

    // 사용자가 주문에 대한 송장을 등록할 때 호출되는 API (배송지 결정, 주문 상태 -> PACKING 단계로 전환)
    @PostMapping("/{orderId}/delivery")
    public ResponseEntity<OrderDetailDto> register(@AuthenticationPrincipal(expression = "id") Long userId,
                                                   @PathVariable Long orderId,
                                                   @Valid @RequestBody DeliveryRegisterRequest req) {

        deliveryService.registerTracking(userId, orderId, req);

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
                .orderStatusKey(o.getOrderStatusCode().getValue())
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

    // 관리자용 배송 상태 변경 API (물류 단계에 따라 주문 상태를 SHIPPED/COMPLETED 등으로 승격)
    @PatchMapping("/{orderId}/delivery/status")
    @PreAuthorize("hasRole('ADMIN_MASTER')")
    public ResponseEntity<DeliveryDto> updateStatus(@PathVariable Long orderId,
                                                    @RequestBody @Valid DeliveryStatusUpdateRequest req) {
        return ResponseEntity.ok(DeliveryDto.from(
                deliveryService.changeStatus(orderId, req.getStatusCodeValue(), req.getMemo(), req.getCompleteAt())
        ));
    }

}
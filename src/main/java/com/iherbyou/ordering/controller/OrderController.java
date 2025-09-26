package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 주문 생성 API 컨트롤러 (장바구니에서 주문하기 버튼을 눌렀을 때 주문 엔터티 생성)
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성 엔드포인트 (인증된 사용자의 장바구니 데이터를 기반으로 주문 생성, 요약 정보 반환)
    @PostMapping
    public ResponseEntity<OrderSummaryDto> create(@AuthenticationPrincipal(expression = "id") Long userId,
                                                  @Valid @RequestBody OrderCreateDto dto) {
        Order order = orderService.createOrder(userId, dto);
        OrderSummaryDto res = OrderSummaryDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .subtotal(order.getSubtotal())
                .deliveryFee(order.getDeliveryFee())
                .discount(order.getDiscount())
                .totalPrice(order.getTotalPrice())
                .orderStatusKey(order.getOrderStatusCode().getValue())
                .build();
        return ResponseEntity.ok(res);
    }

}

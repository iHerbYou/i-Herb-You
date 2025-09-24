package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderSummaryDto> create(@RequestBody OrderCreateDto dto) {
        Order order = orderService.createOrder(dto);
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

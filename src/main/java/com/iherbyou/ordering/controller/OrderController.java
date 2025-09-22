package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.service.OrderQueryService;
import com.iherbyou.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;         // 생성 담당
    private final OrderQueryService queryService;    // 조회 담당

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

    // 내 주문 목록 (최근순, 페이징)
    @GetMapping("/me/{userId}")
    public ResponseEntity<Page<OrderSummaryDto>> myOrders(@PathVariable Long userId,
                                                          @PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        Page<OrderSummaryDto> page = queryService.listUserOrders(userId, pageable);
        return ResponseEntity.ok(page);
    }

    // 주문 상세 조회 (소유자 검사 포함)
    @GetMapping("/{orderId}/me/{userId}")
    public ResponseEntity<OrderDetailDto> detail(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderDetailDto dto = queryService.getOrderDetail(orderId, userId);
        return ResponseEntity.ok(dto);
    }

}
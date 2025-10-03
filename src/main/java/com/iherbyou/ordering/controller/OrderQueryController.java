package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/orders")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    // 주문 목록 (최근순)
    @GetMapping
    public Page<OrderSummaryDto> list(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));
        return orderQueryService.listUserOrders(userId, pageable);
    }

    // 주문 상세 (상품/배송 포함)
    @GetMapping("/{orderId}")
    public OrderDetailDto detail(
            @PathVariable Long userId,
            @PathVariable Long orderId
    ) {
        return orderQueryService.getOrderDetail(orderId, userId);
    }

}
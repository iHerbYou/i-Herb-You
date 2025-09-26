package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 주문 조회(목록/상세) API 컨트롤러 (마이페이지에서 자신의 주문들 조회할 때 사용)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    // 주문 목록 조회 (최근 주문부터 페이지네이션해 반환)
    @GetMapping
    public Page<OrderSummaryDto> list(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));
        return orderQueryService.listUserOrders(userId, pageable);
    }

    // 주문 상세 조회 (주문 상품, 배송 정보까지 포함한 상세 데이터 반환)
    @GetMapping("/{orderId}")
    public OrderDetailDto detail(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long orderId
    ) {
        return orderQueryService.getOrderDetail(orderId, userId);
    }

}

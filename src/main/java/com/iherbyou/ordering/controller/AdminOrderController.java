package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.PageResponseDto;
import com.iherbyou.ordering.dto.admin.AdminOrderDetailDto;
import com.iherbyou.ordering.dto.admin.AdminOrderSearchCondition;
import com.iherbyou.ordering.dto.admin.AdminOrderSummaryDto;
import com.iherbyou.ordering.service.OrderAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN_MASTER','ADMIN_BUSINESS','ADMIN_DEVELOP','ADMIN_MARKETING')")
public class AdminOrderController {

    private static final List<String> ALLOWED_SORTS = List.of("orderDate", "totalPrice", "id");
    private final OrderAdminService orderAdminService;

    @GetMapping
    public ResponseEntity<PageResponseDto<AdminOrderSummaryDto>> searchOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer orderStatusValue,
            @RequestParam(required = false) Integer paymentStatusValue,
            @RequestParam(required = false) Integer paymentMethodValue,
            @RequestParam(required = false) Integer deliveryStatusValue,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "orderDate,desc") String sort
    ) {
        validateRequest(startDate, endDate, page, size);

        AdminOrderSearchCondition condition = AdminOrderSearchCondition.builder()
                .orderDateFrom(toStartDateTime(startDate))
                .orderDateTo(toEndExclusive(endDate))
                .orderStatusValue(orderStatusValue)
                .paymentStatusValue(paymentStatusValue)
                .paymentMethodValue(paymentMethodValue)
                .deliveryStatusValue(deliveryStatusValue)
                .userId(userId)
                .userEmail(userEmail)
                .build();

        Pageable pageable = buildPageable(page, size, sort);
        Page<AdminOrderSummaryDto> result = orderAdminService.searchOrders(condition, pageable);
        return ResponseEntity.ok(PageResponseDto.from(result, page + 1));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<AdminOrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderAdminService.getOrderDetail(orderId));
    }

    private Pageable buildPageable(int page, int size, String sortParam) {
        int safePage = Math.max(0, page);
        int safeSize = Math.max(1, Math.min(size, 200));

        Sort sort = Sort.by(Sort.Direction.DESC, "orderDate");
        if (sortParam != null && !sortParam.isBlank()) {
            String[] tokens = sortParam.split(",", 2);
            String property = tokens[0];
            if (ALLOWED_SORTS.contains(property)) {
                Sort.Direction direction = (tokens.length > 1 && "asc".equalsIgnoreCase(tokens[1]))
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
                sort = Sort.by(direction, property);
            }
        }
        return PageRequest.of(safePage, safeSize, sort);
    }

    private LocalDateTime toStartDateTime(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    private LocalDateTime toEndExclusive(LocalDate date) {
        return date != null ? date.plusDays(1).atStartOfDay() : null;
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be greater or equal to 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        if (size > 500) {
            throw new IllegalArgumentException("size must be less or equal to 500");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate");
        }
    }
}

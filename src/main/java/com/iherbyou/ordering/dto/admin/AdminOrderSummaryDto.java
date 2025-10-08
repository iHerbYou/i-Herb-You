package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrderSummaryDto {

    private final Long orderId;
    private final LocalDateTime orderDate;
    private final Integer orderStatusValue;
    private final String orderStatusName;
    private final Integer totalPrice;

    private final Long userId;
    private final String userEmail;
    private final String userName;

    private final Integer paymentStatusValue;
    private final String paymentStatusName;
    private final Integer paymentMethodValue;
    private final String paymentMethodName;

    private final Integer deliveryStatusValue;
    private final String deliveryStatusName;
}

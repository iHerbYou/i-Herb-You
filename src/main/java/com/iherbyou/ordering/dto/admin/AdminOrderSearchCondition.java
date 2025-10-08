package com.iherbyou.ordering.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class AdminOrderSearchCondition {

    private final LocalDateTime orderDateFrom;
    private final LocalDateTime orderDateTo;
    private final Integer orderStatusValue;
    private final Integer paymentStatusValue;
    private final Integer paymentMethodValue;
    private final Integer deliveryStatusValue;
    private final Long userId;
    private final String userEmail;

    public boolean hasOrderDateFrom() {
        return orderDateFrom != null;
    }

    public boolean hasOrderDateTo() {
        return orderDateTo != null;
    }

    public boolean hasOrderStatus() {
        return orderStatusValue != null;
    }

    public boolean hasPaymentStatus() {
        return paymentStatusValue != null;
    }

    public boolean hasPaymentMethod() {
        return paymentMethodValue != null;
    }

    public boolean hasDeliveryStatus() {
        return deliveryStatusValue != null;
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasUserEmail() {
        return userEmail != null && !userEmail.isBlank();
    }
}

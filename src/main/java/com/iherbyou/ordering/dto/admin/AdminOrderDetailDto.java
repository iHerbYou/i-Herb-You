package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrderDetailDto {

    private final Long orderId;
    private final LocalDateTime orderDate;
    private final Integer orderStatusValue;
    private final String orderStatusName;
    private final Integer subtotal;
    private final Integer deliveryFee;
    private final Integer discount;
    private final Integer totalPrice;
    private final String customsInfo;

    private final Long userId;
    private final String userEmail;
    private final String userName;

    private final AdminShippingAddressDto shippingAddress;
    private final AdminDeliveryInfoDto delivery;
    private final AdminPaymentInfoDto payment;

    private final List<AdminOrderLineItemDto> items;
    private final List<AdminRefundSummaryDto> refunds;
    private final List<AdminOrderStatusHistoryDto> histories;
}

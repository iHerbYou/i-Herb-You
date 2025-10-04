package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminRefundSummaryDto {

    private final Long refundId;
    private final BigDecimal amount;
    private final Integer statusValue;
    private final String statusName;
    private final Integer reasonValue;
    private final String reasonName;
    private final Integer deliveryOptionValue;
    private final String deliveryOptionName;
    private final LocalDateTime requestedAt;
    private final LocalDateTime completedAt;
}

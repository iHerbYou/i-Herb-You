package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminPaymentInfoDto {

    private final Long paymentId;
    private final BigDecimal amount;
    private final Integer statusValue;
    private final String statusName;
    private final Integer methodValue;
    private final String methodName;
    private final LocalDateTime requestedAt;
    private final LocalDateTime paidAt;
    private final String externalOrderKey;
}

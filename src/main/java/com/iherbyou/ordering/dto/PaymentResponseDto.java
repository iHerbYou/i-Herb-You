package com.iherbyou.ordering.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponseDto {

    private final Long paymentId;
    private final Long orderId;
    private final BigDecimal paymentPrice;
    private final Integer paymentStatusKey;
    private final Integer paymentMethodKey;
    private final LocalDateTime requestedAt;
    private final LocalDateTime paidAt;
    private final String externalOrderKey;

}

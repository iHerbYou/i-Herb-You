package com.iherbyou.ordering.dto;

import com.iherbyou.ordering.Refund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RefundResponseDto {

    private Long refundId;
    private Long paymentId;
    private BigDecimal amount;
    private Integer statusKey;
    private LocalDateTime requestedAt;

    public static RefundResponseDto from(Refund refund) {
        return RefundResponseDto.builder()
                .refundId(refund.getId())
                .paymentId(refund.getPayment().getId())
                .amount(refund.getAmount())
                .statusKey(refund.getStatusCode() != null ? refund.getStatusCode().getValue() : null)
                .requestedAt(refund.getRequestedAt())
                .build();
    }
}

package com.iherbyou.ordering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RefundRequestDto {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @NotNull
    private Integer reasonCodeValue; // 61번 그룹(REFUND_REASON)의 코드 값

    @NotNull
    private Integer deliveryOptionCodeValue; // 62번 그룹(REFUND_DELIVERY_OPTION)의 코드 값

}

package com.iherbyou.ordering.dto;

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

    private BigDecimal amount;
    private String reasonCodeKey;
    private String deliveryOptionCodeKey;

}

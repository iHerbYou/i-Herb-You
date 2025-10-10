package com.iherbyou.ordering.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentRequestDto {

    @NotNull
    private Integer methodCodeValue; // 41번 그룹(PAYMENT_METHOD)의 코드 값

}

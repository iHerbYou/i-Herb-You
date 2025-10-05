package com.iherbyou.promotion.coupon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RedeemCouponRequest {

    @NotNull
    private Long userCouponId;

    @Min(0)
    private int discountAmount;
}

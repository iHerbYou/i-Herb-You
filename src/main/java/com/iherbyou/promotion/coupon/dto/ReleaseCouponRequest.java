package com.iherbyou.promotion.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReleaseCouponRequest {

    @NotNull
    private Long userCouponId;
}

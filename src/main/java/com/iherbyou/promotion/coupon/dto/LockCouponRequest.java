package com.iherbyou.promotion.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LockCouponRequest {

    @NotBlank
    private String couponCode;
}

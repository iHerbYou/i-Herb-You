package com.iherbyou.promotion.coupon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WelcomeCouponResponse {

    private final boolean issued;
    private final UsableCouponDto coupon;

    public static WelcomeCouponResponse issuedResponse(UsableCouponDto coupon) {
        return WelcomeCouponResponse.builder()
                .issued(true)
                .coupon(coupon)
                .build();
    }

    public static WelcomeCouponResponse skippedResponse() {
        return WelcomeCouponResponse.builder()
                .issued(false)
                .coupon(null)
                .build();
    }
}

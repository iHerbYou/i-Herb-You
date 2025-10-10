package com.iherbyou.promotion.coupon.dto;

import com.iherbyou.user.entity.UserCoupon;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UsableCouponDto {

    private final Long userCouponId;
    private final String name;
    private final Double amount;
    private final LocalDate expiresAt;
    private final boolean combinable;

    public static UsableCouponDto from(UserCoupon userCoupon) {
        return UsableCouponDto.builder()
                .userCouponId(userCoupon.getId())
                .name(userCoupon.getCoupon().getName())
                .amount(userCoupon.getCoupon().getDiscountValue())
                .expiresAt(userCoupon.getExpiredAt())
                .combinable(userCoupon.getCoupon().isCombinableWithOthers())
                .build();
    }
}

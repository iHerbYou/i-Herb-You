package com.iherbyou.promotion.coupon.dto;

import com.iherbyou.user.entity.UserCoupon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponLockResultDto {

    private final Long userCouponId;

    public static CouponLockResultDto from(UserCoupon coupon) {
        return CouponLockResultDto.builder()
                .userCouponId(coupon.getId())
                .build();
    }
}

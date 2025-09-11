package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class UserCoupon {

    //TODO 팀원들과 리뷰

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon couponId;

}

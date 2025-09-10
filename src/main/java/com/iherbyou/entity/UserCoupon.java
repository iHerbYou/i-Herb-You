package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_coupon")   // 테이블명은 소문자로
public class UserCoupon {

    @Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon couponId;

    public UserCoupon() {
    }
}

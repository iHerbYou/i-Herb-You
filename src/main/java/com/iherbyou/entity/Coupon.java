package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 100, nullable = false, unique = true)
    private String code;

    @Column(name = "discount_type", length = 50)
    private String discountType;

    @Column(name = "discount_value")
    private Double discountValue;

    @Column(name = "min_order_amount")
    private Integer minOrderAmount;

    @Column(name = "coupon_starts_at")
    private LocalDate couponStartsAt;

    @Column(name = "coupon_ends_at")
    private LocalDate couponEndsAt;
}

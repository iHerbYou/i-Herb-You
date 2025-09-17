package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // 사용된 주문

    private LocalDateTime issuedAt; // 발행일

    private LocalDate expiredAt; // 만료일

    private LocalDateTime usedAt; // 사용일

    private boolean isUsed; // 사용 여부 (0 = 미사용, 1 = 사용)
}

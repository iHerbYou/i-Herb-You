package com.iherbyou.user.entity;

import com.iherbyou.ordering.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    @Column(nullable = false)
    private LocalDateTime issuedAt; // 발행일

    @Column(nullable = false)
    private LocalDate expiredAt; // 만료일

    @Column
    private LocalDateTime usedAt; // 사용일

    @Builder.Default
    @Column(nullable = false)
    private boolean isUsed = false; // 사용 여부 (0 = 미사용, 1 = 사용)

    public boolean isExpired(LocalDate today) {
        return expiredAt.isBefore(today);
    }

    public boolean isAvailable(LocalDate today) {
        return !isUsed && !isExpired(today) && order == null;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public void markUsed(Order order) {
        this.order = order;
        this.usedAt = LocalDateTime.now();
        this.isUsed = true;
    }

    public void release() {
        this.order = null;
        this.usedAt = null;
        this.isUsed = false;
    }

    public void expire() {
        this.order = null;
        this.usedAt = LocalDateTime.now();
        this.isUsed = true;
    }

}
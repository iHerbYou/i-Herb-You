package com.iherbyou.user;

import com.iherbyou.ordering.Order;
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

    @Column(nullable = false)
    private LocalDateTime usedAt; // 사용일

    @Column(nullable = false)
    private boolean isUsed; // 사용 여부 (0 = 미사용, 1 = 사용)

}

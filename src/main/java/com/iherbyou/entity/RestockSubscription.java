package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class RestockSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 재입고 구독 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 회원 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;  // 상품 옵션 ID

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;    // 구독 활성화 여부

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime subscribedAt; // 신청된 시각

    @UpdateTimestamp
    @Column
    private LocalDateTime lastNotifiedAt;   // 마지막 알림 발송 시각
}

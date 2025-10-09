package com.iherbyou.catalog.entity;

import com.iherbyou.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class RestockSubscription { // 재입고 알림

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant; // 상품 옵션 ID

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive; // 구독 활성화 여부

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime subscribedAt; // 신청된 시각

    @UpdateTimestamp
    @Column
    private LocalDateTime lastNotifiedAt; // 마지막 알림 발송 시각

    // 상태 전환 메서드
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    // 알림 발송 완료 처리
    public void markAsNotified() {
        this.isActive = false;
        this.lastNotifiedAt = LocalDateTime.now();
    }

}
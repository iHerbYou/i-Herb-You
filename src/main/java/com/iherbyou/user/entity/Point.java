package com.iherbyou.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Point { // 현재 잔액과 생성/수정 일자 저장 (포인트 내역은 PointHistory 에서 관리)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer balance = 0; // 포인트 잔액

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void earn(int amount) {
        this.balance += amount;
    }

    public void spend(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }
        this.balance -= amount;
    }

    public void restore(int amount) {
        this.balance += amount;
    }

    public void expire(int amount) {
        if (amount > balance) {
            amount = balance;
        }
        this.balance -= amount;
    }

}

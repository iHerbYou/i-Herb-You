package com.iherbyou.community;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Where(clause = "is_deleted = false") // 항상 살아있는 데이터만 조회
@Check(constraints = "rating BETWEEN 1 AND 5")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private User user; // 반대쪽 구현 X

    @Column(nullable = false)
    private Integer rating; // 1~5점

    @Column(length = 1000) // 본문 길이 제한 (예: 1000자)
    private String text;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false; // soft delete flag

    @Column
    private LocalDateTime deletedAt; // 삭제 시각 기록

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // === Soft delete 메서드 ===
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}

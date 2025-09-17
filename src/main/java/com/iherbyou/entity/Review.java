package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
// ↓ Hibernate @Check 쓰면 활성화, 아니면 삭제해도 동작함
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "review",
        indexes = {
                @Index(name = "idx_review_product", columnList = "product_id"),
                @Index(name = "idx_review_user", columnList = "user_id")
        }
)
@Check(constraints = "rating BETWEEN 1 AND 5")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)                // PK
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_product"))    // FK NOT NULL
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_user"))       // FK NOT NULL
    private User user;

    @Column(name = "rating", nullable = false)                   // TINYINT, 1~5
    private Integer rating;

    @Lob
    @Column(name = "text", columnDefinition = "TEXT")            // 본문(NULL 허용)
    private String text;

    @Column(name = "is_deleted", nullable = false,               // TINYINT DEFAULT 0
            columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false,               // DEFAULT CURRENT_TIMESTAMP
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}

package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_review_product", columnList = "product_id"), @Index(name = "idx_review_user", columnList = "user_id")}
        // 중복 허용 정책에 따라 다음과 같은 유니크를 둘 수도 있음:
        // , uniqueConstraints = @UniqueConstraint(
        //     name = "uk_review_order_item",
        //     columnNames = {"order_item_id"}
        // )
)
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰 작성자 (User 엔티티가 있다고 가정)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_review_user"))
    private User user;

    // 대상 상품
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_review_product"))
    private Product product;

    // 1~5점
    @Column(nullable = false)
    private Integer rating;

    @Column(length = 100)
    private String title;

    // 길이가 길 수 있어 TEXT 권장 (DB에 따라 columnDefinition 수정)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    // 구매자만 리뷰 강제하고 싶을 때: 해당 주문상품과 연결(정규 FK로 모델링해도 됨)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Column(nullable = false)
    private Boolean deleted = false;

    @PreUpdate
    public void touchUpdatedAt() {
        this.updatedAt = OffsetDateTime.now();
    }
}
package com.iherbyou.catalog.entity;

import com.iherbyou.community.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품 id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand; // 브랜드 id (FK, NOT NULL)

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<ProductCategory> productCategories = new ArrayList<>();

    @Column(length = 150, nullable = false)
    private String name; // 상품명(최대 150)

    @Column(length = 30)
    private String code; // 내부 관리용 코드(SPU)

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description; // 상세 설명

    @Lob
    @Column(columnDefinition = "TEXT")
    private String instruction; // 사용법

    @Lob
    @Column(columnDefinition = "TEXT")
    private String ingredients; // 성분 정보

    @Lob
    @Column(columnDefinition = "TEXT")
    private String cautions; // 주의사항

    @Lob
    @Column(columnDefinition = "TEXT")
    private String disclaimer; // 면책사항

    @Column
    private LocalDateTime saleStartDate; // 판매 시작일

    @Column
    private Integer expirationDate; // 소비기한(개월 수)

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 6")
    private Integer maxQtyPerOrder; // 최대구매한도(기본 6)

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductImg> productImgs = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductVariant> productVariants = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>(); // 댓글 조회

    // 새로 추가 - 정렬용 집계 컬럼
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sales;  // 판매량

    @Column
    private Double avgRating;  // 평균 평점

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reviewCount;    // 리뷰 개수

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer minPrice; // 상품 옵션 중 최저가

    // 엔티티 내부에서 직접 minPrice 자동 갱신
    @PrePersist
    @PreUpdate
    private void updateMinPrice() {
        this.minPrice = productVariants.stream()
                .map(ProductVariant::getSalePrice)
                .filter(Objects::nonNull)
                .min(Integer::compareTo)
                .orElse(0);
    }

}

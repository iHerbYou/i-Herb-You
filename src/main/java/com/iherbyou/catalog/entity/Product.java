package com.iherbyou.catalog.entity;

import com.iherbyou.community.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product")
    private List<ProductImg> productImgs = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> productVariants = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>(); // 댓글 조회
}

package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_product_brand", columnList = "brand_id"),
                @Index(name = "idx_product_category", columnList = "category_id"),
                @Index(name = "idx_product_name", columnList = "product_name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_code", columnNames = {"product_code"})
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)                         // 상품 id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_brand"))               // 브랜드 id (FK, NOT NULL)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category"))            // 카테고리 id (FK, NOT NULL)
    private Category category;

    @Column(name = "product_name", length = 150, nullable = false)         // 상품명(최대 150)
    private String name;

    @Column(name = "product_code", length = 30)                            // 내부 관리용 코드(SPU)
    private String code;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")               // 상세 설명
    private String description;

    @Lob
    @Column(name = "how_to_use", columnDefinition = "TEXT")                // 사용법
    private String howToUse;

    @Lob
    @Column(name = "ingredients", columnDefinition = "TEXT")               // 성분 정보
    private String ingredients;

    @Lob
    @Column(name = "cautions", columnDefinition = "TEXT")                  // 주의사항
    private String cautions;

    @Lob
    @Column(name = "disclaimer", columnDefinition = "TEXT")                // 면책사항
    private String disclaimer;

    @Column(name = "sale_start_date")                                      // 판매 시작일
    private LocalDateTime saleStartDate;

    @Column(name = "expiration_date")                                      // 소비기한(개월 수)
    private Integer expirationDate;

    // 최대구매한도(기본 6)
    @Column(
            name = "max_qty_per_order",
            nullable = false,
            columnDefinition = "SMALLINT UNSIGNED DEFAULT 6"
    )
    private Integer maxQtyPerOrder = 6;
}

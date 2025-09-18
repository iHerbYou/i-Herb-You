package com.iherbyou.catalog;

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
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "product_variant")
    private List<RestockSubscription> restockSubscriptions = new ArrayList<>(); // 관리자 페이지에서 유용

    @OneToOne(mappedBy = "product_variant", fetch = FetchType.LAZY)
    private Stock stock;

    @Column(length = 100)
    private String variantName; // 120정, 60정

    @Column
    private Integer listPrice; // 정가

    @Column
    private Integer salePrice; // 판매가

    @Column
    private Integer volume; // 약 통의 부피

    @Column(length = 50)
    private String upcCode;

    @Column(length = 50)
    private String pillSize; // 알약 크기 (5mm x 3mm x 7mm)

    @Lob
    @Column(columnDefinition = "TEXT") // 긴 텍스트(예: 수천~수만 글자)
    private String nutritionFacts; // 영양 성분 정보

    @Column
    private Integer maxQtyPerOrder; // 옵션별 구매 한도

    @Column
    private LocalDateTime restockEta; // 재입고 예정일

}

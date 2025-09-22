package com.iherbyou.catalog.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductDetailDTO {

    private Long id;
    private String name;

    // 브랜드 정보
    private BrandDTO brand;

    // 카테고리 내역 (Breadcrumb)
    private List<String> breadcrumbs;

    // 상품 집계 정보
    private Double avgRating;
    private Integer reviewCount;
    private Integer sales;

    // 기본 정보
    private Integer expirationDate;
    private String code;

    // 옵션별 가격 및 재고
    private List<VariantDTO> variants;

    // 이미지들
    private List<String> images;

    // 상품 상세 설명 영역
    private InfoDTO info;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VariantDTO {
        private Long id;
        private String variantName;
        private Integer salePrice;
        private Integer listPrice;
        private Integer stock;
        private String upcCode;
        private String pillSize;
        private LocalDateTime restockEta;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InfoDTO {
        private String description;
        private String instruction;
        private String ingredients;
        private String cautions;
        private String disclaimer;
        private String nutritionFacts;
        private Integer volume;
        private Integer weight;
        private LocalDateTime saleStartDate;
    }

}

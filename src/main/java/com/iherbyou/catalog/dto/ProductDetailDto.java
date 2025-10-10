package com.iherbyou.catalog.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductDetailDto {     // 상품 상세정보 dto

    private Long id;
    private String name;

    // 브랜드
    private Long brandId;   // 브랜드 페이지 이동용
    private String brandName;

    // 카테고리
    private List<String> categories;

    // 상품 집계 정보
    private Double avgRating;
    private Integer reviewCount;
    private String code;
    private Integer expirationDate;
    private LocalDateTime saleStartDate;

    // 비주얼 영역
    private List<ImageDto> images;

    // 구매 관련 - 옵션별 가격 및 재고
    private List<VariantDTO> variants;

    // 상품 상세 설명 영역
    private String description;
    private String instruction;
    private String ingredients;
    private String cautions;
    private String disclaimer;
    private String nutritionFacts;
    private String pillSize;

    // 내부 DTO
    @Getter
    @Builder
    public static class ImageDto {
        private String url;
        private boolean isPrimary;
    }

    @Getter
    @Builder
    public static class VariantDTO {
        private Long id;
        private String variantName;
        private Integer listPrice;
        private Integer salePrice;
        private Integer stock;
        private boolean soldOut;
        private String upcCode;
        private LocalDateTime restockEta;
        private boolean restockSubscriptionEnabled;
    }

}

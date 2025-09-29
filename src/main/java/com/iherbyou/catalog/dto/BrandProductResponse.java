package com.iherbyou.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrandProductResponse {     // 브랜드 별 상품 응답 dto

    private Long id;
    private String name;
    private int price;
    private double rating;
    private int reviewCount;
    private String thumbnailUrl;

}

package com.iherbyou.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrandResponse {    // 브랜드 목록 응답 dto

    private Long id;
    private String name;
    private int productCount;
    private String thumbnailUrl;

}

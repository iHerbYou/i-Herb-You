package com.iherbyou.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryFlatDto {  // 카테고리 목록 dto (flat 응답)

    private Long categoryId;
    private String name;
    private Long parentId;
    private int depth;

}

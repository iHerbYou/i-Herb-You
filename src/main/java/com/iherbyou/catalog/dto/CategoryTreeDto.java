package com.iherbyou.catalog.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryTreeDto {

    private Long id;
    private String name;
    private Long parentId;
    private int depth;

    // depth=1 → children만, depth=2 → items만, depth=3 → 배열 없음
    private List<CategoryTreeDto> children;  // 1뎁스 전용
    private List<CategoryTreeDto> items;     // 2뎁스 전용

    public CategoryTreeDto(Long id, String name, Long parentId, int depth) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.depth = depth;
        
        // 뎁스별로 필요한 배열만 초기화
        if (depth == 1) {
            // 최상위: children 배열만
            this.children = new ArrayList<>();
            this.items = null;
        } else if (depth == 2) {
            // 2뎁스: items 배열만
            this.children = null;
            this.items = new ArrayList<>();
        } else {
            // depth=3 (leaf) - 배열 없음
            this.children = null;
            this.items = null;
        }
    }

}

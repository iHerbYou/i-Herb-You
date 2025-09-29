package com.iherbyou.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryTreeDto {

    private Long id;
    private String name;
    private Long parentId;
    private int depth;
    private List<CategoryTreeDto> children;

    public CategoryTreeDto(Long id, String name, Long parentId, int depth) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.depth = depth;
    }

}

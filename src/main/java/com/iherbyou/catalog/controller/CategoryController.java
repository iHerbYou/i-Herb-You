package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.entity.Category;
import com.iherbyou.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 전체 카테고리 조회 (flat 리스트) - 관리/검색/필터링 용
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/tree")    // 화면 표시용
    public ResponseEntity<List<Category>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    // 특정 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory((id)));
    }

    // 특정 카테고리의 하위 카테고리 조회
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<Category>> getSubCategories(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getSubCategories(id));
    }

}

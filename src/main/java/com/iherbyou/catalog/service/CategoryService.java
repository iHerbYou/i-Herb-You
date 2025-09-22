package com.iherbyou.catalog.service;

import com.iherbyou.catalog.Category;
import com.iherbyou.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }

    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    public List<Category> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        
        Map<Long, Category> map = categories.stream()
                .collect(Collectors.toMap(Category::getId, c -> c));
        
        List<Category> rootCategories = new ArrayList<>();
        
        for (Category category : categories) {
            if (category.getParent() != null) {
                Category parent = map.get(category.getParent().getId());
                if (parent != null) { // null 체크 추가
                    parent.getChildren().add(category);
                }
            } else {
                rootCategories.add(category);
            }
        }
        
        return rootCategories;
    }

}

package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.CategoryFlatDto;
import com.iherbyou.catalog.dto.CategoryTreeDto;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.entity.Category;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.repository.CategoryRepository;
import com.iherbyou.catalog.repository.ProductRepository;
import com.iherbyou.exception.catalog.CategoryNotFoundException;
import com.iherbyou.exception.catalog.InvalidParentIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // 모든 카테고리 목록 flat 형태로 가져오기
    public List<CategoryFlatDto> getAllCategoriesFlat() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException(null); // CATEGORY_NOT_FOUND
        }

        return categories.stream()
                .map(c -> new CategoryFlatDto(
                        c.getId(),
                        c.getName(),
                        c.getParent() != null ? c.getParent().getId() : null,
                        getDepth(c)
                ))
                .collect(Collectors.toList());
    }

    // id로 카테고리 단건 조회, flat 구조 dto로 변환
    public CategoryFlatDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return new CategoryFlatDto(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                getDepth(category)
        );
    }

    // 부모 카테고리의 모든 하위 카테고리 가져오기
    public List<Category> getSubCategories(Long parentId) {
        if (!categoryRepository.existsById(parentId)) {
            throw new InvalidParentIdException(parentId);
        }
        return categoryRepository.findByParentId(parentId);
    }

    // 카테고리 목록 트리형태로 가져오기
    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException(null);
        }

        // 모든 카테고리를 DTO로 1:1 매핑
        Map<Long, CategoryTreeDto> dtoMap = new HashMap<>();
        for (Category c : categories) {
            Long parentId = (c.getParent() != null) ? c.getParent().getId() : null;
            dtoMap.put(
                    c.getId(),
                    new CategoryTreeDto(c.getId(), c.getName(), parentId, getDepth(c))
            );
        }

        // 부모-자식 연결
        List<CategoryTreeDto> roots = new ArrayList<>();
        for (Category c : categories) {
            CategoryTreeDto dto = dtoMap.get(c.getId());
            Long parentId = dtoMap.get(c.getId()).getParentId();
            if (parentId == null) {
                roots.add(dto);
            } else {
                CategoryTreeDto parentDto = dtoMap.get(parentId);
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }
        return roots;
    }

    // 특정 카테고리의 상품 목록 가져오기
    public List<ProductListDto> getProductsByCategory(Long categoryId) {
        // 카테고리 존재 여부 확인
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        List<Product> products = productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(ProductListDto::fromEntity)
                .toList();
    }

    // 카테고리 단계(대/중/소) 계산
    private int getDepth(Category category) {
        int depth = 1;
        Category parent = category.getParent();

        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }

}

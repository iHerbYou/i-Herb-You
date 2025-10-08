package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.dto.SuggestionDto;
import com.iherbyou.catalog.dto.SuggestionResponse;
import com.iherbyou.catalog.entity.Category;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.repository.CategoryRepository;
import com.iherbyou.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductListDto> search(String query, int page, int size, String sort) {
        log.info("SearchService.search() called with query: '{}', page: {}, size: {}, sort: {}", query, page, size, sort);

        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty search query provided");
            return Page.empty();
        }

        String trimmedQuery = query.trim();
        Pageable pageable = PageRequest.of(page, size, getSort(sort));

        // 1. 먼저 카테고리명과 정확히 일치하는지 확인
        Optional<Category> category = categoryRepository.findByName(trimmedQuery);
        log.info("Category search result for '{}': {}", trimmedQuery, category.isPresent() ? "Found category ID: " + category.get().getId() : "No category found");

        Page<ProductListDto> result;
        if (category.isPresent()) {
            // 카테고리 상품조회
            log.info("Searching products by category ID: {}", category.get().getId());
            result = productRepository.findByCategoryId(category.get().getId(), pageable)
                    .map(ProductListDto::fromEntity);

            // 카테고리 검색 결과가 없으면 상품명 검색도 시도
            if (result.getTotalElements() == 0) {
                log.info("No products found in category, trying product name search");
                result = productRepository.searchByKeyword(trimmedQuery, pageable)
                        .map(ProductListDto::fromEntity);
            }
        } else {
            // 2. 상품명, 브랜드명 LIKE 검색
            log.info("Searching products by keyword: '{}'", trimmedQuery);
            result = productRepository.searchByKeyword(trimmedQuery, pageable)
                    .map(ProductListDto::fromEntity);
        }

        log.info("Search completed. Found {} products out of {} total elements",
                result.getNumberOfElements(), result.getTotalElements());

        // 결과가 없으면 더 관대한 검색 시도
        if (result.getTotalElements() == 0 && trimmedQuery.length() > 1) {
            log.info("No results found, trying partial search for each character");
            // 한글의 경우 부분 검색도 시도
            result = productRepository.searchByKeyword(trimmedQuery.substring(0, Math.min(trimmedQuery.length(), 2)), pageable)
                    .map(ProductListDto::fromEntity);
            log.info("Partial search completed. Found {} products", result.getTotalElements());
        }

        return result;
    }

    private Sort getSort(String sort) {
        return switch (sort) {
            case "priceAsc" -> Sort.by("minPrice").ascending();
            case "priceDesc" -> Sort.by("minPrice").descending();
            case "rating" -> Sort.by(Sort.Direction.DESC, "avgRating");
            case "review" -> Sort.by(Sort.Direction.DESC, "reviewCount");
            default -> Sort.by(Sort.Direction.DESC, "sales");
        };
    }

    public SuggestionResponse getSuggestions(String query) {
        log.info("Getting suggestions for query: '{}'", query);

        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty query provided for suggestions");
            return new SuggestionResponse(List.of(), List.of(), null);
        }

        String trimmedQuery = query.trim();

        try {
            // 카테고리 Top3
            List<Category> topCategories = categoryRepository
                    .findTopCategoriesByKeyword(trimmedQuery, PageRequest.of(0, 3));
            List<SuggestionDto> categoryDtos = topCategories.stream()
                    .map(c -> new SuggestionDto(c.getId(), c.getName(), "category"))
                    .toList();
            log.info("Found {} category suggestions", categoryDtos.size());

            // 상품 top7
            List<Product> topProducts = productRepository
                    .findTopProductsByKeyword(trimmedQuery, PageRequest.of(0, 7));
            List<SuggestionDto> productDtos = topProducts.stream()
                    .map(p -> new SuggestionDto(p.getId(), p.getName(), "product"))
                    .toList();
            log.info("Found {} product suggestions", productDtos.size());

            // 카테고리 이름 정확 일치 여부 (리다이렉트용)
            Long redirectCategoryId = categoryRepository.findByName(trimmedQuery)
                    .map(Category::getId)
                    .orElse(null);

            if (redirectCategoryId != null) {
                log.info("Found exact category match for '{}': ID {}", trimmedQuery, redirectCategoryId);
            }

            return new SuggestionResponse(categoryDtos, productDtos, redirectCategoryId);

        } catch (Exception e) {
            log.error("Error getting suggestions for query '{}': {}", trimmedQuery, e.getMessage());
            return new SuggestionResponse(List.of(), List.of(), null);
        }
    }

}
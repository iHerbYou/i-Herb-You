package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.ProductDetailDto;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.service.ProductService;
import com.iherbyou.exception.catalog.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 목록 조회 API
    @GetMapping
    public ResponseEntity<Page<ProductListDto>> getProducts(
            @RequestParam(defaultValue = "1") int page,     // 기본 페이지 = 1
            @RequestParam(defaultValue = "24") int size,    // 한 페이지 24개
            @RequestParam(required = false) Boolean excludeSoldOut,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        // page/size 유효성 검사
        if (page < 1 || size <= 0) {
            throw new InvalidParameterException("page와 size는 1 이상의 값이어야 합니다.");
        }

        // direction 유효성 검사 및 변환
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("direction은 asc 또는 desc만 가능합니다.");
        }

        // 3) sort 매핑 유효성 검사
        String sortField = mapSortField(sort);
        if (sortField == null) {
            throw new InvalidParameterException("지원하지 않는 정렬 기준입니다.");
        }

        // 받은 sort값 실제 db명으로 매핑
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortDirection, sortField));

        // 페이징 + 필터 옵션 서비스로 전달
        Page<ProductListDto> products = productService.getProducts(pageable, excludeSoldOut, minPrice, maxPrice, categoryId);

        return ResponseEntity.ok(products);
    }

    private String mapSortField(String sort) {
        return switch (sort.toLowerCase()) {
            case "sales" -> "sales";
            case "rating" -> "avgRating";
            case "reviews" -> "reviewCount";
            case "price" -> "minPrice";
            default -> "id";
        };
    }

    // 상품 상세 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long id) {
        ProductDetailDto dto = productService.getProductDetail(id);

        return ResponseEntity.ok(dto);
    }

    // 메인 - 상품 베스트셀러 API
    @GetMapping("/bestsellers")
    public ResponseEntity<List<ProductListDto>> getBestsellers(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "8") int size) {

        Page<ProductListDto> bestsellers = productService.findBestsellers(categoryId, size);
        return ResponseEntity.ok(bestsellers.getContent());
    }

}

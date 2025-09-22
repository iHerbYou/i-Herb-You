package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.DTO.ProductDetailDTO;
import com.iherbyou.catalog.DTO.ProductListDTO;
import com.iherbyou.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 목록 조회 API
    @GetMapping
    public ResponseEntity<Page<ProductListDTO>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(required = false) Boolean excludeSoldOut,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, mapSortField(sort)));

        Page<ProductListDTO> products = productService.getProducts(pageable, excludeSoldOut, minPrice, maxPrice);

        return ResponseEntity.ok(products);
    }

    private String mapSortField(String sort) {
        return switch (sort.toLowerCase()) {
            case "sales"   -> "sales";
            case "rating"  -> "avgRating";
            case "reviews" -> "reviewCount";
            case "price"   -> "minPrice";
            default        -> "id";
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long id) {
        ProductDetailDTO dto = productService.getProductDetail(id);

        return ResponseEntity.ok(dto);
    }
}

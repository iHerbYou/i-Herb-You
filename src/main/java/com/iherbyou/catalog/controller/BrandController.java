package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.BrandProductResponse;
import com.iherbyou.catalog.dto.BrandResponse;
import com.iherbyou.catalog.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // 브랜드 전체 조회
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    // 특정 브랜드 상품 조회
    @GetMapping("/{brandId}/products")
    public ResponseEntity<List<BrandProductResponse>> getProductsByBrand(
            @PathVariable Long brandId
    ) {
        return ResponseEntity.ok(brandService.getProductsByBrand(brandId));
    }

}

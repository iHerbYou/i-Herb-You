package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.BrandProductResponse;
import com.iherbyou.catalog.dto.BrandResponse;
import com.iherbyou.catalog.entity.Brand;
import com.iherbyou.catalog.entity.ProductImg;
import com.iherbyou.catalog.repository.BrandRepository;
import com.iherbyou.exception.catalog.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    // 모든 브랜드 목록 가져오기
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> {
                    String thumbnailUrl = null;
                    if (!brand.getProducts().isEmpty()) {
                        thumbnailUrl = brand.getProducts().get(0).getProductImgs().stream()
                                .filter(ProductImg::getIsPrimary)   // 썸네일 이미지만
                                .findFirst()
                                .map(ProductImg::getImageUrl)       // url 컬럼
                                .orElse(null);
                    }
                    
                    return new BrandResponse(
                            brand.getId(),
                            brand.getName(),
                            brand.getProducts().size(),
                            thumbnailUrl
                    );
                })
                .collect(Collectors.toList());
    }

    // 특정 브랜드 상품 가져오기
    public List<BrandProductResponse> getProductsByBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ProductNotFoundException(brandId));

        return brand.getProducts().stream()
                .map(product -> new BrandProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getMinPrice(),
                        product.getAvgRating(),
                        product.getReviewCount(),
                        product.getProductImgs().stream()
                                .filter(ProductImg::getIsPrimary)
                                .findFirst()
                                .map(ProductImg::getImageUrl)
                                .orElse(null)
                ))
                .collect(Collectors.toList());
    }

}

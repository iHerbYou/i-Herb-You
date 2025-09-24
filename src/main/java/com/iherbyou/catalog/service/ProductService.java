package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.ProductDetailDto;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductImg;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.Stock;
import com.iherbyou.catalog.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductListDto> getProducts(Pageable pageable,
                                            Boolean excludeSoldOut,
                                            Integer minPrice,
                                            Integer maxPrice) {

        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        // 가격 범위 필터
        // 최소 가격 (Product.minPrice 기준)
        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("minPrice"), minPrice));
        }

        // 최대 가격
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);
                return cb.lessThanOrEqualTo(variantJoin.get("salePrice"), maxPrice);
            });
        }

        // 품절 상품 제외
        if (Boolean.TRUE.equals(excludeSoldOut)) {
            spec = spec.and((root, query, cb) -> {
                // 동일 상품이 여러 variant/stock에 매칭되면 페이징/카운트에 영향을 줄 수 있어 distinct 처리
                query.distinct(true);
                Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);
                Join<ProductVariant, Stock> stockJoin = variantJoin.join("stock", JoinType.INNER);
                return cb.greaterThan(stockJoin.get("amount"), 0);
            });
        }

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(ProductListDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProductDetailDto getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // : 카테고리 이름만 리스트로
        List<String> categories = product.getProductCategories().stream()
                .map(pc -> pc.getCategory().getName())
                .toList();

        // Variants 매핑
        List<ProductDetailDto.VariantDTO> variants = product.getProductVariants().stream()
                .map(v -> ProductDetailDto.VariantDTO.builder()
                        .id(v.getId())
                        .variantName(v.getVariantName())
                        .listPrice(v.getListPrice())
                        .salePrice(v.getSalePrice())
                        .stock(v.getStock() != null ? v.getStock().getAmount() : 0)
                        .soldOut(v.getStock() == null || v.getStock().getAmount() <= 0)
                        .upcCode(v.getUpcCode())
                        .restockEta(v.getRestockEta())
                        .restockSubscriptionEnabled(
                                v.getRestockSubscriptions().stream().anyMatch(r -> Boolean.TRUE.equals(r.getIsActive()))
                        )
                        .build())
                .toList();

        // 이미지
        List<ProductDetailDto.ImageDto> images = product.getProductImgs().stream()
                .map(img -> ProductDetailDto.ImageDto.builder()
                        .url(img.getImageUrl())
                        .isPrimary(Boolean.TRUE.equals(img.getIsPrimary()))
                        .build())
                .toList();

        return ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .categories(categories)
                .avgRating(product.getAvgRating())
                .reviewCount(product.getReviewCount())
                .code(product.getCode())
                .expirationDate(product.getExpirationDate())
                .saleStartDate(product.getSaleStartDate())
                .images(images)
                .variants(variants)
                .description(product.getDescription())
                .instruction(product.getInstruction())
                .ingredients(product.getIngredients())
                .cautions(product.getCautions())
                .disclaimer(product.getDisclaimer())
                .nutritionFacts(
                        product.getProductVariants().stream()
                                .map(ProductVariant::getNutritionFacts)
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null)
                )
                .pillSize(product.getProductVariants().stream()
                        .map(ProductVariant::getPillSize)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null))
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProductDetailDto.VariantDTO> getProductVariants(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return  product.getProductVariants().stream()
                .map(v -> ProductDetailDto.VariantDTO.builder()
                        .id(v.getId())
                        .variantName(v.getVariantName())
                        .salePrice(v.getSalePrice())
                        .stock(v.getStock() != null ? v.getStock().getAmount() : 0)
                        .soldOut(v.getStock() == null || v.getStock().getAmount() <= 0)
                        .upcCode(v.getUpcCode())
                        .restockEta(v.getRestockEta())
                        .restockSubscriptionEnabled(
                                v.getRestockSubscriptions().stream()
                                        .anyMatch(r -> Boolean.TRUE.equals(r.getIsActive()))
                        )
                        .build())
                .toList();
    }

}
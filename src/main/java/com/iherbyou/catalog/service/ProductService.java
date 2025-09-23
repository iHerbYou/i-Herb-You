package com.iherbyou.catalog.service;

import com.iherbyou.catalog.DTO.ProductDetailDTO;
import com.iherbyou.catalog.DTO.ProductListDTO;
import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.ProductImg;
import com.iherbyou.catalog.ProductVariant;
import com.iherbyou.catalog.Stock;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductListDTO> getProducts(Pageable pageable,
                                            Boolean excludeSoldOut,
                                            Integer minPrice,
                                            Integer maxPrice) {

        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        // 품절 제외
        if (Boolean.TRUE.equals(excludeSoldOut)) {
            spec = spec.and((root, query, cb) -> {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);
                Join<ProductVariant, Stock> stockJoin = variantJoin.join("stock", JoinType.INNER);
                return cb.greaterThan(stockJoin.get("amount"), 0);
            });
        }

        // 최소 가격
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);
                return cb.greaterThanOrEqualTo(variantJoin.get("salePrice"), minPrice);
            });
        }

        // 최대 가격
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);
                return cb.lessThanOrEqualTo(variantJoin.get("salePrice"), maxPrice);
            });
        }

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(ProductListDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProductDetailDTO getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // Breadcrumbs: 카테고리 이름만 리스트로
        List<String> breadcrumbs = product.getProductCategories().stream()
                .map(pc -> pc.getCategory().getName())
                .toList();

        // Variants 매핑
        List<ProductDetailDTO.VariantDTO> variants = product.getProductVariants().stream()
                .map(v -> ProductDetailDTO.VariantDTO.builder()
                        .id(v.getId())
                        .variantName(v.getVariantName())
                        .salePrice(v.getSalePrice())
                        .listPrice(v.getListPrice())
                        .stock(v.getStock() != null ? v.getStock().getAmount() : 0)
                        .upcCode(v.getUpcCode())
                        .pillSize(v.getPillSize())
                        .restockEta(v.getRestockEta())
                        .build())
                .toList();

        // 이미지
        List<String> images = product.getProductImgs().stream()
                .map(ProductImg::getImageUrl)
                .toList();

        // Info
        ProductDetailDTO.InfoDTO info = ProductDetailDTO.InfoDTO.builder()
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
                .volume(product.getProductVariants().stream()
                        .map(ProductVariant::getVolume)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null))
                .saleStartDate(product.getSaleStartDate())
                .build();

        return ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(new ProductDetailDTO.BrandDTO(
                        product.getBrand().getId(),
                        product.getBrand().getName()))
                .breadcrumbs(breadcrumbs)
                .avgRating(product.getAvgRating())
                .reviewCount(product.getReviewCount())
                .sales(product.getSales())
                .expirationDate(product.getExpirationDate())
                .code(product.getCode())
                .variants(variants)
                .images(images)
                .info(info)
                .build();
    }

}
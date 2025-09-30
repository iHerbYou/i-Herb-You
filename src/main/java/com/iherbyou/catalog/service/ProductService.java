package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.ProductDetailDto;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductCategory;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.Stock;
import com.iherbyou.catalog.repository.ProductRepository;
import com.iherbyou.exception.catalog.InvalidParameterException;
import com.iherbyou.exception.catalog.ProductNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductListDto> getProducts(Pageable pageable,
                                            Boolean excludeSoldOut,
                                            Integer minPrice,
                                            Integer maxPrice,
                                            Long categoryId) {

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new InvalidParameterException("minPrice은 maxPrice보다 클 수 없습니다.");
        }

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

        // minPrice > maxPrice 검사
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new InvalidParameterException("minPrice은 maxPrice보다 클 수 없습니다.");
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

        // 카테고리 필터
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<Product, ProductCategory> pcJoin = root.join("productCategories", JoinType.INNER);
                return cb.equal(pcJoin.get("category").get("id"), categoryId);
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

    // 베스트셀러 상품 가져옴
    public Page<ProductListDto> findBestsellers(Long categoryId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "sales"));

        Page<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryIdOrderBySalesDesc(categoryId, pageable);
        } else {
            products = productRepository.findAllOrderBySalesDesc(pageable);
        }

        return products.map(ProductListDto::fromEntity);
    }

    // 최근 출시된 상품 가져옴
    @Transactional(readOnly = true)
    public List<ProductListDto> findNewProducts(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "saleStartDate"));

        Page<Product> products = productRepository.findAllOrderBySaleStartDateDesc(pageable);

        return products.map(ProductListDto::fromEntity).getContent();
    }

    @Transactional(readOnly = true)
    public List<ProductListDto> findTopRatedProducts(int size) {
        Pageable pageable = PageRequest.of(0, size,
                Sort.by(Sort.Order.desc("avgRating"), Sort.Order.desc("sales")));

        Page<Product> products = productRepository.findAllOrderByAvgRatingDesc(pageable);

        return products.map(ProductListDto::fromEntity).getContent();
    }

}
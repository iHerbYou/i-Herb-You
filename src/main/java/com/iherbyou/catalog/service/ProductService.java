package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.*;
import com.iherbyou.catalog.entity.*;
import com.iherbyou.catalog.repository.*;
import com.iherbyou.exception.catalog.BrandNotFoundException;
import com.iherbyou.exception.catalog.CategoryNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductImgRepository productImgRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StockRepository stockRepository;
    private final RestockSubscriptionRepository restockSubscriptionRepository;

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

    // 별점 높은 상품 가져옴
    @Transactional(readOnly = true)
    public List<ProductListDto> findTopRatedProducts(int size) {
        Pageable pageable = PageRequest.of(0, size,
                Sort.by(Sort.Order.desc("avgRating"), Sort.Order.desc("sales")));

        Page<Product> products = productRepository.findAllOrderByAvgRatingDesc(pageable);

        return products.map(ProductListDto::fromEntity).getContent();
    }

    // admin - 상품(+브랜드, 이미지, 옵션, 재고) 등록
    @Transactional
    public Product createProduct(ProductCreateRequest dto) {
        // 브랜드 조회
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new BrandNotFoundException(dto.getBrandId()));

        // 상품 생성 및 저장
        Product product = Product.builder()
                .name(dto.getName())
                .code(dto.getProductCode())
                .description(dto.getDescription())
                .instruction(dto.getInstruction())
                .ingredients(dto.getIngredients())
                .cautions(dto.getCautions())
                .disclaimer(dto.getDisclaimer())
                .saleStartDate(LocalDateTime.now())
                .expirationDate(dto.getExpirationDate())
                .maxQtyPerOrder(dto.getMaxQtyPerOrder())
                .brand(brand)
                .build();
        productRepository.save(product);

        // 카테고리 연결
        if (dto.getCategoryIds() != null) {
            for (Long categoryId : dto.getCategoryIds()) {
                Category category = categoryRepository.findById((categoryId))
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId));

                ProductCategory pc = ProductCategory.builder()
                        .product(product)
                        .category(category)
                        .build();

                productCategoryRepository.save(pc);
            }
        }

        // 이미지 등록
        if (dto.getProductImgs() != null) {
            for (ProductImgDto imgDto : dto.getProductImgs()) {
                ProductImg img = ProductImg.builder()
                        .product(product)
                        .imageUrl(imgDto.getImageUrl())
                        .isPrimary(imgDto.isPrimary())
                        .build();

                productImgRepository.save(img);
            }
        }

        // 옵션 + 재고
        if (dto.getVariants() != null) {
            for (ProductVariantDto variantDto : dto.getVariants()) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .variantName(variantDto.getVariantName())
                        .listPrice(variantDto.getListPrice())
                        .salePrice(variantDto.getSalePrice())
                        .volume(variantDto.getVolume())
                        .upcCode(variantDto.getUpcCode())
                        .pillSize(variantDto.getPillSize())
                        .nutritionFacts(variantDto.getNutritionFacts())
                        .maxQtyPerOrder(variantDto.getMaxQtyPerOrder())
                        .restockEta(variantDto.getRestockEta())
                        .build();

                productVariantRepository.save(variant);

                StockDto stockDto = variantDto.getStock();
                if (stockDto != null) {
                    Stock stock = Stock.builder()
                            .productVariant(variant)
                            .amount(stockDto.getAmount())
                            .restockedAt(stockDto.getRestockedAt())
                            .build();

                    stockRepository.save(stock);
                }
            }
        }

        return product;
    }

    // admin - 상품 수정
    @Transactional
    public void updateProduct(Long id, ProductUpdateRequest dto) {
        // 기존 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // 브랜드 조회
        Brand brand = null;
        if (dto.getBrandId() != null) {
            brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new BrandNotFoundException(dto.getBrandId()));
        }

        product.updateBasicInfo(dto, brand);

        // 연결된 하위 엔티티들 별도 갱신
        updateCategories(product, dto.getCategoryIds());
        updateImages(product, dto.getProductImgs());
        updateVariants(product, dto.getVariants());
    }

    // 카테고리 갱신
    private void updateCategories(Product product, List<Long> categoryIds) {
        if (categoryIds == null) return;

        // 기존 연결 삭제
        productCategoryRepository.deleteByProduct(product);

        // 새로 추가
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));

            ProductCategory pc = ProductCategory.builder()
                    .product(product)
                    .category(category)
                    .build();

            productCategoryRepository.save(pc);
        }
    }

    // 이미지 갱신
    private void updateImages(Product product, List<ProductImgDto> images) {
        if (images == null) return;

        // 기존 이미지 삭제
        productImgRepository.deleteByProduct(product);

        // 새로 저장
        for (ProductImgDto imgDto : images) {
            ProductImg img = ProductImg.builder()
                    .product(product)
                    .imageUrl(imgDto.getImageUrl())
                    .altText(imgDto.getAltText())
                    .sortIdx(imgDto.getSortIdx())
                    .isPrimary(imgDto.isPrimary())
                    .build();

            productImgRepository.save(img);
        }
    }

    // 옵션 + 재고 갱신
    private void updateVariants(Product product, List<ProductVariantDto> variants) {
        if (variants == null) return;

        // 기존 옵션들 + 연결된 재고 삭제
        List<ProductVariant> existingVariants = productVariantRepository.findByProduct(product);
        for (ProductVariant variant : existingVariants) {
            if (variant.getStock() != null) {
                stockRepository.delete(variant.getStock());
            }
        }
        productVariantRepository.deleteAll(existingVariants);

        // 새로 등록
        for (ProductVariantDto variantDto : variants) {
            ProductVariant variant = ProductVariant.builder()
                    .product(product)
                    .variantName(variantDto.getVariantName())
                    .listPrice(variantDto.getListPrice())
                    .salePrice(variantDto.getSalePrice())
                    .volume(variantDto.getVolume())
                    .upcCode(variantDto.getUpcCode())
                    .pillSize(variantDto.getPillSize())
                    .nutritionFacts(variantDto.getNutritionFacts())
                    .maxQtyPerOrder(variantDto.getMaxQtyPerOrder())
                    .restockEta(variantDto.getRestockEta())
                    .build();

            productVariantRepository.save(variant);

            StockDto stockDto = variantDto.getStock();
            if (stockDto != null) {
                Stock stock = Stock.builder()
                        .productVariant(variant)
                        .amount(stockDto.getAmount())
                        .restockedAt(stockDto.getRestockedAt())
                        .build();

                stockRepository.save(stock);
            }
        }
    }

    // admin - 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // 재입고 알림 삭제
        List<ProductVariant> variants = productVariantRepository.findByProduct(product);
        for (ProductVariant variant : variants) {
            restockSubscriptionRepository.deleteAllByProductVariant(variant);
        }

        // 재고 삭제
        for (ProductVariant variant : variants) {
            if (variant.getStock() != null) {
                stockRepository.delete(variant.getStock());
            }
        }

        // 옵션 삭제
        productVariantRepository.deleteAll(variants);

        // 이미지 삭제
        productImgRepository.deleteByProduct(product);

        // 카테고리 연결 삭제
        productCategoryRepository.deleteByProduct(product);

        // 상품 삭제
        productRepository.delete(product);
    }

}
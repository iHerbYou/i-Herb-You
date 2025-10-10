package com.iherbyou.catalog.dto;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductImg;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.Stock;
import com.iherbyou.community.entity.Review;
import lombok.*;

import java.util.Comparator;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductListDto {   // 목록에 노출되는 상품 정보 dto

    private Long id;

    private String name;

    private String brandName;

    private String thumbnailUrl;

    private Integer minPrice;

    private Double avgRating;

    private Integer reviewCount;

    private Long productVariantId;

    private Integer sales;

    private Boolean soldOut;

    public static ProductListDto fromEntity(Product product) {
        // 대표 이미지 (isPrimary = true)
        String thumbnail = product.getProductImgs().stream().filter(ProductImg::getIsPrimary).map(ProductImg::getImageUrl).findFirst().orElse(null);

        // 대표 옵션 선택: 1) 재고 있는 옵션 중 최저가  2) 없으면 전체 옵션 중 최저가
        ProductVariant cheapestInStock = product.getProductVariants().stream().filter(v -> v.getStock() != null && v.getStock().getAmount() > 0).filter(v -> v.getSalePrice() != null).min(Comparator.comparingInt(ProductVariant::getSalePrice)).orElse(null);

        ProductVariant chosenVariant = (cheapestInStock != null) ? cheapestInStock : product.getProductVariants().stream().filter(v -> v.getSalePrice() != null).min(Comparator.comparingInt(ProductVariant::getSalePrice)).orElse(null);

        Long representativeVariantId = (chosenVariant != null) ? chosenVariant.getId() : null;

        // 최저가(대표 옵션과 일관되게)
        int minPrice = (chosenVariant != null && chosenVariant.getSalePrice() != null) ? chosenVariant.getSalePrice() : product.getProductVariants().stream().map(ProductVariant::getSalePrice).filter(Objects::nonNull).min(Integer::compareTo).orElse(0);

        // 별점 평균 -> 임시 게산
        // 리뷰 작성 시 평균 평점 계산해 갱신 필요
        Double avgRating = product.getReviews().stream().mapToInt(Review::getRating).average().orElse(0);

        // 리뷰 개수
        int reviewCount = product.getReviews().size();

        // 품절 여부 (Stock.amount 합계 == 0)
        boolean soldOut = product.getProductVariants().stream().map(ProductVariant::getStock).filter(Objects::nonNull).mapToInt(Stock::getAmount).sum() <= 0;

        return ProductListDto.builder().id(product.getId()).name(product.getName()).brandName(product.getBrand().getName()).thumbnailUrl(thumbnail).minPrice(minPrice)  // 계산된 최저가 사용
                .avgRating(avgRating)  // 계산된 평점 사용
                .reviewCount(reviewCount)  // 계산된 리뷰수 사용
                .productVariantId(representativeVariantId).sales(product.getSales()).soldOut(soldOut).build();

    }

}

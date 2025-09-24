package com.iherbyou.catalog.dto;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductImg;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.Stock;
import com.iherbyou.community.entity.Review;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductListDto {

    private Long id;

    private String name;

    private String brandName;

    private String thumbnailUrl;

    private Integer minPrice;

    private Double avgRating;

    private Integer reviewCount;

    private Integer sales;

    private Boolean soldOut;

    public static ProductListDto fromEntity(Product product) {
        // 대표 이미지 (isPrimary = true)
        String thumbnail = product.getProductImgs().stream()
                .filter(ProductImg::getIsPrimary)
                .map(ProductImg::getImageUrl)
                .findFirst()
                .orElse(null);

        // 별점 평균 -> 임시 게산
        // 리뷰 작성 시 평균 평점 계산해 갱신 필요
        Double avgRating = product.getReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        // 리뷰 개수
        int reviewCount = product.getReviews().size();

        // 품절 여부 (Stock.amount 합계 == 0)
        boolean soldOut = product.getProductVariants().stream()
                .map(ProductVariant::getStock)
                .filter(Objects::nonNull)
                .mapToInt(Stock::getAmount)
                .sum() <= 0;

        return ProductListDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brandName(product.getBrand().getName())
                .thumbnailUrl(thumbnail)
                .minPrice(product.getMinPrice())
                .avgRating(product.getAvgRating())
                .reviewCount(product.getReviewCount())
                .sales(product.getSales())
                .soldOut(soldOut)
                .build();

    }

}

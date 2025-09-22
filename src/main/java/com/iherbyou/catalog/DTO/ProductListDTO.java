package com.iherbyou.catalog.DTO;

import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.ProductImg;
import com.iherbyou.community.Review;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductListDTO {


    private Long id;

    private String name;

    private String imageUrl;

    private Integer salePrice;

    private Double avgRating;

    private Integer reviewCount;

    private Integer sales;

    private Integer minPrice;

    public static ProductListDTO fromEntity(Product product) {
        // 대표 이미지 (첫번째 이미지 기준)
        String imageUrl = product.getProductImgs().stream()
                .findFirst()
                .map(ProductImg::getImageUrl)
                .orElse(null);

        // 별점 평균
        Double avgRating = product.getReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        // 리뷰 개수
        int reviewCount = product.getReviews().size();

        return ProductListDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(imageUrl)
                .salePrice(product.getMinPrice())
                .avgRating(avgRating)
                .reviewCount(reviewCount)
                .sales(product.getSales())
                .minPrice(product.getMinPrice())
                .build();

    }

}

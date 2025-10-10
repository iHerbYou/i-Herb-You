package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RecommendedProductDTO {
    private Long productId;
    private String productName;
    private String imageUrl;
    private Integer price;
}
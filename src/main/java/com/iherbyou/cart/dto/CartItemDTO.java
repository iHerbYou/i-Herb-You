package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartProductId;
    private Long productVariantId;
    private Long productId;
    private String productName;
    private String brandName;
    private String imageUrl;
    private Integer price;
    private Integer qty;
    private Boolean isSelected;
    private Integer stockQuantity;
    private Boolean isOutOfStock;
}
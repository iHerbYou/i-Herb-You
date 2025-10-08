package com.iherbyou.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CartResponseDTO {
    private Long cartId;
    private String guestToken;
    private List<CartItemDTO> items;
    private OrderSummaryDTO summary;
    private List<RecommendedProductDTO> recommendedProducts;
}
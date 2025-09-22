package com.iherbyou.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트에 상품 추가
public class AddWishlistItemRequest {
    @NotNull
    @Positive
    private Long productId;
}
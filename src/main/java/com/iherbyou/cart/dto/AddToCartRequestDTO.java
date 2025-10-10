package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequestDTO {
    private Long productVariantId;
    private Integer qty;
}
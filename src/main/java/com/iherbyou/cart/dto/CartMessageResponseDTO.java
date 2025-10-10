package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartMessageResponseDTO {
    private Long cartId;
    private String message;
    private String guestToken;
}
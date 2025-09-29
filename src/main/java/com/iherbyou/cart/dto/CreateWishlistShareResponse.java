package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateWishlistShareResponse {
    private String shareId;
    private String shortUrl;
    private String expiresAt;
}

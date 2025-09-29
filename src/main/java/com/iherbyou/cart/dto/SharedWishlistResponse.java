package com.iherbyou.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SharedWishlistResponse {
    private final String shareId;
    private final boolean readonly;
    private final String createdAt;  // ISO-8601 Z
    private final String expiresAt;  // ISO-8601 Z
    private final WishlistPageResponse page;
}

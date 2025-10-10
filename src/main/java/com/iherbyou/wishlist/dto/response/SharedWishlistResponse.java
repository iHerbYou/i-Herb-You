package com.iherbyou.wishlist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SharedWishlistResponse {
    private String shareId;
    private boolean readonly;
    private String createdAt;
    private String expiresAt;
    private WishlistResponse wishlist;
}
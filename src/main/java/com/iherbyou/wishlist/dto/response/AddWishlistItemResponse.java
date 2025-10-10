package com.iherbyou.wishlist.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddWishlistItemResponse {
    private boolean duplicated;
    private String message;
}
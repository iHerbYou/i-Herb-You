package com.iherbyou.wishlist.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWishlistShareResponse {
    private String shareId; // shareUrl의 뒷부분
    private String shareUrl;
    private String expiresAt;
}

package com.iherbyou.wishlist.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트 아이템
public class WishlistItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
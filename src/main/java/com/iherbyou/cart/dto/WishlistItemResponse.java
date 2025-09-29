package com.iherbyou.cart.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시 아이템 조회(목록, 단건 공통)
public class WishlistItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
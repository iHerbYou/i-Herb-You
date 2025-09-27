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
    private String productName;      // 스냅샷(옵션)
    private String thumbnailUrl;     // 스냅샷(옵션)
    private LocalDateTime createdAt; // 엔티티에 생성시간 있다면
}
package com.iherbyou.wishlist.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트 조회
public class WishlistResponse {
    private List<WishlistItemResponse> items;
    private int count;
}
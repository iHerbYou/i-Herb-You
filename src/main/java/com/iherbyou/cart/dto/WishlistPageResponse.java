package com.iherbyou.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트 조회
public class WishlistPageResponse {
    private List<WishlistItemResponse> items;
    private int count;        // 이번 응답 개수 (옵션)
}
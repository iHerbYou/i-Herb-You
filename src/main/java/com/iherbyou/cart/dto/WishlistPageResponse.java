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
    private int count;
}
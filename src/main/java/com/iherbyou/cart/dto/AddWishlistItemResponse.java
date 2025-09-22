package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트 추가 결과
public class AddWishlistItemResponse {
    private Long itemId;       // 새로 만들어졌으면 값, 중복이면 null 가능
    private boolean duplicated; // 중복 추가였다면 true
}
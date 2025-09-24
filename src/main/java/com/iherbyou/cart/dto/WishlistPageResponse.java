package com.iherbyou.cart.dto;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시리스트 페이징
public class WishlistPageResponse {
    private List<WishlistItemResponse> items;
    private Long nextCursor;  // 다음 페이지 조회용 (null이면 더 없음)
    private boolean hasNext;
    private int count;        // 이번 응답 개수 (옵션)
}
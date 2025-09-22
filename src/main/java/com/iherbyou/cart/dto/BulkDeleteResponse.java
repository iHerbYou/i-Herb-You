package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 위시 여러 개 삭제
public class BulkDeleteResponse {
    private int deletedCount;
}
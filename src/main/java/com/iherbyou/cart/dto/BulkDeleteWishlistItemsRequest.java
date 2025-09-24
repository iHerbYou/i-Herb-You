package com.iherbyou.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 여러 아이템 삭제
public class BulkDeleteWishlistItemsRequest {
    @NotEmpty
    private List<@Positive Long> itemIds;
}
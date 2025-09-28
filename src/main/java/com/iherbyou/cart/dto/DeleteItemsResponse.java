package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteItemsResponse {
    private int deletedCount;
    private String message;
}
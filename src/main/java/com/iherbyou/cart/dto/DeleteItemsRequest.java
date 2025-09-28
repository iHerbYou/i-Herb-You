package com.iherbyou.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 위시리스트 아이템 삭제 요청 DTO
 * - 단건/복수 삭제 공용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteItemsRequest {
    private Long userId;
    private List<Long> itemIds;
}
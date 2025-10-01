package com.iherbyou.wishlist.mapper;

import com.iherbyou.wishlist.dto.response.AddWishlistItemResponse;
import com.iherbyou.wishlist.dto.response.WishlistItemResponse;
import com.iherbyou.wishlist.dto.response.WishlistResponse;
import com.iherbyou.wishlist.entity.WishlistProduct;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 위시리스트 엔티티 ↔ DTO 변환 담당
 */
@Component
public class WishlistMapper {

    /**
     * WishlistProduct → WishlistItemResponse 변환
     */
    public WishlistItemResponse toItemResponse(WishlistProduct wishlistProduct) {
        return WishlistItemResponse.builder()
                .itemId(wishlistProduct.getId())
                .productId(wishlistProduct.getProduct().getId())
                .productName(wishlistProduct.getProduct().getName())
                .thumbnailUrl(getPrimaryImageUrl(wishlistProduct))
                .createdAt(wishlistProduct.getCreatedAt())
                .build();
    }

    /**
     * WishlistProduct 리스트 → WishlistResponse 변환
     */
    public WishlistResponse toResponse(List<WishlistProduct> items) {
        List<WishlistItemResponse> responses = items.stream()
                .map(this::toItemResponse)
                .toList();

        return WishlistResponse.builder()
                .items(responses)
                .count(responses.size())
                .build();
    }

    /**
     * 빈 위시리스트 응답 생성
     */
    public WishlistResponse toEmptyResponse() {
        return WishlistResponse.builder()
                .items(Collections.emptyList())
                .count(0)
                .build();
    }

    /**
     * 위시리스트 추가 성공 응답
     */
    public AddWishlistItemResponse toAddSuccessResponse() {
        return AddWishlistItemResponse.builder()
                .duplicated(false)
                .message("위시리스트에 담겼습니다.")
                .build();
    }

    /**
     * 위시리스트 중복 응답
     */
    public AddWishlistItemResponse toAddDuplicateResponse() {
        return AddWishlistItemResponse.builder()
                .duplicated(true)
                .message("이미 위시리스트에 있습니다.")
                .build();
    }

    /**
     * 상품의 대표 이미지 URL 추출
     * 1. isPrimary=true인 이미지 우선
     * 2. 없으면 sortIdx 가장 낮은 이미지
     * 3. 그것도 없으면 null
     */
    private String getPrimaryImageUrl(WishlistProduct wishlistProduct) {
        return wishlistProduct.getProduct().getProductImgs().stream()
                .filter(img -> img.getIsPrimary() != null && img.getIsPrimary())
                .findFirst()
                .map(img -> img.getImageUrl())
                .or(() -> wishlistProduct.getProduct().getProductImgs().stream()
                        .min(Comparator.comparing(img -> img.getSortIdx() != null ? img.getSortIdx() : Integer.MAX_VALUE))
                        .map(img -> img.getImageUrl()))
                .orElse(null);
    }
}
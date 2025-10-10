package com.iherbyou.community.dto;
// 리뷰 생성 DTO (사용자가 상품에 리뷰를 등록할 때 요청 바디)
public record ReviewCreateRequest(
        Long productId,
        Integer rating,
        String text,
        Boolean containsImage
) {}

package com.iherbyou.community.dto;
// 리뷰 응답 DTO (id, 평점, 본문, 작성자 이름, 작성일)
public record ReviewProduct(
        Long id,
        Integer rating,
        String text,
        String nickname,
        String createdAt
) {}

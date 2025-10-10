package com.iherbyou.community.dto;
// 리뷰 신고 응답 DTO (신고 ID, 리뷰 ID, 신고 사유, 상태값, 신고일)
public record ReviewReportProduct(
        Long id,
        Long reviewId,
        Long reasonCodeId,
        String statusKey,
        String createdAt
) {}

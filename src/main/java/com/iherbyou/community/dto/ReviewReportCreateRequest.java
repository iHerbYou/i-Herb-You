package com.iherbyou.community.dto;
// 리뷰 신고 요청 DTO (어떤 리뷰를, 어떤 사유로)
public record ReviewReportCreateRequest(
        Long reviewId,
        Long reasonCodeId
) {}

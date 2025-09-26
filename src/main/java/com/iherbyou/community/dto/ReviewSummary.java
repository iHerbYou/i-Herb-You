package com.iherbyou.community.dto;
// 리뷰 통계 응답 DTO (리뷰 총 개수, 평균 평점, 평점별 분포 배열)
public record ReviewSummary(
        long totalCount,
        double average,
        long[] counts   // index 0~4 == 1~5점
) {}

package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
import com.iherbyou.community.entity.ReviewReport;
import com.iherbyou.community.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/review-reports")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewReportService reportService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 리뷰 신고 접수
    // Header: X-USER-ID (신고자 사용자 ID)
    // Body: ReviewReportCreateRequest { reviewId, reasonCodeId }
    // Response: ReviewReportProduct (생성된 신고 요약)
    // POST인데 응답 이상함 -> 신고 핵심 필드만 응답
    @PostMapping
    public ResponseEntity<ReviewReportProduct> report(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody ReviewReportCreateRequest req
    ) {
        ReviewReport saved = reportService.createReport(userId, req.reviewId(), req.reasonCodeId());
        ReviewReportProduct res = new ReviewReportProduct(
                saved.getId(),
                saved.getReview().getId(),
                saved.getReasonCode().getId(),
                // saved.getStatusCode().getValue(),      // 팀 공통 Code 엔티티
                saved.getStatusCode().getId().toString(), // 임시: Code PK를 문자열로 반환
                saved.getCreatedAt() == null ? null : saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.ok(res);
    }

    // 내가 신고한 내역 (마이페이지)
    // Header: X-USER-ID
    // Response: Page<ReviewReportProduct>
    // 리뷰 피드백 해당 페이지 없음? → /my 명시
    @GetMapping("/my")
    public ResponseEntity<Page<ReviewReportProduct>> myReports(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        Page<ReviewReport> page = reportService.listByReporter(userId, pageable);
        Page<ReviewReportProduct> body = page.map(rr -> new ReviewReportProduct(
                rr.getId(),
                rr.getReview().getId(),
                rr.getReasonCode().getId(),
                // rr.getStatusCode().getValue(),        // Code 스펙 반영 전
                rr.getStatusCode().getId().toString(),   // 임시 대체
                rr.getCreatedAt() == null ? null : rr.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }
}

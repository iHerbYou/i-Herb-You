package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
import com.iherbyou.community.entity.ReviewReport;
import com.iherbyou.community.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;

// ⬇️ 추가
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.iherbyou.security.auth.UserPrincipal;

@RestController
@RequestMapping("/api/review-reports")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewReportService reportService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 신고 접수: 201 + Location (요청은 reasonCodeId 사용 중)
    @PostMapping
    public ResponseEntity<ReviewReportProduct> report(
            @AuthenticationPrincipal UserPrincipal me,   // ✅ 헤더 대신 인증 주입
            @RequestBody ReviewReportCreateRequest req
    ) {
        ReviewReport saved = reportService.createReport(me.getId(), req.reviewId(), req.reasonCodeId());

        ReviewReportProduct res = new ReviewReportProduct(
                saved.getId(),
                saved.getReview().getId(),
                saved.getReasonCode().getId(),
                saved.getStatusCode().getId().toString(),
                saved.getCreatedAt() == null ? null : saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.created(URI.create("/api/review-reports/" + saved.getId())).body(res);
    }

    // (공개) 리뷰별 신고 목록
    @GetMapping
    public ResponseEntity<Page<ReviewReport>> list(
            @RequestParam Long reviewId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.listByReview(reviewId, pageable));
    }
}

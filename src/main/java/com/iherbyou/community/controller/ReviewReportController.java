package com.iherbyou.community.controller;

import com.iherbyou.community.ReviewReport;
import com.iherbyou.community.service.ReviewReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review-reports")
public class ReviewReportController {

    private final ReviewReportService reportService;

    public ReviewReportController(ReviewReportService reportService) {
        this.reportService = reportService;
    }

    // 신고 접수
    @PostMapping
    public ReviewReport report(@RequestParam Long userId, @RequestParam Long reviewId,
                               @RequestParam Long reasonCodeId) {
        return reportService.createReport(userId, reviewId, reasonCodeId);
    }

    // 특정 리뷰 신고 목록 (관리자)
    @GetMapping("/admin")
    public Page<ReviewReport> adminListByReview(@RequestParam Long reviewId, Pageable pageable) {
        return reportService.listByReview(reviewId, pageable);
    }

    // 내가 한 신고 목록
    @GetMapping("/my")
    public Page<ReviewReport> myReports(@RequestParam Long userId, Pageable pageable) {
        return reportService.listByReporter(userId, pageable);
    }

    // 신고 상태 변경 (관리자)
    @PatchMapping("/{id}/status")
    public void changeStatus(@PathVariable Long id, @RequestParam Long adminId,
                             @RequestParam(defaultValue = "false") boolean isAdmin,
                             @RequestParam Long statusCodeId) {
        reportService.changeStatus(adminId, isAdmin, id, statusCodeId);
    }
}

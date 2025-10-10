package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.RefundRequestDto;
import com.iherbyou.ordering.dto.RefundResponseDto;
import com.iherbyou.ordering.service.RefundService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // 결제 건에 대한 환불 요청 등록 (환불 금액과 사유, 배송 옵션을 저장하고 후속 프로세스 시작)
    @PostMapping("/{paymentId}/refunds")
    public ResponseEntity<RefundResponseDto> requestRefund(@AuthenticationPrincipal UserPrincipal me,
                                                           @PathVariable Long paymentId,
                                                           @Valid @RequestBody RefundRequestDto request) {
        RefundResponseDto response = refundService.requestRefund(me.getId(), paymentId, request);
        return ResponseEntity.ok(response);
    }
}

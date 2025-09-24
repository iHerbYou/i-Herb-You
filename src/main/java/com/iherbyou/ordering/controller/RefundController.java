package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.RefundRequestDto;
import com.iherbyou.ordering.dto.RefundResponseDto;
import com.iherbyou.ordering.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{paymentId}/refunds")
    public ResponseEntity<RefundResponseDto> requestRefund(@PathVariable Long paymentId,
                                                           @Valid @RequestBody RefundRequestDto request) {
        RefundResponseDto response = refundService.requestRefund(paymentId, request);
        return ResponseEntity.ok(response);
    }
}

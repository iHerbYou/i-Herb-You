package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.dto.PaymentRequestDto;
import com.iherbyou.ordering.dto.PaymentResponseDto;
import com.iherbyou.ordering.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/payments")
    public ResponseEntity<PaymentResponseDto> requestPayment(@PathVariable Long orderId,
                                                             @RequestBody PaymentRequestDto request) {
        Payment payment = paymentService.requestPayment(orderId, request.getMethodCodeKey());
        return ResponseEntity.ok(toDto(payment));
    }

    @PostMapping("/payments/{paymentId}/complete")
    public ResponseEntity<PaymentResponseDto> completePayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.completePayment(paymentId);
        return ResponseEntity.ok(toDto(payment));
    }

    private PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentPrice(payment.getPaymentPrice())
                .paymentStatusKey(payment.getPaymentStatusCode() != null ? payment.getPaymentStatusCode().getValue() : null)
                .paymentMethodKey(payment.getPaymentMethodCode() != null ? payment.getPaymentMethodCode().getValue() : null)
                .requestedAt(payment.getRequestedAt())
                .paidAt(payment.getPaidAt())
                .build();
    }

}
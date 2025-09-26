package com.iherbyou.ordering.controller;

import com.iherbyou.ordering.dto.PaymentCancelRequestDto;
import com.iherbyou.ordering.dto.PaymentFailureRequestDto;
import com.iherbyou.ordering.dto.PaymentRequestDto;
import com.iherbyou.ordering.dto.PaymentResponseDto;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 결제 관련 API 컨트롤러 (PG와의 연동에서 발생하는 결제 요청/완료/취소/실패 이벤트를 Order 도메인에 연결)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 주문 상세 화면에서 결제 수단을 선택하고 결제를 시도할 때 호출되는 API (사용자 인증 정보를 받아 주문 소유 여부를 확인한 뒤 결제 엔터티를 READY 상태로 만듦)
    @PostMapping("/orders/{orderId}/payments")
    public ResponseEntity<PaymentResponseDto> requestPayment(@AuthenticationPrincipal(expression = "id") Long userId,
                                                             @PathVariable Long orderId,
                                                             @Valid @RequestBody PaymentRequestDto request) {
        Payment payment = paymentService.requestPayment(userId, orderId, request.getMethodCodeValue());
        return ResponseEntity.ok(toDto(payment));
    }

    // PG에서 결제가 성공적으로 완료되었을 때 호출되는 API (결제 상태를 PAID 로 바꾸고 주문 상태는 OrderService 를 통해 PAID 단계로 전환)
    @PostMapping("/payments/{paymentId}/complete")
    public ResponseEntity<PaymentResponseDto> completePayment(@AuthenticationPrincipal(expression = "id") Long userId,
                                                              @PathVariable Long paymentId) {
        Payment payment = paymentService.completePayment(userId, paymentId);
        return ResponseEntity.ok(toDto(payment));
    }

    // PG 취소 콜백 혹은 사용자가 결제를 취소했을 때 호출 (결제 상태를 CANCELED 로 만들고 주문 상태를 CANCELED 단계로 동기화)
    @PostMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(@AuthenticationPrincipal(expression = "id") Long userId,
                                                            @PathVariable Long paymentId,
                                                            @RequestBody(required = false) PaymentCancelRequestDto request) {
        String actor = resolveActor(userId, request);
        Payment payment = paymentService.cancelPayment(paymentId, actor);
        return ResponseEntity.ok(toDto(payment));
    }

    // PG 결제 실패 콜백을 처리하는 API (결제 상태를 FAILED 로 갱신하고 주문 상태도 FAILED 로 전환)
    @PostMapping("/payments/{paymentId}/fail")
    public ResponseEntity<PaymentResponseDto> markPaymentFailed(@PathVariable Long paymentId,
                                                                @Valid @RequestBody PaymentFailureRequestDto request) {
        Payment payment = paymentService.markPaymentFailed(paymentId, request.getReason());
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

    private String resolveActor(Long userId, PaymentCancelRequestDto request) {
        if (request != null && StringUtils.hasText(request.getActor())) {
            return request.getActor().trim();
        }
        if (userId != null) {
            return "user:" + userId;
        }
        return "system";
    }

}

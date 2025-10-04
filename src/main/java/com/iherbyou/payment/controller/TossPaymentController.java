package com.iherbyou.payment.controller;

import com.iherbyou.ordering.service.PaymentService;
import com.iherbyou.payment.dto.ConfirmRequest;
import com.iherbyou.payment.dto.ConfirmResponse;
import com.iherbyou.payment.service.TossPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payments")
public class TossPaymentController {

    private final TossPaymentService tossPaymentService;
    private final PaymentService paymentService;

    public TossPaymentController(TossPaymentService tossPaymentService, PaymentService paymentService) {
        this.tossPaymentService = tossPaymentService;
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmResponse> confirmPayment(@Valid @RequestBody ConfirmRequest request) {
        try {
            ConfirmResponse response = tossPaymentService.confirmPayment(request);
            paymentService.completeExternalPayment(
                    response.getOrderId(),
                    response.getAmount(),
                    response.getPaymentKey() != null ? "toss:" + response.getPaymentKey() : "toss"
            );
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            if (isAlreadyProcessed(ex)) {
                paymentService.completeExternalPayment(
                        request.getOrderId(),
                        request.getAmount(),
                        request.getPaymentKey() != null ? "toss:" + request.getPaymentKey() : "toss"
                );

                ConfirmResponse response = new ConfirmResponse();
                response.setStatus("ALREADY_PROCESSED");
                response.setOrderId(request.getOrderId());
                response.setPaymentKey(request.getPaymentKey());
                response.setAmount(request.getAmount());
                return ResponseEntity.ok(response);
            }
            throw ex;
        }
    }

    private boolean isAlreadyProcessed(ResponseStatusException ex) {
        if (!HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
            return false;
        }
        String reason = ex.getReason();
        if (!StringUtils.hasText(reason)) {
            return false;
        }
        String upper = reason.toUpperCase();
        return upper.contains("ALREADY_PROCESSED_PAYMENT_KEY")
                || upper.contains("ALREADY_PROCESSED_PAYMENT");
    }
}

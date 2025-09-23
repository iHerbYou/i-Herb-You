package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.common.CodeFinder;
import com.iherbyou.ordering.dto.RefundRequestDto;
import com.iherbyou.ordering.dto.RefundResponseDto;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.entity.Refund;
import com.iherbyou.ordering.repository.PaymentRepository;
import com.iherbyou.ordering.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CodeFinder codeFinder;

    public RefundResponseDto requestRefund(Long paymentId, RefundRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("refund request is required");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        BigDecimal amount = request.getAmount();
        validateAmount(amount, payment.getPaymentPrice());

        Code reason = codeFinder.get("REFUND_REASON", request.getReasonCodeKey());
        Code deliveryOption = codeFinder.get("REFUND_DELIVERY_OPTION", request.getDeliveryOptionCodeKey());
        Code requestedStatus = codeFinder.get("REFUND_STATUS", "REQUESTED");

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .reasonCode(reason)
                .statusCode(requestedStatus)
                .deliveryOptionCode(deliveryOption)
                .requestedAt(LocalDateTime.now())
                .build();

        Refund saved = refundRepository.save(refund);
        payment.getRefunds().add(saved);

        return RefundResponseDto.from(saved);
    }

    private void validateAmount(BigDecimal amount, BigDecimal paymentPrice) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("refund amount must be positive");
        }
        if (paymentPrice == null) {
            throw new IllegalStateException("payment amount is not defined");
        }
        if (amount.compareTo(paymentPrice) > 0) {
            throw new IllegalArgumentException("refund amount exceeds payment amount");
        }
    }
}

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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundService {

    private static final String PAYMENT_STATUS_PAID = "PAID";

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CodeFinder codeFinder;

    public RefundResponseDto requestRefund(Long paymentId, RefundRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("refund request is required");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        ensurePaymentIsRefundable(payment);

        BigDecimal amount = request.getAmount();
        BigDecimal alreadyRefunded = refundRepository.findByPayment_Id(paymentId).stream()
                .map(Refund::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validateAmount(amount, payment.getPaymentPrice(), alreadyRefunded);

        Code reason = codeFinder.get("REFUND_REASON", request.getReasonCodeKey());
        Code deliveryOption = codeFinder.get("REFUND_DELIVERY_OPTION", request.getDeliveryOptionCodeKey());
        Code requestedStatus = codeFinder.get("REFUND_STATUS", "REQUESTED");

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .reasonCode(reason)
                .statusCode(requestedStatus)
                .deliveryOptionCode(deliveryOption)
                .build();

        Refund saved = refundRepository.save(refund);
        payment.addRefund(saved);

        return RefundResponseDto.from(saved);
    }

    private void validateAmount(BigDecimal amount, BigDecimal paymentPrice, BigDecimal alreadyRefunded) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("refund amount must be positive");
        }
        if (paymentPrice == null) {
            throw new IllegalStateException("payment amount is not defined");
        }
        BigDecimal totalAfterRefund = amount.add(alreadyRefunded);
        if (totalAfterRefund.compareTo(paymentPrice) > 0) {
            throw new IllegalArgumentException("refund amount exceeds payment amount");
        }
    }

    private void ensurePaymentIsRefundable(Payment payment) {
        if (payment.getPaymentStatusCode() == null
                || !PAYMENT_STATUS_PAID.equals(payment.getPaymentStatusCode().getName())) {
            throw new IllegalStateException("payment is not settled");
        }
    }
}

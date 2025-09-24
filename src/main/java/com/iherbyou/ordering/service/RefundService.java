package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
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

    private static final int PAYMENT_STATUS_PAID = 403; // 40=PAYMENT_STATUS, 403=PAID

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CodeService codeService;

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

        Code reason = requireCode(61, request.getReasonCodeValue(), "REFUND_REASON:" + request.getReasonCodeValue()); // 61=REFUND_REASON
        Code deliveryOption = requireCode(62, request.getDeliveryOptionCodeValue(), "REFUND_DELIVERY_OPTION:" + request.getDeliveryOptionCodeValue()); // 62=REFUND_DELIVERY_OPTION
        Code requestedStatus = requireCode(60, 601, "REFUND_STATUS:REQUESTED"); // 60=REFUND_STATUS, 601=REQUESTED

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
                || PAYMENT_STATUS_PAID != payment.getPaymentStatusCode().getValue()) {
            throw new IllegalStateException("payment is not settled");
        }
    }

    private Code requireCode(int groupValue, int codeValue, String context) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("code not found: " + context);
        }
        return code;
    }
}
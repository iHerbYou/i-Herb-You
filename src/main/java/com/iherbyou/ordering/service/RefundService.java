package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.RefundRequestDto;
import com.iherbyou.ordering.dto.RefundResponseDto;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.entity.Refund;
import com.iherbyou.ordering.repository.PaymentRepository;
import com.iherbyou.ordering.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefundService {

    private static final int PAYMENT_STATUS_PAID = 403; // 40=PAYMENT_STATUS, 403=PAID
    private static final int REFUND_STATUS_REQUESTED = 601;
    private static final int REFUND_STATUS_COMPLETED = 605;
    private static final int REFUND_STATUS_FAILED = 606;

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CodeService codeService;
    private final OrderService orderService;

    public RefundResponseDto requestRefund(Long userId, Long paymentId, RefundRequestDto request) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required for refund request");
        }
        if (request == null) {
            throw new IllegalArgumentException("refund request is required");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        ensureOrderOwner(payment, userId);

        ensurePaymentIsRefundable(payment);

        BigDecimal amount = request.getAmount();
        BigDecimal alreadyRefunded = refundRepository.findByPayment_Id(paymentId).stream()
                .map(Refund::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validateAmount(amount, payment.getPaymentPrice(), alreadyRefunded);

        Code reason = requireCode(61, request.getReasonCodeValue(), "REFUND_REASON:" + request.getReasonCodeValue()); // 61=REFUND_REASON
        Code deliveryOption = requireCode(62, request.getDeliveryOptionCodeValue(), "REFUND_DELIVERY_OPTION:" + request.getDeliveryOptionCodeValue()); // 62=REFUND_DELIVERY_OPTION
        Code requestedStatus = requireRefundStatus(REFUND_STATUS_REQUESTED, "REFUND_STATUS:REQUESTED");

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .reasonCode(reason)
                .statusCode(requestedStatus)
                .deliveryOptionCode(deliveryOption)
                .build();

        Refund saved = refundRepository.save(refund);
        payment.addRefund(saved);

        Long orderId = payment.getOrder() != null ? payment.getOrder().getId() : null;
        if (orderId == null) {
            throw new IllegalStateException("order id is required for refund status transition");
        }

        String actor = "user:" + userId;
        String correlationId = "REFUND_REQUEST:" + (saved.getId() != null ? saved.getId() : paymentId);
        orderService.updateStatus(orderId, OrderStatus.REFUND_REQUESTED, "REFUND_REQUESTED", actor, correlationId);
        log.info("[RefundRequested] paymentId={} refundId={} userId={} amount={}", paymentId, saved.getId(), userId, amount);

        return RefundResponseDto.from(saved);
    }

    public RefundResponseDto completeRefund(Long refundId, boolean success, String actor, LocalDateTime completedAt) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("refund not found"));

        if (refund.getPayment() == null || refund.getPayment().getOrder() == null) {
            throw new IllegalStateException("refund is not attached to an order");
        }

        if (success) {
            if (isStatus(refund, REFUND_STATUS_COMPLETED)) {
                return RefundResponseDto.from(refund);
            }

            Code completedStatus = requireRefundStatus(REFUND_STATUS_COMPLETED, "REFUND_STATUS:COMPLETED");
            LocalDateTime appliedCompletedAt = completedAt != null ? completedAt : LocalDateTime.now();
            refund.markCompleted(completedStatus, appliedCompletedAt);
            Refund saved = refundRepository.save(refund);

            BigDecimal totalCompleted = calculateCompletedAmount(refund.getPayment().getId());
            BigDecimal paymentPrice = refund.getPayment().getPaymentPrice();
            if (paymentPrice == null) {
                throw new IllegalStateException("payment amount is missing for refund completion");
            }

            OrderStatus nextStatus = totalCompleted.compareTo(paymentPrice) >= 0
                    ? OrderStatus.REFUNDED
                    : OrderStatus.PARTIALLY_REFUNDED;

            String resolvedActor = (actor != null && !actor.isBlank()) ? actor : "system";
            String correlationId = "REFUND_COMPLETED:" + refundId;
            orderService.updateStatus(refund.getPayment().getOrder().getId(), nextStatus, "REFUND_COMPLETED", resolvedActor, correlationId);
            log.info("[RefundCompleted] refundId={} paymentId={} orderId={} status={} actor={}",
                    refundId, refund.getPayment().getId(), refund.getPayment().getOrder().getId(), nextStatus, resolvedActor);

            return RefundResponseDto.from(saved);
        }

        if (isStatus(refund, REFUND_STATUS_FAILED)) {
            return RefundResponseDto.from(refund);
        }

        Code failedStatus = requireRefundStatus(REFUND_STATUS_FAILED, "REFUND_STATUS:FAILED");
        refund.markStatus(failedStatus);
        Refund saved = refundRepository.save(refund);
        log.info("[RefundFailed] refundId={} paymentId={} actor={}", refundId, refund.getPayment().getId(), actor);
        return RefundResponseDto.from(saved);
    }

    private void ensureOrderOwner(Payment payment, Long userId) {
        if (payment.getOrder() == null
                || payment.getOrder().getUser() == null
                || !payment.getOrder().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied for payment " + payment.getId());
        }
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

    private Code requireRefundStatus(int codeValue, String context) {
        return requireCode(60, codeValue, context);
    }

    private boolean isStatus(Refund refund, int codeValue) {
        return refund.getStatusCode() != null && refund.getStatusCode().getValue() == codeValue;
    }

    private BigDecimal calculateCompletedAmount(Long paymentId) {
        List<Refund> refunds = refundRepository.findByPayment_Id(paymentId);
        return refunds.stream()
                .filter(r -> r.getStatusCode() != null && r.getStatusCode().getValue() == REFUND_STATUS_COMPLETED)
                .map(Refund::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

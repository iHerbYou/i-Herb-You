package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private static final int PAYMENT_STATUS_PAID = 403; // 40=PAYMENT_STATUS, 403=PAID
    private static final int PAYMENT_STATUS_READY = 401;
    private static final int PAYMENT_STATUS_FAILED = 406;
    private static final int PAYMENT_STATUS_CANCELED = 405;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CodeService codeService;
    private final OrderService orderService;

    public Payment requestPayment(Long userId, Long orderId, Integer methodCodeValue) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required for payment request");
        }
        if (methodCodeValue == null) {
            throw new IllegalArgumentException("methodCodeValue is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        ensureOrderOwner(order, userId);
        ensureOrderAllowsPayment(order);

        Payment existing = paymentRepository.findByOrder_Id(orderId).orElse(null);
        if (existing != null
                && existing.getPaymentStatusCode() != null
                && PAYMENT_STATUS_PAID == existing.getPaymentStatusCode().getValue()) {
            throw new IllegalStateException("payment already completed");
        }

        Code method = requireCode(41, methodCodeValue, "PAYMENT_METHOD:" + methodCodeValue); // 41=PAYMENT_METHOD
        Code requestedStatus = requireCode(40, PAYMENT_STATUS_READY, "PAYMENT_STATUS:READY");

        BigDecimal amount = BigDecimal.valueOf(order.getTotalPrice());
        LocalDateTime now = LocalDateTime.now();

        Payment payment = (existing != null)
                ? existing
                : Payment.builder().order(order).build();

        payment.markRequested(requestedStatus, method, amount, now);
        log.info("[PaymentRequested] paymentId={} orderId={} method={} amount={} userId={} at={}",
                payment.getId(), orderId, methodCodeValue, amount, userId, now);
        return paymentRepository.save(payment);
    }

    public Payment completePayment(Long userId, Long paymentId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required to complete payment");
        }
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        ensureOrderOwner(payment.getOrder(), userId);

        if (payment.getPaymentStatusCode() != null
                && PAYMENT_STATUS_PAID == payment.getPaymentStatusCode().getValue()) {
            return payment;
        }

        Code paidStatus = requireCode(40, PAYMENT_STATUS_PAID, "PAYMENT_STATUS:PAID");
        payment.markPaid(paidStatus, LocalDateTime.now());

        // 멱등 키: 동일 결제 성공 콜백 중복 처리를 막기 위해 결제 ID 기반으로 구성
        String correlationId = "PAYMENT:" + paymentId + ":PAID";
        orderService.updateStatus(
                payment.getOrder().getId(),
                OrderStatus.PAID,
                "PAYMENT_PAID",
                "user:" + userId,
                correlationId
        );
        log.info("[PaymentCompleted] paymentId={} orderId={} userId={}", paymentId, payment.getOrder().getId(), userId);

        return payment;
    }

    public Payment markPaymentFailed(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        if (payment.getPaymentStatusCode() != null
                && PAYMENT_STATUS_FAILED == payment.getPaymentStatusCode().getValue()) {
            return payment;
        }

        Code failedStatus = requireCode(40, PAYMENT_STATUS_FAILED, "PAYMENT_STATUS:FAILED");
        payment.setPaymentStatusCode(failedStatus);
        payment.setPaidAt(null);

        // 멱등 키: 결제 실패 콜백 재전송 대비
        String correlationId = "PAYMENT:" + paymentId + ":FAILED";
        orderService.updateStatus(
                payment.getOrder().getId(),
                OrderStatus.FAILED,
                "PAYMENT_FAILED",
                "system",
                correlationId
        );

        log.info("[PaymentFailed] paymentId={} orderId={} reason={}", paymentId, payment.getOrder().getId(), reason);
        return payment;
    }

    public Payment cancelPayment(Long paymentId, String actor) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        if (payment.getPaymentStatusCode() != null
                && PAYMENT_STATUS_CANCELED == payment.getPaymentStatusCode().getValue()) {
            return payment;
        }

        Code canceledStatus = requireCode(40, PAYMENT_STATUS_CANCELED, "PAYMENT_STATUS:CANCELED");
        payment.setPaymentStatusCode(canceledStatus);
        payment.setPaidAt(null);

        // 멱등 키: 동일한 결제 취소 콜백이 여러 번 도착할 수 있어 고정 키 사용
        String correlationId = "PAYMENT:" + paymentId + ":CANCELED";
        orderService.updateStatus(
                payment.getOrder().getId(),
                OrderStatus.CANCELED,
                "PAYMENT_CANCELED",
                Optional.ofNullable(actor).orElse("system"),
                correlationId
        );

        log.info("[PaymentCanceled] paymentId={} orderId={} actor={}", paymentId, payment.getOrder().getId(), actor);
        return payment;
    }

    private void ensureOrderOwner(Order order, Long userId) {
        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied for order " + order.getId());
        }
    }

    private void ensureOrderAllowsPayment(Order order) {
        if (order.getOrderStatusCode() == null) {
            throw new IllegalStateException("order status is not set");
        }
        OrderStatus status = OrderStatus.fromCodeValue(order.getOrderStatusCode().getValue());
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("order status does not allow payment: " + status);
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

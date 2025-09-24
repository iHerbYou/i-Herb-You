package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private static final int PAYMENT_STATUS_PAID = 403; // 40=PAYMENT_STATUS, 403=PAID

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CodeService codeService;

    public Payment requestPayment(Long orderId, Integer methodCodeValue) {
        if (methodCodeValue == null) {
            throw new IllegalArgumentException("methodCodeValue is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        Payment existing = paymentRepository.findByOrder_Id(orderId).orElse(null);
        if (existing != null
                && existing.getPaymentStatusCode() != null
                && PAYMENT_STATUS_PAID == existing.getPaymentStatusCode().getValue()) {
            throw new IllegalStateException("payment already completed");
        }

        Code method = requireCode(41, methodCodeValue, "PAYMENT_METHOD:" + methodCodeValue); // 41=PAYMENT_METHOD
        Code requestedStatus = requireCode(40, 401, "PAYMENT_STATUS:READY"); // 40=PAYMENT_STATUS, 401=READY

        BigDecimal amount = BigDecimal.valueOf(order.getTotalPrice());
        LocalDateTime now = LocalDateTime.now();

        Payment payment = (existing != null)
                ? existing
                : Payment.builder().order(order).build();

        payment.markRequested(requestedStatus, method, amount, now);
        return paymentRepository.save(payment);
    }

    public Payment completePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("payment not found"));

        if (payment.getPaymentStatusCode() != null
                && PAYMENT_STATUS_PAID == payment.getPaymentStatusCode().getValue()) {
            return payment;
        }

        Code paidStatus = requireCode(40, PAYMENT_STATUS_PAID, "PAYMENT_STATUS:PAID");
        payment.markPaid(paidStatus, LocalDateTime.now());

        Code orderPaidStatus = requireCode(30, 302, "ORDER_STATUS:PAID"); // 30=ORDER_STATUS, 302=PAID
        payment.getOrder().setOrderStatusCode(orderPaidStatus);

        return payment;
    }

    private Code requireCode(int groupValue, int codeValue, String context) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("code not found: " + context);
        }
        return code;
    }

}
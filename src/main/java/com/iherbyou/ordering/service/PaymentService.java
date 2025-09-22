package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.Payment;
import com.iherbyou.ordering.common.CodeFinder;
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

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CodeFinder codeFinder;

    public Payment requestPayment(Long orderId, String methodCodeKey) {
        if (methodCodeKey == null || methodCodeKey.isBlank()) {
            throw new IllegalArgumentException("methodCodeKey is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        Payment existing = paymentRepository.findByOrder_Id(orderId).orElse(null);
        if (existing != null && "PAID".equals(existing.getPaymentStatusCode().getValue())) {
            throw new IllegalStateException("payment already completed");
        }

        Code method = codeFinder.get("PAYMENT_METHOD", methodCodeKey);
        Code requestedStatus = codeFinder.get("PAYMENT_STATUS", "PENDING");

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

        if ("PAID".equals(payment.getPaymentStatusCode().getValue())) {
            return payment;
        }

        Code paidStatus = codeFinder.get("PAYMENT_STATUS", "PAID");
        payment.markPaid(paidStatus, LocalDateTime.now());

        Code orderPaidStatus = codeFinder.get("ORDER_STATUS", "PAID");
        payment.getOrder().setOrderStatusCode(orderPaidStatus);

        return payment;
    }

}

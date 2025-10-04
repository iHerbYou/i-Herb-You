package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.entity.AddressSnapshot;
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
import java.util.Set;

import com.iherbyou.user.entity.UserAddress;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private static final int PAYMENT_STATUS_PAID = 403; // 40=PAYMENT_STATUS, 403=PAID
    private static final int PAYMENT_STATUS_READY = 401;
    private static final int PAYMENT_STATUS_FAILED = 406;
    private static final int PAYMENT_STATUS_CANCELED = 405;

    private static final int PAYMENT_METHOD_TOSS = 417; // PG 연동 결제 (토스)
    private static final Set<Integer> AUTO_COMPLETE_METHODS = Set.of(411, 412, 413, 414, 415, 416, 418);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CodeService codeService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

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

        ensureShippingSnapshot(order);

        Payment payment = (existing != null)
                ? existing
                : Payment.builder().order(order).build();

        if (requiresExternalOrderKey(methodCodeValue)) {
            if (payment.getExternalOrderKey() == null) {
                payment.assignExternalOrderKey(generateExternalOrderKey(orderId));
            }
        } else {
            payment.assignExternalOrderKey(null);
        }

        payment.markRequested(requestedStatus, method, amount, now);
        Payment saved = paymentRepository.save(payment);
        log.info("[PaymentRequested] paymentId={} orderId={} method={} amount={} userId={} at={}",
                saved.getId(), orderId, methodCodeValue, amount, userId, now);

        if (shouldAutoComplete(methodCodeValue)) {
            String actor = "user:" + userId;
            return settlePayment(saved, actor);
        }

        return saved;
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

        String actor = "user:" + userId;
        Payment settled = settlePayment(payment, actor);
        log.info("[PaymentCompleted] paymentId={} orderId={} userId={}", paymentId, payment.getOrder().getId(), userId);

        return settled;
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

    public Payment completeExternalPayment(String externalOrderKey, Long amount, String actor) {
        if (!StringUtils.hasText(externalOrderKey)) {
            throw new IllegalArgumentException("externalOrderKey is required to complete payment");
        }

        Payment payment = paymentRepository.findByExternalOrderKey(externalOrderKey.trim())
                .orElseThrow(() -> new IllegalArgumentException("payment not found for externalOrderKey"));

        if (amount != null && payment.getPaymentPrice() != null) {
            BigDecimal requested = BigDecimal.valueOf(amount);
            if (payment.getPaymentPrice().compareTo(requested) != 0) {
                throw new IllegalStateException("payment amount mismatch for externalOrderKey");
            }
        }

        String resolvedActor = (actor == null || actor.isBlank()) ? "system" : actor;
        Payment settled = settlePayment(payment, resolvedActor);
        log.info("[PaymentExternalCompleted] externalOrderKey={} paymentId={} actor={}", externalOrderKey, settled.getId(), resolvedActor);
        return settled;
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

    private boolean shouldAutoComplete(Integer methodCodeValue) {
        if (methodCodeValue == null) {
            return false;
        }
        return methodCodeValue != null && AUTO_COMPLETE_METHODS.contains(methodCodeValue);
    }

    private boolean requiresExternalOrderKey(Integer methodCodeValue) {
        return methodCodeValue != null && methodCodeValue == PAYMENT_METHOD_TOSS;
    }

    private String generateExternalOrderKey(Long orderId) {
        String prefix = "ORDER-" + orderId + "-";
        String random = java.util.UUID.randomUUID().toString().replace("-", "");
        int maxRandomLength = Math.max(6, 64 - prefix.length());
        if (maxRandomLength <= 0) {
            throw new IllegalStateException("external order key prefix exceeded length limit");
        }
        if (random.length() > maxRandomLength) {
            random = random.substring(0, maxRandomLength);
        }
        if (random.length() < 6) {
            random = String.format("%-6s", random).replace(' ', '0');
        }
        return prefix + random;
    }

    private void ensureShippingSnapshot(Order order) {
        if (order.getShippingAddress() != null) {
            return;
        }
        if (order.getUser() == null || order.getUser().getAddresses() == null || order.getUser().getAddresses().isEmpty()) {
            throw new IllegalStateException("shipping address is required for payment");
        }
        UserAddress address = order.getUser().getAddresses().stream()
                .filter(UserAddress::isDefault)
                .findFirst()
                .orElse(order.getUser().getAddresses().get(0));
        AddressSnapshot snapshot = AddressSnapshot.from(address);
        order.updateShippingAddress(snapshot);
    }

    private Payment settlePayment(Payment payment, String actor) {
        if (payment == null) {
            throw new IllegalArgumentException("payment is required to settle");
        }
        if (payment.getOrder() == null) {
            throw new IllegalStateException("payment must be attached to an order");
        }

        if (payment.getPaymentStatusCode() != null
                && PAYMENT_STATUS_PAID == payment.getPaymentStatusCode().getValue()) {
            return payment;
        }

        Code paidStatus = requireCode(40, PAYMENT_STATUS_PAID, "PAYMENT_STATUS:PAID");
        payment.markPaid(paidStatus, LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);

        String resolvedActor = (actor == null || actor.isBlank()) ? "system" : actor;
        Long orderId = payment.getOrder().getId();

        String correlationId = "PAYMENT:" + saved.getId() + ":PAID";
        orderService.updateStatus(
                orderId,
                OrderStatus.PAID,
                "PAYMENT_PAID",
                resolvedActor,
                correlationId
        );

        deliveryService.prepareAutomaticDelivery(orderId, resolvedActor);
        log.info("[PaymentSettled] paymentId={} orderId={} actor={}", saved.getId(), orderId, resolvedActor);

        return saved;
    }

}

package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.dto.admin.*;
import com.iherbyou.ordering.entity.*;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.OrderStatusHistoryRepository;
import com.iherbyou.ordering.repository.PaymentRepository;
import com.iherbyou.ordering.repository.RefundRepository;
import com.iherbyou.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderAdminService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final RefundRepository refundRepository;

    public Page<AdminOrderSummaryDto> searchOrders(AdminOrderSearchCondition condition, Pageable pageable) {
        Page<Order> orders = orderRepository.searchAdminOrders(condition, pageable);
        Map<Long, Payment> paymentMap = loadPayments(orders.getContent());
        return orders.map(order -> toSummary(order, paymentMap.get(order.getId())));
    }

    public AdminOrderDetailDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findAdminOrderDetail(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        Payment payment = order.getPayment();
        List<OrderStatusHistory> histories = historyRepository.findByOrder_IdOrderByChangedAtAsc(orderId);
        List<Refund> refunds = (payment != null) ? refundRepository.findByPayment_Id(payment.getId()) : List.of();

        return toDetail(order, payment, histories, refunds);
    }

    private Map<Long, Payment> loadPayments(List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return Map.of();
        }
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        return paymentRepository.findByOrder_IdIn(orderIds).stream()
                .collect(Collectors.toMap(p -> p.getOrder().getId(), p -> p));
    }

    private AdminOrderSummaryDto toSummary(Order order, Payment payment) {
        Code orderStatus = order.getOrderStatusCode();
        User user = order.getUser();
        Delivery delivery = order.getDelivery();

        return AdminOrderSummaryDto.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatusValue(orderStatus != null ? orderStatus.getValue() : null)
                .orderStatusName(orderStatus != null ? orderStatus.getDisplayName() : null)
                .totalPrice(order.getTotalPrice())
                .userId(user != null ? user.getId() : null)
                .userEmail(user != null ? user.getEmail() : null)
                .userName(user != null ? user.getName() : null)
                .paymentStatusValue(payment != null && payment.getPaymentStatusCode() != null ? payment.getPaymentStatusCode().getValue() : null)
                .paymentStatusName(payment != null && payment.getPaymentStatusCode() != null ? payment.getPaymentStatusCode().getDisplayName() : null)
                .paymentMethodValue(payment != null && payment.getPaymentMethodCode() != null ? payment.getPaymentMethodCode().getValue() : null)
                .paymentMethodName(payment != null && payment.getPaymentMethodCode() != null ? payment.getPaymentMethodCode().getDisplayName() : null)
                .deliveryStatusValue(delivery != null && delivery.getCode() != null ? delivery.getCode().getValue() : null)
                .deliveryStatusName(delivery != null && delivery.getCode() != null ? delivery.getCode().getDisplayName() : null)
                .build();
    }

    private AdminOrderDetailDto toDetail(Order order,
                                         Payment payment,
                                         List<OrderStatusHistory> histories,
                                         List<Refund> refunds) {
        Code orderStatus = order.getOrderStatusCode();
        User user = order.getUser();
        Delivery delivery = order.getDelivery();
        AddressSnapshot shipping = order.getShippingAddress();

        return AdminOrderDetailDto.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatusValue(orderStatus != null ? orderStatus.getValue() : null)
                .orderStatusName(orderStatus != null ? orderStatus.getDisplayName() : null)
                .subtotal(order.getSubtotal())
                .deliveryFee(order.getDeliveryFee())
                .discount(order.getDiscount())
                .totalPrice(order.getTotalPrice())
                .customsInfo(order.getCustomsInfo())
                .userId(user != null ? user.getId() : null)
                .userEmail(user != null ? user.getEmail() : null)
                .userName(user != null ? user.getName() : null)
                .shippingAddress(toShippingAddress(shipping))
                .delivery(toDelivery(delivery))
                .payment(toPayment(payment))
                .items(toItems(order.getOrderProducts()))
                .refunds(toRefunds(refunds))
                .histories(toHistories(histories))
                .build();
    }

    private AdminShippingAddressDto toShippingAddress(AddressSnapshot snapshot) {
        if (snapshot == null) {
            return null;
        }
        return AdminShippingAddressDto.builder()
                .recipient(snapshot.getRecipient())
                .phone(snapshot.getPhone())
                .zipcode(snapshot.getZipcode())
                .addressLine1(snapshot.getAddressLine1())
                .addressLine2(snapshot.getAddressLine2())
                .build();
    }

    private AdminDeliveryInfoDto toDelivery(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        Code status = delivery.getCode();
        return AdminDeliveryInfoDto.builder()
                .deliveryId(delivery.getId())
                .statusValue(status != null ? status.getValue() : null)
                .statusName(status != null ? status.getDisplayName() : null)
                .company(delivery.getDeliveryCompany())
                .trackingNumber(delivery.getTrackingNumber())
                .memo(delivery.getDelMemo())
                .startedAt(delivery.getDelStartAt())
                .completedAt(delivery.getDelCompleteAt())
                .build();
    }

    private AdminPaymentInfoDto toPayment(Payment payment) {
        if (payment == null) {
            return null;
        }
        Code status = payment.getPaymentStatusCode();
        Code method = payment.getPaymentMethodCode();
        return AdminPaymentInfoDto.builder()
                .paymentId(payment.getId())
                .amount(payment.getPaymentPrice())
                .statusValue(status != null ? status.getValue() : null)
                .statusName(status != null ? status.getDisplayName() : null)
                .methodValue(method != null ? method.getValue() : null)
                .methodName(method != null ? method.getDisplayName() : null)
                .requestedAt(payment.getRequestedAt())
                .paidAt(payment.getPaidAt())
                .externalOrderKey(payment.getExternalOrderKey())
                .build();
    }

    private List<AdminOrderLineItemDto> toItems(List<OrderProduct> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(op -> AdminOrderLineItemDto.builder()
                        .orderProductId(op.getId())
                        .productId(op.getProductVariant() != null && op.getProductVariant().getProduct() != null
                                ? op.getProductVariant().getProduct().getId() : null)
                        .productVariantId(op.getProductVariant() != null ? op.getProductVariant().getId() : null)
                        .productName(op.getProductVariant() != null && op.getProductVariant().getProduct() != null
                                ? op.getProductVariant().getProduct().getName() : null)
                        .variantName(op.getProductVariant() != null ? op.getProductVariant().getVariantName() : null)
                        .quantity(op.getQty())
                        .unitPrice(op.getUnitPriceAtOrder())
                        .subtotal(op.getSubtotal())
                        .build())
                .toList();
    }

    private List<AdminRefundSummaryDto> toRefunds(List<Refund> refunds) {
        if (refunds == null) {
            return List.of();
        }
        return refunds.stream()
                .sorted(Comparator.comparing(Refund::getRequestedAt))
                .map(refund -> AdminRefundSummaryDto.builder()
                        .refundId(refund.getId())
                        .amount(refund.getAmount())
                        .statusValue(refund.getStatusCode() != null ? refund.getStatusCode().getValue() : null)
                        .statusName(refund.getStatusCode() != null ? refund.getStatusCode().getDisplayName() : null)
                        .reasonValue(refund.getReasonCode() != null ? refund.getReasonCode().getValue() : null)
                        .reasonName(refund.getReasonCode() != null ? refund.getReasonCode().getDisplayName() : null)
                        .deliveryOptionValue(refund.getDeliveryOptionCode() != null ? refund.getDeliveryOptionCode().getValue() : null)
                        .deliveryOptionName(refund.getDeliveryOptionCode() != null ? refund.getDeliveryOptionCode().getDisplayName() : null)
                        .requestedAt(refund.getRequestedAt())
                        .completedAt(refund.getCompletedAt())
                        .build())
                .toList();
    }

    private List<AdminOrderStatusHistoryDto> toHistories(List<OrderStatusHistory> histories) {
        if (histories == null) {
            return List.of();
        }
        return histories.stream()
                .map(history -> AdminOrderStatusHistoryDto.builder()
                        .fromStatus(history.getFromStatus() != null ? history.getFromStatus().name() : null)
                        .toStatus(history.getToStatus() != null ? history.getToStatus().name() : null)
                        .source(history.getSource())
                        .actor(history.getActor())
                        .reason(history.getReason())
                        .changedAt(history.getChangedAt())
                        .build())
                .toList();
    }
}

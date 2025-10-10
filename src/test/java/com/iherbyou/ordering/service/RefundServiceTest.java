package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.RefundRequestDto;
import com.iherbyou.ordering.dto.RefundResponseDto;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.entity.Refund;
import com.iherbyou.ordering.repository.PaymentRepository;
import com.iherbyou.ordering.repository.RefundRepository;
import com.iherbyou.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefundServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private CodeService codeService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private RefundService refundService;

    private Code requestedStatus;
    private Code reasonCode;
    private Code deliveryOptionCode;
    private Code completedStatus;
    private Code failedStatus;
    private Code paidStatus;

    @BeforeEach
    void setUp() {
        requestedStatus = code(601, "REQUESTED");
        reasonCode = code(611, "CHANGE_OF_MIND");
        deliveryOptionCode = code(621, "SELF_SHIP");
        completedStatus = code(605, "COMPLETED");
        failedStatus = code(606, "FAILED");
        paidStatus = code(403, "PAID");
    }

    @Test
    void requestRefund_updatesOrderStatus() {
        Long userId = 10L;
        Long paymentId = 200L;

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("pw")
                .name("사용자")
                .build();

        Order order = Order.builder()
                .id(300L)
                .user(user)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .paymentStatusCode(paidStatus)
                .paymentPrice(BigDecimal.valueOf(50_000))
                .build();

        RefundRequestDto request = new RefundRequestDto(BigDecimal.valueOf(10_000), 611, 621);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(refundRepository.findByPayment_Id(paymentId)).thenReturn(List.of());
        when(codeService.getCode(61, 611)).thenReturn(reasonCode);
        when(codeService.getCode(62, 621)).thenReturn(deliveryOptionCode);
        when(codeService.getCode(60, 601)).thenReturn(requestedStatus);
        when(refundRepository.save(any(Refund.class))).thenAnswer(invocation -> {
            Refund r = invocation.getArgument(0);
            return Refund.builder()
                    .id(400L)
                    .payment(r.getPayment())
                    .amount(r.getAmount())
                    .reasonCode(r.getReasonCode())
                    .statusCode(r.getStatusCode())
                    .deliveryOptionCode(r.getDeliveryOptionCode())
                    .requestedAt(LocalDateTime.now())
                    .build();
        });

        RefundResponseDto response = refundService.requestRefund(userId, paymentId, request);

        assertThat(response.getStatusKey()).isEqualTo(601);
        verify(orderService).updateStatus(eq(order.getId()), eq(OrderStatus.REFUND_REQUESTED), eq("REFUND_REQUESTED"), eq("user:" + userId), eq("REFUND_REQUEST:400"));
    }

    @Test
    void completeRefund_successFullyRefunded_updatesOrderStatusToRefunded() {
        Long refundId = 500L;
        Long paymentId = 501L;

        Order order = Order.builder()
                .id(600L)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .paymentStatusCode(paidStatus)
                .paymentPrice(BigDecimal.valueOf(30_000))
                .build();

        Refund refund = Refund.builder()
                .id(refundId)
                .payment(payment)
                .amount(BigDecimal.valueOf(30_000))
                .statusCode(requestedStatus)
                .reasonCode(reasonCode)
                .deliveryOptionCode(deliveryOptionCode)
                .build();

        when(refundRepository.findById(refundId)).thenReturn(Optional.of(refund));
        when(codeService.getCode(60, 605)).thenReturn(completedStatus);
        when(refundRepository.save(refund)).thenReturn(refund);
        when(refundRepository.findByPayment_Id(paymentId)).thenReturn(List.of(refund));

        RefundResponseDto response = refundService.completeRefund(refundId, true, "admin:1", LocalDateTime.of(2025, 1, 1, 10, 0));

        assertThat(response.getStatusKey()).isEqualTo(605);
        assertThat(response.getCompletedAt()).isNotNull();
        verify(orderService).updateStatus(eq(order.getId()), eq(OrderStatus.REFUNDED), eq("REFUND_COMPLETED"), eq("admin:1"), eq("REFUND_COMPLETED:" + refundId));
    }

    @Test
    void completeRefund_successPartiallyRefunded_updatesOrderStatusToPartial() {
        Long refundId = 700L;
        Long paymentId = 701L;

        Order order = Order.builder()
                .id(702L)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .paymentStatusCode(paidStatus)
                .paymentPrice(BigDecimal.valueOf(100_000))
                .build();

        Refund refund = Refund.builder()
                .id(refundId)
                .payment(payment)
                .amount(BigDecimal.valueOf(30_000))
                .statusCode(requestedStatus)
                .reasonCode(reasonCode)
                .deliveryOptionCode(deliveryOptionCode)
                .build();

        Refund previouslyCompleted = Refund.builder()
                .id(703L)
                .payment(payment)
                .amount(BigDecimal.valueOf(40_000))
                .statusCode(completedStatus)
                .reasonCode(reasonCode)
                .deliveryOptionCode(deliveryOptionCode)
                .build();

        when(refundRepository.findById(refundId)).thenReturn(Optional.of(refund));
        when(codeService.getCode(60, 605)).thenReturn(completedStatus);
        when(refundRepository.save(refund)).thenReturn(refund);
        when(refundRepository.findByPayment_Id(paymentId)).thenReturn(List.of(refund, previouslyCompleted));

        refundService.completeRefund(refundId, true, null, null);

        verify(orderService).updateStatus(eq(order.getId()), eq(OrderStatus.PARTIALLY_REFUNDED), eq("REFUND_COMPLETED"), eq("system"), eq("REFUND_COMPLETED:" + refundId));
    }

    @Test
    void completeRefund_failureKeepsOrderStatus() {
        Long refundId = 800L;
        Long paymentId = 801L;

        Order order = Order.builder()
                .id(802L)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .paymentStatusCode(paidStatus)
                .paymentPrice(BigDecimal.valueOf(50_000))
                .build();

        Refund refund = Refund.builder()
                .id(refundId)
                .payment(payment)
                .amount(BigDecimal.valueOf(10_000))
                .statusCode(requestedStatus)
                .reasonCode(reasonCode)
                .deliveryOptionCode(deliveryOptionCode)
                .build();

        when(refundRepository.findById(refundId)).thenReturn(Optional.of(refund));
        when(codeService.getCode(60, 606)).thenReturn(failedStatus);
        when(refundRepository.save(refund)).thenReturn(refund);

        RefundResponseDto response = refundService.completeRefund(refundId, false, "admin:2", LocalDateTime.now());

        assertThat(response.getStatusKey()).isEqualTo(606);
        verify(orderService, never()).updateStatus(anyLong(), any(), anyString(), anyString(), anyString());
    }

    @Test
    void completeRefund_throwsWhenRefundMissing() {
        when(refundRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refundService.completeRefund(1L, true, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("refund not found");
    }

    private Code code(int value, String name) {
        return Code.builder()
                .id((long) value)
                .value(value)
                .displayName(name)
                .isActive(true)
                .sortOrder(0)
                .build();
    }
}

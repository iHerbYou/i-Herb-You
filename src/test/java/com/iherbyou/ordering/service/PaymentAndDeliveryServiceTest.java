package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.DeliveryRegisterRequest;
import com.iherbyou.ordering.entity.Delivery;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.repository.DeliveryRepository;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.PaymentRepository;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.entity.UserAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentAndDeliveryServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private CodeService codeService;

    @Mock
    private OrderService orderService;

    private PaymentService paymentService;
    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, orderRepository, codeService, orderService);
        deliveryService = new DeliveryService(orderRepository, deliveryRepository, codeService, orderService);
    }

    @Test
    void requestPayment_whenOrderStatusNotPending_throws() {
        Long userId = 1L;
        Long orderId = 10L;

        Code orderStatus = code(OrderStatus.PAID.getCodeValue());

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("pw")
                .name("사용자")
                .addresses(new ArrayList<>())
                .build();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .orderStatusCode(orderStatus)
                .totalPrice(20_000)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> paymentService.requestPayment(userId, orderId, 410))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("does not allow payment");

        verify(codeService, never()).getCode(anyInt(), anyInt());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void registerTracking_whenOrderStatusNotEligible_throws() {
        Long userId = 1L;
        Long orderId = 20L;

        Code orderStatus = code(OrderStatus.SHIPPED.getCodeValue());

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("pw")
                .name("사용자")
                .addresses(new ArrayList<>())
                .build();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .orderStatusCode(orderStatus)
                .totalPrice(40_000)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        DeliveryRegisterRequest request = DeliveryRegisterRequest.builder()
                .deliveryCompany("CJ")
                .trackingNumber("T0001")
                .build();

        assertThatThrownBy(() -> deliveryService.registerTracking(userId, orderId, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("does not allow delivery registration");

        verify(orderService, never()).updateStatus(eq(orderId), eq(OrderStatus.PACKING), anyString(), anyString(), anyString());
    }

    @Test
    void completePayment_thenRegisterTracking_advancesStatuses() {
        Long userId = 1L;
        Long orderId = 10L;
        Long paymentId = 10L;

        Code pendingOrderStatus = code(OrderStatus.PENDING.getCodeValue());

        Code paidOrderStatus = code(OrderStatus.PAID.getCodeValue());

        Code packingOrderStatus = code(OrderStatus.PACKING.getCodeValue());

        Code paymentReadyStatus = code(401);

        Code paymentPaidStatus = code(403);

        Code deliveryPreparingStatus = code(201);

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("pw")
                .name("사용자")
                .addresses(new ArrayList<>())
                .build();

        UserAddress userAddress = UserAddress.builder()
                .id(100L)
                .user(user)
                .recipient("수령인")
                .phone("010-0000-0000")
                .zipcode("12345")
                .address("서울시")
                .isDefault(true)
                .build();
        user.getAddresses().add(userAddress);

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .orderStatusCode(pendingOrderStatus)
                .totalPrice(30_000)
                .build();

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .paymentStatusCode(paymentReadyStatus)
                .paymentMethodCode(code(510))
                .paymentPrice(BigDecimal.valueOf(order.getTotalPrice()))
                .requestedAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(codeService.getCode(40, 403)).thenReturn(paymentPaidStatus);
        when(orderService.updateStatus(eq(orderId), eq(OrderStatus.PAID), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    order.setOrderStatusCode(paidOrderStatus);
                    return order;
                });
        when(orderService.updateStatus(eq(orderId), eq(OrderStatus.PACKING), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    order.setOrderStatusCode(packingOrderStatus);
                    return order;
                });

        Payment result = paymentService.completePayment(userId, paymentId);

        assertThat(result.getPaymentStatusCode()).isSameAs(paymentPaidStatus);
        assertThat(result.getPaidAt()).isNotNull();
        assertThat(order.getOrderStatusCode()).isSameAs(paidOrderStatus);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(deliveryRepository.findByOrder_Id(orderId)).thenReturn(Optional.empty());
        when(codeService.getCode(20, 201)).thenReturn(deliveryPreparingStatus);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            delivery.setId(200L);
            return delivery;
        });

        DeliveryRegisterRequest request = DeliveryRegisterRequest.builder()
                .deliveryCompany("CJ")
                .trackingNumber("T123456789")
                .build();

        Delivery savedDelivery = deliveryService.registerTracking(userId, orderId, request);

        assertThat(savedDelivery.getDeliveryCompany()).isEqualTo(request.getDeliveryCompany());
        assertThat(savedDelivery.getTrackingNumber()).isEqualTo(request.getTrackingNumber());
        assertThat(savedDelivery.getCode()).isSameAs(deliveryPreparingStatus);
        assertThat(order.getOrderStatusCode()).isSameAs(packingOrderStatus);

        verify(orderService).updateStatus(eq(orderId), eq(OrderStatus.PAID), eq("PAYMENT_PAID"), eq("user:" + userId), anyString());
        verify(orderService).updateStatus(eq(orderId), eq(OrderStatus.PACKING), eq("DELIVERY_TRACKING_REGISTERED"), eq("user:" + userId), anyString());
        verify(deliveryRepository).save(any(Delivery.class));
    }
    private Code code(int value) {
        return Code.builder()
                .value(value)
                .displayName("code-" + value)
                .isActive(true)
                .sortOrder(0)
                .build();
    }
}

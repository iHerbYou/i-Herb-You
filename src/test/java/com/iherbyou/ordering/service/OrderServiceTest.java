package com.iherbyou.ordering.service;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.dto.OrderCreateDto;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderStatusHistory;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.repository.OrderStatusHistoryRepository;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CodeService codeService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, codeService, orderStatusHistoryRepository, entityManager);
    }

    @Test
    void createOrder_calculatesTotals_andPersistsItems() {
        // given
        Long userId = 1L;
        OrderCreateDto dto = OrderCreateDto.builder()
                .discount(5_000)
                .items(List.of(
                        OrderCreateDto.Item.builder()
                                .productVariantId(100L)
                                .qty(2)
                                .unitPrice(10_000)
                                .regularPrice(12_000)
                                .build(),
                        OrderCreateDto.Item.builder()
                                .productVariantId(200L)
                                .qty(1)
                                .unitPrice(20_000)
                                .regularPrice(22_000)
                                .build()
                ))
                .build();

        User userRef = mock(User.class);
        ProductVariant variantRef = mock(ProductVariant.class);
        Code statusCode = mock(Code.class);

        when(entityManager.getReference(User.class, userId)).thenReturn(userRef);
        when(entityManager.getReference(ProductVariant.class, 100L)).thenReturn(variantRef);
        when(entityManager.getReference(ProductVariant.class, 200L)).thenReturn(variantRef);
        when(codeService.getCode(30, 301)).thenReturn(statusCode);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order saved = orderService.createOrder(userId, dto);

        // then
        assertThat(saved.getOrderProducts()).hasSize(2);
        assertThat(saved.getSubtotal()).isEqualTo(40_000);
        assertThat(saved.getDiscount()).isEqualTo(5_000);
        assertThat(saved.getDeliveryFee()).isEqualTo(2_500); // 40,000 - 5,000 < 50,000
        assertThat(saved.getTotalPrice()).isEqualTo(37_500);
        assertThat(saved.getOrderStatusCode()).isSameAs(statusCode);

        verify(orderRepository).save(saved);
    }

    @Test
    void createOrder_whenDeliveryFeeIsWaivedForLargeOrders() {
        // given
        Long userId = 1L;
        OrderCreateDto dto = OrderCreateDto.builder()
                .discount(0)
                .items(List.of(
                        OrderCreateDto.Item.builder()
                                .productVariantId(100L)
                                .qty(3)
                                .unitPrice(20_000)
                                .build()
                ))
                .build();

        User userRef = mock(User.class);
        ProductVariant variantRef = mock(ProductVariant.class);
        Code statusCode = mock(Code.class);

        when(entityManager.getReference(User.class, userId)).thenReturn(userRef);
        when(entityManager.getReference(ProductVariant.class, 100L)).thenReturn(variantRef);
        when(codeService.getCode(30, 301)).thenReturn(statusCode);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order saved = orderService.createOrder(userId, dto);

        // then
        assertThat(saved.getSubtotal()).isEqualTo(60_000);
        assertThat(saved.getDeliveryFee()).isZero();
        assertThat(saved.getTotalPrice()).isEqualTo(60_000);
    }

    @Test
    void createOrder_throwsWhenOrderStatusCodeMissing() {
        // given
        Long userId = 1L;
        OrderCreateDto dto = OrderCreateDto.builder()
                .items(List.of(
                        OrderCreateDto.Item.builder()
                                .productVariantId(100L)
                                .qty(1)
                                .unitPrice(10_000)
                                .build()
                ))
                .build();

        when(codeService.getCode(30, 301)).thenReturn(null);

        // when / then
        assertThatThrownBy(() -> orderService.createOrder(userId, dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ORDER_STATUS:PENDING");

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void updateStatus_transitionsWhenAllowedAndRecordsHistory() {
        // given
        Long orderId = 10L;
        String correlation = "corr-123";

        Code currentCode = mock(Code.class);
        when(currentCode.getValue()).thenReturn(OrderStatus.PENDING.getCodeValue());

        Code nextCode = mock(Code.class);
        when(codeService.getCode(OrderStatus.GROUP_VALUE, OrderStatus.PAID.getCodeValue())).thenReturn(nextCode);

        Order order = Order.builder().build();
        order.setOrderStatusCode(currentCode);

        when(orderStatusHistoryRepository.existsByOrder_IdAndToStatusAndCorrelationId(orderId, OrderStatus.PAID, correlation))
                .thenReturn(false);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderStatusHistoryRepository.save(any(OrderStatusHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order updated = orderService.updateStatus(orderId, OrderStatus.PAID, "PAYMENT_PAID", "system", correlation);

        // then
        assertThat(updated.getOrderStatusCode()).isSameAs(nextCode);

        ArgumentCaptor<OrderStatusHistory> historyCaptor = ArgumentCaptor.forClass(OrderStatusHistory.class);
        verify(orderStatusHistoryRepository).save(historyCaptor.capture());
        OrderStatusHistory history = historyCaptor.getValue();
        assertThat(history.getOrder()).isSameAs(order);
        assertThat(history.getFromStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(history.getToStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(history.getCorrelationId()).isEqualTo(correlation);
    }

    @Test
    void updateStatus_throwsWhenTransitionNotAllowed() {
        // given
        Long orderId = 20L;
        String correlation = "corr-456";

        Code currentCode = mock(Code.class);
        when(currentCode.getValue()).thenReturn(OrderStatus.SHIPPED.getCodeValue());

        Order order = Order.builder().build();
        order.setOrderStatusCode(currentCode);

        when(orderStatusHistoryRepository.existsByOrder_IdAndToStatusAndCorrelationId(orderId, OrderStatus.PAID, correlation))
                .thenReturn(false);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when / then
        assertThatThrownBy(() -> orderService.updateStatus(orderId, OrderStatus.PAID, "PAYMENT_PAID", "system", correlation))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalid order status transition");

        verify(orderStatusHistoryRepository, never()).save(any(OrderStatusHistory.class));
    }

    @Test
    void updateStatus_isIdempotentWhenHistoryExists() {
        // given
        Long orderId = 30L;
        String correlation = "corr-789";

        Code currentCode = mock(Code.class);
        Order order = Order.builder().build();
        order.setOrderStatusCode(currentCode);

        when(orderStatusHistoryRepository.existsByOrder_IdAndToStatusAndCorrelationId(orderId, OrderStatus.PAID, correlation))
                .thenReturn(true);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        Order updated = orderService.updateStatus(orderId, OrderStatus.PAID, "PAYMENT_PAID", "system", correlation);

        // then
        assertThat(updated).isSameAs(order);
        verify(orderStatusHistoryRepository, never()).save(any(OrderStatusHistory.class));
        verify(codeService, never()).getCode(anyInt(), anyInt());
    }

    @Test
    void updateStatus_generatesCorrelationWhenMissing() {
        // given
        Long orderId = 40L;

        Code currentCode = mock(Code.class);
        when(currentCode.getValue()).thenReturn(OrderStatus.PENDING.getCodeValue());

        Code nextCode = mock(Code.class);
        when(codeService.getCode(OrderStatus.GROUP_VALUE, OrderStatus.PAID.getCodeValue())).thenReturn(nextCode);

        Order order = Order.builder().build();
        order.setOrderStatusCode(currentCode);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderStatusHistoryRepository.save(any(OrderStatusHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order updated = orderService.updateStatus(orderId, OrderStatus.PAID, "PAYMENT_PAID", "system", null);

        // then
        assertThat(updated.getOrderStatusCode()).isSameAs(nextCode);

        ArgumentCaptor<OrderStatusHistory> historyCaptor = ArgumentCaptor.forClass(OrderStatusHistory.class);
        verify(orderStatusHistoryRepository).save(historyCaptor.capture());
        OrderStatusHistory savedHistory = historyCaptor.getValue();
        assertThat(savedHistory.getCorrelationId()).isNotBlank();

        verify(orderStatusHistoryRepository, never())
                .existsByOrder_IdAndToStatusAndCorrelationId(anyLong(), any(), anyString());
    }
}

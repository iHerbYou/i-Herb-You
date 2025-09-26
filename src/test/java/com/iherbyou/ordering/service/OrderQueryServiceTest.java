package com.iherbyou.ordering.service;

import com.iherbyou.catalog.entity.Brand;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.dto.OrderSummaryDto;
import com.iherbyou.ordering.entity.Delivery;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderQueryService orderQueryService;

    private Code orderStatus;

    @BeforeEach
    void setUp() {
        orderStatus = Code.builder()
                .value(301)
                .displayName("PENDING")
                .sortOrder(0)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void listUserOrders_returnsSummaryDtoPage() {
        // given
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("encoded")
                .name("사용자")
                .build();

        Order order = Order.builder()
                .id(10L)
                .user(user)
                .orderStatusCode(orderStatus)
                .orderDate(LocalDateTime.now())
                .subtotal(30_000)
                .deliveryFee(2_500)
                .discount(5_000)
                .totalPrice(27_500)
                .build();

        Page<Order> page = new PageImpl<>(List.of(order), PageRequest.of(0, 10), 1);
        when(orderRepository.findByUser_IdOrderByOrderDateDesc(eq(userId), any(Pageable.class)))
                .thenReturn(page);

        // when
        Page<OrderSummaryDto> result = orderQueryService.listUserOrders(userId, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        OrderSummaryDto summary = result.getContent().get(0);
        assertThat(summary.getId()).isEqualTo(order.getId());
        assertThat(summary.getTotalPrice()).isEqualTo(27_500);
        assertThat(summary.getOrderStatusKey()).isEqualTo(301);
    }

    @Test
    void getOrderDetail_returnsDetailDtoWithItemsAndDeliveryInfo() {
        // given
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("user@example.com")
                .password("encoded")
                .name("사용자")
                .build();

        Brand brand = Brand.builder()
                .name("iHerb")
                .build();

        Product product = Product.builder()
                .brand(brand)
                .name("비타민C")
                .maxQtyPerOrder(6)
                .sales(0)
                .avgRating(0.0)
                .reviewCount(0)
                .minPrice(0)
                .build();

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .variantName("500mg")
                .salePrice(15_000)
                .build();

        Order order = Order.builder()
                .id(100L)
                .user(user)
                .orderStatusCode(orderStatus)
                .orderDate(LocalDateTime.now())
                .subtotal(15_000)
                .deliveryFee(2_500)
                .discount(0)
                .totalPrice(17_500)
                .customsInfo("P123456789012")
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .productVariant(variant)
                .qty(1)
                .unitPriceAtOrder(15_000)
                .regularPriceAtOrder(18_000)
                .subtotal(15_000)
                .build();
        order.addItem(orderProduct);

        Code deliveryStatus = Code.builder()
                .value(201)
                .displayName("PREPARING")
                .sortOrder(0)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Delivery delivery = Delivery.builder()
                .order(order)
                .code(deliveryStatus)
                .deliveryCompany("CJ")
                .trackingNumber("T123456789")
                .delStartAt(LocalDateTime.now())
                .delCompleteAt(LocalDateTime.now())
                .build();
        order.setDelivery(delivery);

        when(orderRepository.existsByIdAndUser_Id(order.getId(), userId)).thenReturn(true);
        when(orderRepository.findWithDetailsById(order.getId())).thenReturn(Optional.of(order));

        // when
        OrderDetailDto detail = orderQueryService.getOrderDetail(order.getId(), userId);

        // then
        assertThat(detail.getId()).isEqualTo(order.getId());
        assertThat(detail.getOrderStatusKey()).isEqualTo(301);
        assertThat(detail.getDeliveryCompany()).isEqualTo("CJ");
        assertThat(detail.getTrackingNumber()).isEqualTo("T123456789");
        assertThat(detail.getItems()).hasSize(1);
        assertThat(detail.getItems().get(0).getProductName()).isEqualTo("비타민C");
    }

    @Test
    void getOrderDetail_throwsWhenNotOwnedByUser() {
        when(orderRepository.existsByIdAndUser_Id(1L, 2L)).thenReturn(false);

        assertThatThrownBy(() -> orderQueryService.getOrderDetail(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no permission");
    }
}

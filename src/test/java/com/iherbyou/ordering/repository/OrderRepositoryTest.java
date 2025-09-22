package com.iherbyou.ordering.repository;

import com.iherbyou.catalog.Brand;
import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.ProductVariant;
import com.iherbyou.common.Code;
import com.iherbyou.common.CodeGroup;
import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.OrderProduct;
import com.iherbyou.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByUserOrders_paging() {
        // given
        User user = em.persist(User.builder()
                .email("order-test-" + UUID.randomUUID() + "@iherbyou.com")
                .password("pw")
                .name("사용자1")
                .build());

        CodeGroup group = em.persist(CodeGroup.builder()
                .groupKey("ORDER_STATUS_" + UUID.randomUUID())
                .groupName("주문상태")
                .sortOrder(1)
                .isActive(true)
                .build());

        Code status = em.persist(Code.builder()
                .codeGroup(group)
                .codeKey("CREATED")
                .displayName("주문생성")
                .sortOrder(1)
                .isActive(true)
                .build());

        Order order = Order.builder()
                .user(user)
                .orderStatusCode(status)
                .subtotal(20000)
                .deliveryFee(2500)
                .discount(0)
                .totalPrice(22500)
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        // when
        Page<Order> page = orderRepository.findByUser_IdOrderByOrderDateDesc(user.getId(), PageRequest.of(0, 10));

        // then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getSubtotal()).isEqualTo(20000);
    }

    @Test
    void findWithDetailsById_fetchJoin() {
        // given
        User user = em.persist(User.builder()
                .email("order-test-" + UUID.randomUUID() + "@iherbyou.com")
                .password("pw")
                .name("사용자2")
                .build());

        CodeGroup group = em.persist(CodeGroup.builder()
                .groupKey("ORDER_STATUS_" + UUID.randomUUID())
                .groupName("주문상태")
                .sortOrder(1)
                .isActive(true)
                .build());

        Code status = em.persist(Code.builder()
                .codeGroup(group)
                .codeKey("CREATED")
                .displayName("주문생성")
                .sortOrder(1)
                .isActive(true)
                .build());

        Brand brand = em.persist(Brand.builder().name("브랜드-" + UUID.randomUUID()).build());

        Product p = em.persist(Product.builder()
                .brand(brand)
                .name("비타민C")
                .maxQtyPerOrder(10)
                .build());

        ProductVariant pv = em.persist(ProductVariant.builder()
                .product(p)
                .variantName("100정")
                .build());

        Order order = Order.builder()
                .user(user)
                .orderStatusCode(status)
                .subtotal(15000)
                .deliveryFee(2500)
                .discount(0)
                .totalPrice(17500)
                .orderDate(LocalDateTime.now())
                .build();

        OrderProduct op = OrderProduct.builder()
                .order(order)
                .productVariant(pv)
                .qty(1)
                .unitPriceAtOrder(15000)
                .regularPriceAtOrder(18000)
                .subtotal(15000)
                .build();

        order.addItem(op);
        orderRepository.save(order);

        // when
        var found = orderRepository.findWithDetailsById(order.getId()).orElseThrow();

        // then
        assertThat(found.getOrderProducts()).hasSize(1);
        assertThat(found.getOrderProducts().get(0).getProductVariant().getVariantName()).isEqualTo("100정");
    }
}

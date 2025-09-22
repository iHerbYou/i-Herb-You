package com.iherbyou.ordering.service;

import com.iherbyou.catalog.Brand;
import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.ProductVariant;
import com.iherbyou.common.Code;
import com.iherbyou.common.CodeGroup;
import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.OrderProduct;
import com.iherbyou.ordering.dto.OrderDetailDto;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class
OrderQueryServiceTest {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void getOrderDetail_fetchesOrderProducts() {
        // given
        String unique = UUID.randomUUID().toString();

        User user = User.builder()
                .email("tester-" + unique + "@iherbyou.com")
                .password("secret")
                .name("테스터")
                .build();
        em.persist(user);

        CodeGroup orderStatusGroup = CodeGroup.builder()
                .groupKey("ORDER_STATUS_" + unique)
                .groupName("주문상태" + unique)
                .sortOrder(1)
                .isActive(true)
                .build();
        em.persist(orderStatusGroup);

        Code createdStatus = Code.builder()
                .codeGroup(orderStatusGroup)
                .codeKey("CREATED_" + unique)
                .displayName("주문 생성")
                .description("주문 접수 상태")
                .sortOrder(1)
                .isActive(true)
                .build();
        em.persist(createdStatus);

        Brand brand = Brand.builder()
                .name("brand-" + unique)
                .build();
        em.persist(brand);

        Product product = Product.builder()
                .brand(brand)
                .name("비타민C")
                .maxQtyPerOrder(10)
                .build();
        em.persist(product);

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .variantName("100정")
                .listPrice(18000)
                .salePrice(15000)
                .build();
        em.persist(variant);

        Order order = Order.builder()
                .user(user)
                .orderStatusCode(createdStatus)
                .subtotal(15000)
                .deliveryFee(2500)
                .discount(0)
                .totalPrice(17500)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .productVariant(variant)
                .qty(1)
                .unitPriceAtOrder(15000)
                .regularPriceAtOrder(18000)
                .subtotal(15000)
                .build();

        order.addItem(orderProduct);
        orderRepository.save(order);

        em.flush();
        Long orderId = order.getId();
        Long userId = user.getId();
        em.clear();

        // when
        OrderDetailDto detail = orderQueryService.getOrderDetail(orderId, userId);

        // then
        assertThat(detail.getItems()).hasSize(1);
        assertThat(detail.getItems().get(0).getVariantName()).isEqualTo("100정");
    }
}

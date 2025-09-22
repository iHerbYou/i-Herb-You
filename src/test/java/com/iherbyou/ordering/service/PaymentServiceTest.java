package com.iherbyou.ordering.service;

import com.iherbyou.catalog.Brand;
import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.ProductVariant;
import com.iherbyou.common.Code;
import com.iherbyou.common.CodeGroup;
import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.Payment;
import com.iherbyou.ordering.common.CodeFinder;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ssPaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CodeFinder codeFinder;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void ensureColumns() {
        try {
            em.createNativeQuery("ALTER TABLE payment ADD COLUMN requested_at DATETIME(6) NOT NULL").executeUpdate();
        } catch (Exception ignored) {
        }
        try {
            em.createNativeQuery("ALTER TABLE payment ADD COLUMN paid_at DATETIME(6)").executeUpdate();
        } catch (Exception ignored) {
        }
    }

    @Test
    void requestAndCompletePayment() {
        User user = User.builder()
                .email("payment-test@iherbyou.com")
                .password("secret")
                .name("결제테스트")
                .build();
        em.persist(user);

        Code createdStatus = ensureCode("ORDER_STATUS", "CREATED", "주문생성");
        ensureCode("PAYMENT_STATUS", "PENDING", "결제대기");
        ensureCode("PAYMENT_STATUS", "PAID", "결제완료");
        ensureCode("PAYMENT_METHOD", "CARD", "신용/체크카드");

        Brand brand = Brand.builder()
                .name("결제테스트브랜드")
                .build();
        em.persist(brand);

        Product product = Product.builder()
                .brand(brand)
                .name("결제테스트상품")
                .maxQtyPerOrder(10)
                .build();
        em.persist(product);

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .variantName("기본옵션")
                .build();
        em.persist(variant);

        Order order = Order.builder()
                .user(user)
                .orderStatusCode(createdStatus)
                .subtotal(15000)
                .deliveryFee(2500)
                .discount(0)
                .totalPrice(17500)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        Payment requested = paymentService.requestPayment(order.getId(), "CARD");

        assertThat(requested.getPaymentStatusCode().getCodeKey()).isEqualTo("PENDING");
        assertThat(requested.getPaymentMethodCode().getCodeKey()).isEqualTo("CARD");
        assertThat(requested.getPaymentPrice().intValue()).isEqualTo(order.getTotalPrice());
        assertThat(requested.getRequestedAt()).isNotNull();
        assertThat(requested.getPaidAt()).isNull();
        
        Payment completed = paymentService.completePayment(requested.getId());

        assertThat(completed.getPaymentStatusCode().getCodeKey()).isEqualTo("PAID");
        assertThat(completed.getPaidAt()).isNotNull();
        assertThat(completed.getOrder().getOrderStatusCode().getCodeKey()).isEqualTo("PAID");
    }

    private Code ensureCode(String groupKey, String codeKey, String displayName) {
        try {
            return codeFinder.get(groupKey, codeKey);
        } catch (IllegalArgumentException ignored) {
            CodeGroup group = ensureGroup(groupKey);
            Code code = Code.builder()
                    .codeGroup(group)
                    .codeKey(codeKey)
                    .displayName(displayName)
                    .sortOrder(1)
                    .isActive(true)
                    .build();
            em.persist(code);
            em.flush();
            return code;
        }
    }

    private CodeGroup ensureGroup(String groupKey) {
        return em.createQuery("select g from CodeGroup g where g.groupKey = :key", CodeGroup.class)
                .setParameter("key", groupKey)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    CodeGroup group = CodeGroup.builder()
                            .groupKey(groupKey)
                            .groupName(groupKey)
                            .sortOrder(1)
                            .isActive(true)
                            .build();
                    em.persist(group);
                    em.flush();
                    return group;
                });
    }
}

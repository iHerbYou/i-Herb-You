package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.dto.admin.AdminOrderSearchCondition;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.entity.Payment;
import com.iherbyou.ordering.entity.Delivery;
import com.iherbyou.ordering.entity.OrderProduct;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderAdminRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<Order> searchAdminOrders(AdminOrderSearchCondition condition, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).distinct(true);

        List<Predicate> predicates = buildPredicates(condition, cb, root);
        query.where(predicates.toArray(Predicate[]::new));
        query.orderBy(toOrderList(pageable.getSort(), cb, root));

        TypedQuery<Order> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Order> content = typedQuery.getResultList();

        long total = count(condition);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Order> findAdminOrderDetail(Long orderId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        root.fetch("orderStatusCode", JoinType.LEFT);
        root.fetch("user", JoinType.LEFT);
        Fetch<Order, Delivery> deliveryFetch = root.fetch("delivery", JoinType.LEFT);
        if (deliveryFetch != null) {
            deliveryFetch.fetch("code", JoinType.LEFT);
        }
        Fetch<Order, Payment> paymentFetch = root.fetch("payment", JoinType.LEFT);
        if (paymentFetch != null) {
            paymentFetch.fetch("paymentMethodCode", JoinType.LEFT);
            paymentFetch.fetch("paymentStatusCode", JoinType.LEFT);
        }
        Fetch<Order, OrderProduct> orderProductFetch = root.fetch("orderProducts", JoinType.LEFT);
        if (orderProductFetch != null) {
            Fetch<OrderProduct, ?> variantFetch = orderProductFetch.fetch("productVariant", JoinType.LEFT);
            if (variantFetch instanceof Fetch<?, ?> fetch) {
                fetch.fetch("product", JoinType.LEFT);
            }
        }

        query.select(root).distinct(true);
        query.where(cb.equal(root.get("id"), orderId));

        return em.createQuery(query).getResultStream().findFirst();
    }

    private long count(AdminOrderSearchCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Order> countRoot = countQuery.from(Order.class);
        countQuery.select(cb.countDistinct(countRoot));
        List<Predicate> countPredicates = buildPredicates(condition, cb, countRoot);
        countQuery.where(countPredicates.toArray(Predicate[]::new));
        return em.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(AdminOrderSearchCondition condition,
                                            CriteriaBuilder cb,
                                            Root<Order> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (condition.hasOrderDateFrom()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), condition.getOrderDateFrom()));
        }
        if (condition.hasOrderDateTo()) {
            predicates.add(cb.lessThan(root.get("orderDate"), condition.getOrderDateTo()));
        }
        if (condition.hasOrderStatus()) {
            Join<Order, Code> orderStatusJoin = root.join("orderStatusCode", JoinType.LEFT);
            predicates.add(cb.equal(orderStatusJoin.get("value"), condition.getOrderStatusValue()));
        }

        if (condition.hasUserId() || condition.hasUserEmail()) {
            Join<Order, User> userJoin = root.join("user", JoinType.LEFT);
            if (condition.hasUserId()) {
                predicates.add(cb.equal(userJoin.get("id"), condition.getUserId()));
            }
            if (condition.hasUserEmail()) {
                predicates.add(cb.like(cb.lower(userJoin.get("email")), pattern(condition.getUserEmail())));
            }
        }

        if (condition.hasPaymentStatus() || condition.hasPaymentMethod()) {
            Join<Order, Payment> paymentJoin = root.join("payment", JoinType.LEFT);
            if (condition.hasPaymentStatus()) {
                Join<Payment, Code> paymentStatusJoin = paymentJoin.join("paymentStatusCode", JoinType.LEFT);
                predicates.add(cb.equal(paymentStatusJoin.get("value"), condition.getPaymentStatusValue()));
            }
            if (condition.hasPaymentMethod()) {
                Join<Payment, Code> paymentMethodJoin = paymentJoin.join("paymentMethodCode", JoinType.LEFT);
                predicates.add(cb.equal(paymentMethodJoin.get("value"), condition.getPaymentMethodValue()));
            }
        }

        if (condition.hasDeliveryStatus()) {
            Join<Order, Delivery> deliveryJoin = root.join("delivery", JoinType.LEFT);
            Join<Delivery, Code> deliveryStatusJoin = deliveryJoin.join("code", JoinType.LEFT);
            predicates.add(cb.equal(deliveryStatusJoin.get("value"), condition.getDeliveryStatusValue()));
        }

        return predicates;
    }

    private List<jakarta.persistence.criteria.Order> toOrderList(Sort sort,
                                                                 CriteriaBuilder cb,
                                                                 Root<Order> root) {
        List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();
        if (sort == null || sort.isUnsorted()) {
            orders.add(cb.desc(root.get("orderDate")));
            return orders;
        }
        sort.forEach(order -> {
            String property = order.getProperty();
            jakarta.persistence.criteria.Path<?> path;
            switch (property) {
                case "id" -> path = root.get("id");
                case "totalPrice" -> path = root.get("totalPrice");
                case "orderDate" -> path = root.get("orderDate");
                default -> {
                    path = root.get("orderDate");
                }
            }
            orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
        });
        return orders;
    }

    private String pattern(String email) {
        String trimmed = email.trim().toLowerCase();
        return StringUtils.hasText(trimmed) ? trimmed + "%" : "%";
    }
}

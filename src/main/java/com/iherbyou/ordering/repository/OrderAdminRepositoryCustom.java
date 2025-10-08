package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.dto.admin.AdminOrderSearchCondition;
import com.iherbyou.ordering.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderAdminRepositoryCustom {

    Page<Order> searchAdminOrders(AdminOrderSearchCondition condition, Pageable pageable);

    Optional<Order> findAdminOrderDetail(Long orderId);
}

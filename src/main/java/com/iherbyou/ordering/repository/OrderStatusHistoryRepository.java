package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    boolean existsByOrder_IdAndToStatusAndCorrelationId(Long orderId, OrderStatus toStatus, String correlationId);
}

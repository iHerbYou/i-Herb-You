package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    // 주문 상세 구성품 조회
    List<OrderProduct> findByOrder_Id(Long orderId);
}
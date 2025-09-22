package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // 주문 기준 배송 단건
    Optional<Delivery> findByOrder_Id(Long orderId);

    // 송장번호로 조회 (unique라 했으니 편의상 추가)
    Optional<Delivery> findByTrackingNumber(String trackingNumber);

}
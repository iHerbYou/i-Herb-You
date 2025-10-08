package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 주문 기준 단건 결제 조회
    Optional<Payment> findByOrder_Id(Long orderId);

    // 상태별 결제 조회 (Code id로 필터)
    List<Payment> findByPaymentStatusCode_Id(Long paymentStatusCodeId);

    // 특정 주문이 특정 결제상태인지 존재 여부 체크
    boolean existsByOrder_IdAndPaymentStatusCode_Id(Long orderId, Long paymentStatusCodeId);

    Optional<Payment> findByExternalOrderKey(String externalOrderKey);

    List<Payment> findByOrder_IdIn(Collection<Long> orderIds);
}

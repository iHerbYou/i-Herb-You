package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    // 결제 기준 환불 목록
    List<Refund> findByPayment_Id(Long paymentId);

    // 최근 환불 1건 (상태 변경 직후 확인용)
    Optional<Refund> findTopByPayment_IdOrderByIdDesc(Long paymentId);

}
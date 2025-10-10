package com.iherbyou.promotion.point.repository;

import com.iherbyou.user.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByUserId(Long userId);

    Optional<PointHistory> findFirstByPointTypeCodeValueAndRelatedReviewId(Integer pointTypeValue, Long reviewId);

    Optional<PointHistory> findFirstByPointTypeCodeValueAndRelatedOrderId(Integer pointTypeValue, Long orderId);

    List<PointHistory> findByPointTypeCodeValueAndExpiredFalseAndExpiresAtBefore(Integer pointTypeValue, LocalDateTime now);

    List<PointHistory> findByExpiredFalseAndExpiresAtBefore(LocalDateTime now);
}

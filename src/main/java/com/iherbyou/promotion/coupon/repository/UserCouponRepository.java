package com.iherbyou.promotion.coupon.repository;

import com.iherbyou.user.entity.UserCoupon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    long countByUserIdAndCouponId(Long userId, Long couponId);

    @EntityGraph(attributePaths = "coupon")
    List<UserCoupon> findByUserId(Long userId);

    @EntityGraph(attributePaths = "coupon")
    Optional<UserCoupon> findFirstByUserIdAndCoupon_CodeAndIsUsedFalseAndExpiredAtGreaterThanEqualOrderByExpiredAtAsc(Long userId, String couponCode, LocalDate today);

    List<UserCoupon> findByIsUsedFalseAndExpiredAtBefore(LocalDate today);
}

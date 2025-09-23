package com.iherbyou.cart.repository;

import com.iherbyou.cart.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // userId로 위시리스트 엔티티 조회 (없으면 생성 로직은 Service에서 처리)
    @Query("select w from Wishlist w where w.user.id = :userId")
    Optional<Wishlist> findByUserId(Long userId);

    // User와 연결된 Wishlist의 id만 가져오기
    @Query("select w.id from Wishlist w where w.user.id = :userId")
    Optional<Long> findIdByUserId(Long userId);
}
package com.iherbyou.wishlist.repository;

import com.iherbyou.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * userId로 위시리스트 조회 (상품 정보 + 이미지 포함)
     * N+1 문제 방지를 위한 fetch join
     */
    @Query("""
            SELECT w 
            FROM Wishlist w 
            LEFT JOIN FETCH w.wishlistProducts wp 
            LEFT JOIN FETCH wp.product p
            WHERE w.user.id = :userId
            """)
    Optional<Wishlist> findByUserIdWithProducts(Long userId);
}
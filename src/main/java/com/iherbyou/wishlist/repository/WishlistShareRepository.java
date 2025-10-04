package com.iherbyou.wishlist.repository;

import com.iherbyou.wishlist.entity.WishlistShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface WishlistShareRepository extends JpaRepository<WishlistShare, Long> {

    /**
     * shareId로 공유 조회
     */
    Optional<WishlistShare> findByShareId(String shareId);

    /**
     * 만료된 공유 삭제 (배치 작업용)
     */
    @Query("DELETE FROM WishlistShare ws WHERE ws.expiresAt < :now")
    void deleteExpiredShares(Instant now);
}
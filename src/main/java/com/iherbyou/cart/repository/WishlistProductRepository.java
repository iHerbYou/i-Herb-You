package com.iherbyou.cart.repository;

import com.iherbyou.cart.entity.WishlistProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {

    /**
     * 위시리스트 상품 페이지 조회 (커서 페이징, createdAt 기준 최신순)
     * - cursor가 null이면 최신부터 limit만큼 가져옴
     * - cursor가 있으면 createdAt < cursor 조건으로 이어서 가져옴
     */
    @Query("""
                select wp from WishlistProduct wp
                join fetch wp.product p
                where wp.wishlist.id = :wishlistId
                order by wp.createdAt desc, wp.id desc
            """)
    List<WishlistProduct> findPage(Long wishlistId, Pageable pageable);

    /**
     * 특정 위시리스트에 해당 상품이 이미 존재하는지 확인
     */
    boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId);

    /**
     * 단건 삭제 (내 위시리스트 소유권 검증 포함)
     */
    int deleteByIdAndWishlistId(Long itemId, Long wishlistId);

    /**
     * 여러 건 삭제 (벌크 삭제)
     */
    int deleteAllByWishlistId(Long wishlistId);

    long countByWishlistId(Long wishlistId);
}
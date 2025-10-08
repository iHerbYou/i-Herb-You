package com.iherbyou.cart.repository;

import com.iherbyou.cart.entity.Cart;
import com.iherbyou.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 회원 장바구니 조회 (cartProducts, productVariant, product, stock, productImgs, brand 모두 fetch)
    @EntityGraph(attributePaths = {"cartProducts", "cartProducts.productVariant", "cartProducts.productVariant.product", "cartProducts.productVariant.product.brand", "cartProducts.productVariant.product.productImgs", "cartProducts.productVariant.stock"})
    Optional<Cart> findByUser(User user);

    // 비회원 장바구니 조회
    @EntityGraph(attributePaths = {"cartProducts", "cartProducts.productVariant", "cartProducts.productVariant.product", "cartProducts.productVariant.product.brand", "cartProducts.productVariant.product.productImgs", "cartProducts.productVariant.stock"})
    Optional<Cart> findByGuestToken(String guestToken);

    // 7일 지난 비회원 장바구니 삭제
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.guestToken IS NOT NULL AND c.createdAt < :expiryDate")
    void deleteExpiredGuestCarts(@Param("expiryDate") LocalDateTime expiryDate);

    // 휴면 계정 장바구니 삭제
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id IN " + "(SELECT u.id FROM User u WHERE u.statusCode.displayName != 'ACTIVE')")
    void deleteInactiveUserCarts();
}
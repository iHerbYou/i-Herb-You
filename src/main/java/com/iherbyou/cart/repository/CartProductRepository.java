package com.iherbyou.cart.repository;

import com.iherbyou.cart.entity.Cart;
import com.iherbyou.cart.entity.CartProduct;
import com.iherbyou.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    Optional<CartProduct> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);

    @Modifying
    @Query("DELETE FROM CartProduct cp WHERE cp.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("DELETE FROM CartProduct cp WHERE cp.cart.id = :cartId AND cp.isSelected = true")
    void deleteSelectedByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("UPDATE CartProduct cp SET cp.isSelected = :isSelected WHERE cp.cart.id = :cartId")
    void updateAllSelectionByCartId(@Param("cartId") Long cartId, @Param("isSelected") Boolean isSelected);
}
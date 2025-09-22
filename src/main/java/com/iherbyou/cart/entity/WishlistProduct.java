package com.iherbyou.cart.entity;

import com.iherbyou.catalog.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"wishlist_id", "product_id"})},
        indexes = {
            @Index(name = "idx_wishlist_id", columnList = "wishlist_id"), @Index(name = "idx_product_id", columnList = "product_id")
        })
@Entity
public class WishlistProduct { // WishList, Product의 중간 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}

package com.iherbyou.wishlist.entity;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.user.entity.User;
import com.iherbyou.wishlist.constant.WishlistConstants;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistProduct> wishlistProducts = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========== 비즈니스 메서드 ==========

    /**
     * 위시리스트에 상품 추가
     *
     * @param product 추가할 상품
     * @return 추가된 WishlistProduct (중복이면 null)
     * @throws IllegalStateException 최대 개수 초과 시
     */
    public WishlistProduct addProduct(Product product) {
        validateCapacity();

        if (containsProduct(product.getId())) {
            return null; // 중복
        }

        WishlistProduct wishlistProduct = WishlistProduct.builder()
                .wishlist(this)
                .product(product)
                .build();

        wishlistProducts.add(wishlistProduct);
        return wishlistProduct;
    }

    /**
     * 위시리스트에서 상품 제거
     */
    public boolean removeProduct(Long itemId) {
        return wishlistProducts.removeIf(wp -> wp.getId().equals(itemId));
    }

    /**
     * 특정 상품이 위시리스트에 포함되어 있는지 확인
     */
    public boolean containsProduct(Long productId) {
        return wishlistProducts.stream()
                .anyMatch(wp -> wp.getProduct().getId().equals(productId));
    }

    /**
     * 위시리스트 아이템 조회 (최신순, 최대 20개)
     */
    public List<WishlistProduct> getItems(int maxSize) {
        return wishlistProducts.stream()
                .sorted(Comparator.comparing(WishlistProduct::getCreatedAt).reversed())
                .limit(Math.min(maxSize, WishlistConstants.MAX_WISHLIST_SIZE))
                .toList();
    }

    /**
     * 용량 검증
     */
    private void validateCapacity() {
        if (wishlistProducts.size() >= WishlistConstants.MAX_WISHLIST_SIZE) {
            throw new IllegalStateException(
                    String.format("위시리스트는 최대 %d개까지 담을 수 있습니다.",
                            WishlistConstants.MAX_WISHLIST_SIZE)
            );
        }
    }

    /**
     * 정적 팩토리 메서드
     */
    public static Wishlist createForUser(User user) {
        return Wishlist.builder()
                .user(user)
                .wishlistProducts(new ArrayList<>())
                .build();
    }
}
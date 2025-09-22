package com.iherbyou.cart;

import com.iherbyou.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 장바구니 id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)    // 회원 id
    private User user;

    @Column(length = 64, unique = true)
    private String guestToken; // 비회원 식별용 토큰

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0 CHECK (subtotal >= 0)")
    private Integer subTotal; // 소계 금액

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0 CHECK (total_amount >= subtotal)")
    private Integer totalAmount; // 총 금액

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    // 1:N (장바구니 하나에 여러 항목)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

}

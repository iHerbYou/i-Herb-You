package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 장바구니 id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)    // 회원 id
    private User userId;

    @Column(length = 64, unique = true)
    private String guestToken;  // 비회원 식별용 토큰

    @Column(nullable = false,
            columnDefinition = "INT DEFAULT 0 CHECK (subtotal >= 0)")
    private Integer subTotal;   // 소계 금액

    @Column(nullable = false,
            columnDefinition = "BIGINT DEFAULT 0 CHECK (total_amount >= subtotal)")
    private Integer totalAmount;    // 총 금액

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;    // 생성일시

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;    // 수정일시

//     1:N (장바구니 하나에 여러 항목)
    @OneToMany(mappedBy = "cart_product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

}

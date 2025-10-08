package com.iherbyou.cart.entity;

import com.iherbyou.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Check(constraints = "sub_total >= 0 AND total_amount >= sub_total")
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

    @Column(nullable = false)
    private Integer subTotal; // 소계 금액

    @Column(nullable = false)
    private Integer totalAmount; // 총 금액

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    // 1:N (장바구니 하나에 여러 항목)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartProduct> cartProducts = new ArrayList<>();

}

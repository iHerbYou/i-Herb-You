package com.iherbyou.user;

import com.iherbyou.common.Code;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 쿠폰 id

    @Column(length = 100, nullable = false)
    private String name; // 쿠폰명

    @Column(length = 100, nullable = false, unique = true)
    private String code; // 코드

    @Column
    private String description; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code_id", nullable = false) // 할인종류코드 id
    private Code DiscountCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code_id") // 상태코드 id
    private Code statusCode;

    @Column(nullable = false)
    private Double discountValue; // 할인값

    @Column
    private Integer maxDiscountAmount; // 최대 할인 금액

    @Column
    private Integer minOrderAmount; // 최소 주문 금액

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer perUserLimit; // 1인당 사용가능 횟수

    @Column
    private Integer totalIssuable;  // 총 발급 가능 수량

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer issuedCount; // 발급된 수량

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean stackable; // 중복 사용 가능 여부

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean combinableWithOthers; // 타 프로모션 중복 가능 여부

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isFirstPurchaseOnly; // 첫 구매 전용 여부

    @Column
    private LocalDateTime startsAt; // 유효시작일시

    @Column
    private LocalDateTime couponEndsAt; // 종료일시

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

}

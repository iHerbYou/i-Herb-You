package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 쿠폰 id

    @Column(length = 100, nullable = false)
    private String name;    // 쿠폰명

    @Column(length = 100, nullable = false, unique = true)
    private String code;    // 코드

    @Column(length = 255)
    private String description; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discountTypeId", nullable = false)  // 할인종류코드 id
    private Code DiscountCode;

    @Column(nullable = false)
    private Double discountValue;   // 할인값

    @Column
    private Integer maxDiscountAmount;  // 최대 할인 금액

    @Column
    private Integer minOrderAmount; // 최소 주문 금액

    @Column
    private Integer perUserLimit = 1;   // 1인당 사용가능 횟수

    @Column
    private Integer totalIssuable;  // 총 발급 가능 수량

    @Column
    private Integer issuedCount = 0;    // 발급된 수량

    @Column
    private boolean stackable = false;  // 중복 사용 가능 여부

    @Column
    private boolean combinableWithOthers = false;   // 타 프로모션 중복 가능 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicableScopeId") //  적용 범위 코드 id
    private Code ApplicableScopeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id") //  채널 코드 id
    private Code channelCode;

    @Column
    private boolean isFirstPurchaseOnly = false;    // 첫 구매 전용 여부

    @Column
    private LocalDateTime startsAt;   // 시작 일시

    @Column
    private LocalDateTime couponEndsAt; // 종료 일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id") // 상태코드 id
    private Code statusCode;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;    // 생성일시

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;    // 수정일시
  
}

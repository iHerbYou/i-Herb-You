package com.iherbyou.user.dto.admin;

import com.iherbyou.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AdminUserDetailDto {

    // 기본 정보
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 추가 상세 정보
    private Integer addressCount;           // 등록된 주소 개수
    private Integer totalCouponCount;       // 발급받은 전체 쿠폰 개수
    private Integer availableCouponCount;   // 사용 가능한 쿠폰 개수
    private Integer usedCouponCount;        // 사용한 쿠폰 개수
    private Integer pointBalance;           // 현재 포인트 잔액
    private boolean hasWishlist;            // 위시리스트 여부

    public static AdminUserDetailDto from(User user) {
        // 쿠폰 통계 계산
        int totalCoupons = user.getCoupons() != null ? user.getCoupons().size() : 0;
        int usedCoupons = user.getCoupons() != null ?
                (int) user.getCoupons().stream().filter(uc -> uc.isUsed()).count() : 0;
        int availableCoupons = totalCoupons - usedCoupons;

        // 포인트 잔액
        Integer pointBalance = user.getPoint() != null ? user.getPoint().getBalance() : 0;

        return AdminUserDetailDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRoleName())
                .status(user.getStatusName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addressCount(user.getAddresses() != null ? user.getAddresses().size() : 0)
                .totalCouponCount(totalCoupons)
                .availableCouponCount(availableCoupons)
                .usedCouponCount(usedCoupons)
                .pointBalance(pointBalance)
                .hasWishlist(user.getWishlist() != null)
                .build();
    }

}
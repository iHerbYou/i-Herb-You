package com.iherbyou.ordering.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderCreateDto {
    private Long userId;          // 임시: 인증 붙으면 토큰에서 추출
    private String customsInfo;   // 개인통관고유부호
    private Integer deliveryFee;  // 생략 시 서비스에서 정책 계산(5만원 이상 무료)
    private Integer discount;     // 생략 시 0

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item {
        private Long productVariantId;
        private Integer qty;
        private Integer unitPrice;     // 임시(프론트 전달). 실제론 서버 가격 재조회 권장
        private Integer regularPrice;  // 선택
    }
    
}
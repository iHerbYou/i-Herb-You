package com.iherbyou.ordering.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderCreateDto {
    @Size(max = 50)
    private String customsInfo;   // 개인통관고유부호

    @PositiveOrZero
    private Integer deliveryFee;  // 생략 시 서비스에서 정책 계산(5만원 이상 무료)

    @PositiveOrZero
    private Integer discount;     // 생략 시 0

    @Builder.Default
    @NotEmpty
    @Valid
    private List<Item> items = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        @NotNull
        private Long productVariantId;

        @NotNull
        @Positive
        private Integer qty;

        @NotNull
        @Positive
        private Integer unitPrice;     // TODO: 서버 가격 재검증 로직 도입 후 제거/대체 검토

        @PositiveOrZero
        private Integer regularPrice;  // 선택
    }
}

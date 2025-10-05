package com.iherbyou.promotion.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsePointsRequest {

    @NotNull
    private Long userId;

    @Min(0)
    private int amount;
}

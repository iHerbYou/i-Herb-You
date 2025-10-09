package com.iherbyou.promotion.point.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsePointsRequest {

    @Min(0)
    private int amount;
}

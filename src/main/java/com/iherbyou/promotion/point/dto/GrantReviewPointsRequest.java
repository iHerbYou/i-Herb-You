package com.iherbyou.promotion.point.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GrantReviewPointsRequest {

    @NotNull
    private Long userId;

    private boolean containsImage;
}

package com.iherbyou.ordering.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryStatusUpdateRequest {

    @NotNull
    private Integer statusCodeValue;

    private String memo;

    private LocalDateTime completeAt;
}

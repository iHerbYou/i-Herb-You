package com.iherbyou.catalog.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StockDto {

    private Integer amount;          // 재고 수량 (Stock.amount)
    private LocalDateTime restockedAt; // 입고일 (Stock.restockedAt)

}

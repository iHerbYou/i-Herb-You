package com.iherbyou.catalog.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductVariantDto {

    private String variantName;      // 옵션명 (120정, 60정 등)
    private Integer listPrice;       // 정가
    private Integer salePrice;       // 판매가
    private Integer volume;          // 부피 (ml 등)
    private String upcCode;          // UPC 코드
    private String pillSize;         // 알약 크기 (5mm x 3mm x 7mm)
    private String nutritionFacts;   // 영양 성분 정보
    private Integer maxQtyPerOrder;  // 구매 한도
    private LocalDateTime restockEta;// 재입고 예정일
    private StockDto stock;          // 재고 정보 (연관관계)

}

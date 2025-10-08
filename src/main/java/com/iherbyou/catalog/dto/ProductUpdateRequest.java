package com.iherbyou.catalog.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductUpdateRequest {

    private String name;                 // 상품명 (Product.name)
    private String productCode;          // 상품 코드 (Product.code)
    private String description;          // 상품 설명 (Product.description)
    private String instruction;          // 사용법 (Product.instruction)
    private String ingredients;          // 성분 정보 (Product.ingredients)
    private String cautions;             // 주의사항 (Product.cautions)
    private String disclaimer;           // 면책사항 (Product.disclaimer)
    private LocalDateTime saleStartDate;    // 판매 시작일 (Product.saleStartDate)
    private Integer expirationDate;      // 소비기한 (Product.expirationDate)
    private Integer maxQtyPerOrder;      // 최대구매한도 (Product.maxQtyPerOrder)
    private Long brandId;                // 브랜드 FK (Brand.id)
    private List<Long> categoryIds;      // 카테고리 FK (ProductCategory.category.id)
    private List<ProductImgDto> productImgs;    // ProductImg 연결
    private List<ProductVariantDto> variants;   // 옵션 + 재고

}

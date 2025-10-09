package com.iherbyou.catalog.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductImgDto {

    private String imageUrl;  // 이미지 URL (ProductImg.imageUrl)
    private String altText;     // 사진 설명 (ProductImg.altText)
    private Integer sortIdx;    // 정렬 순서 (ProductImg.sortIdx)
    private boolean isPrimary; // 대표 이미지 여부 (ProductImg.isPrimary)

}

package com.iherbyou.banner.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddBannerRequest {
    @NotBlank(message = "배너 이름은 필수입니다.")
    private String bannerName;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String imageUrl;

    @NotNull(message = "정렬 순서는 필수입니다.")
    @Min(value = 1, message = "정렬 순서는 1 이상이어야 합니다.")
    private Integer sortOrder;
}
package com.iherbyou.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressUpdateRequestDto {

    @NotBlank
    @Size(max = 20)
    private String recipient;

    @NotBlank
    @Size(max = 30)
    private String phone;

    @NotBlank
    @Size(max = 10)
    private String zipcode;

    @NotBlank
    @Size(max = 200)
    private String address;

    @Size(max = 200)
    private String addressDetail;

    private Boolean isDefault;
}

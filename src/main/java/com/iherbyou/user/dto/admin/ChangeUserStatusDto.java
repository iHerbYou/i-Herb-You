package com.iherbyou.user.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 관리자가 사용자의 상태를 변경할 때 사용 (Request DTO)
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ChangeUserStatusDto {

    @NotNull
    private Integer statusCode; // 712: ACTIVE, 712: SUSPENDED
    private String reason; // 정지/복구 사유

}

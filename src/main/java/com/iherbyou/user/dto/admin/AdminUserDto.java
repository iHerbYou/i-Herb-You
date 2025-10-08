package com.iherbyou.user.dto.admin;

import com.iherbyou.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AdminUserDto {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;          // 권한 (USER, ADMIN)
    private String status;        // 상태 (ACTIVE, DELETED 등)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AdminUserDto from(User user) {
        return AdminUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRoleName())
                .status(user.getStatusName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
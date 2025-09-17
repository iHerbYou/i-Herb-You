package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_code_id", "provider_user_id"})})
@Entity
public class LoginCredential { // 1:다 = user:LoginCredential

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_code_id", nullable = false)
    private Code providerCode; // kakao, naver, google

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId; // 소셜 로그인에서 발급받는 userId

    @Column
    private String email;

}

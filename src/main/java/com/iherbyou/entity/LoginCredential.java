package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class LoginCredential { // 1:다 = user:LoginCredential

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_code_id")
    private Code providerCode;

    @Column(nullable = false, unique = true)
    private String providerUserId; // 소셜 로그인에서 발급받는 userId

    @Column
    private String email;
}

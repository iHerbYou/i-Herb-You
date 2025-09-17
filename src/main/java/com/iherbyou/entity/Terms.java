package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "terms", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTerms> userTermsList = new ArrayList<>();

    @Column(nullable = false, length = 20)
    private String type; // 약관 종류 (SERVICE, PRIVACY, GUIDE 등)

    @Column(nullable = false, length = 20)
    private String version; // 약관 버전 (예: v1.0, v1.1)

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contentHtml; // 약관 본문 (HTML 가능)

    @Column(nullable = false)
    private LocalDate effectiveDate;  // 시행일

    @Column(nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;  // 생성일시
}

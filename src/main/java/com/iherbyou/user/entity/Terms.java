package com.iherbyou.user.entity;

import com.iherbyou.common.code.entity.Code;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Terms { // 약관들

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code_id")
    private Code typeCode; // 약관 종류 (SERVICE, PRIVACY, GUIDE 등)

    @Column(nullable = false, length = 20)
    private String version; // 약관 버전 (예: v1.0, v1.1)

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contentHtml; // 약관 본문 (HTML 가능)

    @Column(nullable = false)
    private LocalDate effectiveDate; // 시행일

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 생성일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 일시
}

package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 코드 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group_id", nullable = false)
    private CodeGroup codeGroup; // 코드 그룹 ID (FK)

    @Column(length = 50, nullable = false)
    private String codeKey; // 내부 식별 키

    @Column(length = 100, nullable = false)
    private String displayName; // 화면 표기명

    @Column
    private String description; // 설명/툴팁

    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer sortOrder; // 정렬

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive; // 사용 여부

    @Column
    private LocalDateTime validFrom; // 유효 시작

    @Column
    private LocalDateTime validTo; // 유효 종료

    @CreationTimestamp //@CreatedDate, // @CreatedBy : 생성한 사람이 누구야
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 시각

}

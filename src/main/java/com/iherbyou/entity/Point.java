package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Point { // 현재 잔액과 생성/수정 일자 저장 (포인트 내역은 PointHistory 에서 관리)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer balance; // 포인트 잔액

    @CreatedDate // TODO
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // TODO
    private LocalDateTime updatedAt;

}

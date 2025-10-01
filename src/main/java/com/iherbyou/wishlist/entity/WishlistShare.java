package com.iherbyou.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * 위시리스트 공유 엔티티
 * 공유 링크 생성 시점의 위시리스트 스냅샷을 저장
 * 위시리스트가 변경되어도 공유되는 위시리스트는 변경되지 않음
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(indexes = {
        @Index(name = "idx_share_id", columnList = "shareId"),
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_expires_at", columnList = "expiresAt")
})
@Entity
public class WishlistShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String shareId; // "wsh_abc12345"

    @Column(nullable = false)
    private Long userId; // 공유를 생성한 사용자

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    /**
     * 공유 시점의 위시리스트 스냅샷 (JSON)
     * 원본 위시리스트 변경에 영향받지 않음
     */
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String snapshotJson;

    /**
     * 만료 여부 확인
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
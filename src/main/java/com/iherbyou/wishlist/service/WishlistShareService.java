package com.iherbyou.wishlist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iherbyou.exception.wishlist.WishlistException;
import com.iherbyou.wishlist.constant.WishlistShareConstants;
import com.iherbyou.wishlist.dto.response.CreateWishlistShareResponse;
import com.iherbyou.wishlist.dto.response.SharedWishlistResponse;
import com.iherbyou.wishlist.dto.response.WishlistItemResponse;
import com.iherbyou.wishlist.dto.response.WishlistResponse;
import com.iherbyou.wishlist.entity.WishlistShare;
import com.iherbyou.wishlist.repository.WishlistShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 위시리스트 공유 서비스
 *
 * 주요 기능:
 * - 공유 링크 생성 (72시간 유효)
 * - 공유 링크 조회 (스냅샷 기반, 읽기 전용)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistShareService {

    private final WishlistService wishlistService;
    private final WishlistShareRepository wishlistShareRepository;
    private final ObjectMapper objectMapper;

    /**
     * 위시리스트 공유 링크 생성
     *
     * 동작 방식:
     * 1. 현재 사용자의 전체 위시리스트 조회 (최대 20개)
     * 2. 조회한 데이터를 JSON으로 직렬화하여 스냅샷 저장
     * 3. 72시간 유효한 공유 ID 생성 및 DB 저장
     * 4. 단축 URL과 만료 시간 반환
     *
     * @param userId 공유하려는 사용자 ID
     * @return 공유 링크 정보 (shareId, shortUrl, expiresAt)
     * @throws WishlistException 공유할 아이템이 없는 경우
     */
    @Transactional
    public CreateWishlistShareResponse createShare(Long userId) {
        // 1. 전체 위시리스트 조회
        WishlistResponse currentWishlist = wishlistService.getMyWishlist(userId);
        List<WishlistItemResponse> items = currentWishlist.getItems();

        validateHasItemsToShare(items);

        // 2. 스냅샷 생성 (JSON 직렬화)
        WishlistResponse snapshot = createSnapshot(items);
        String snapshotJson = serializeSnapshot(snapshot);

        // 3. 공유 엔티티 생성
        Instant now = Instant.now();
        Instant expiresAt = calculateExpirationTime(now);
        String shareId = generateShareId();

        WishlistShare wishlistShare = WishlistShare.builder()
                .shareId(shareId)
                .userId(userId)
                .expiresAt(expiresAt)
                .snapshotJson(snapshotJson)
                .build();

        // 4. DB 저장
        wishlistShareRepository.save(wishlistShare);

        log.info("Wishlist share created. userId={}, shareId={}, expiresAt={}",
                userId, shareId, expiresAt);

        // 5. 응답 생성
        return CreateWishlistShareResponse.builder()
                .shareId(shareId)
                .shareUrl(buildShareUrl(shareId))
                .expiresAt(formatInstant(expiresAt))
                .build();
    }

    /**
     * 공유된 위시리스트 조회
     *
     * @param shareId 공유 ID
     * @return 공유된 위시리스트 정보 (읽기 전용)
     * @throws WishlistException 공유 링크가 없거나 만료된 경우
     */
    @Transactional(readOnly = true)
    public SharedWishlistResponse viewShare(String shareId) {
        WishlistShare wishlistShare = getShareOrThrow(shareId);

        validateNotExpired(wishlistShare);

        // JSON 역직렬화
        WishlistResponse snapshot = deserializeSnapshot(wishlistShare.getSnapshotJson());

        log.debug("Wishlist share viewed. shareId={}", shareId);

        return SharedWishlistResponse.builder()
                .shareId(shareId)
                .readonly(true)
                .createdAt(formatInstant(wishlistShare.getCreatedAt()))
                .expiresAt(formatInstant(wishlistShare.getExpiresAt()))
                .wishlist(snapshot)
                .build();
    }

    /**
     * 만료된 공유 링크 일괄 삭제 (스케줄러에서 호출)
     */
    @Transactional
    public void deleteExpiredShares() {
        wishlistShareRepository.deleteExpiredShares(Instant.now());
        log.info("Expired wishlist shares deleted");
    }

    // ========== Private Helper Methods ==========

    private void validateHasItemsToShare(List<WishlistItemResponse> items) {
        if (items == null || items.isEmpty()) {
            throw new WishlistException(
                    "NO_ITEMS_TO_SHARE",
                    WishlistShareConstants.ErrorMessage.NO_ITEMS_TO_SHARE
            );
        }
    }

    private WishlistResponse createSnapshot(List<WishlistItemResponse> items) {
        return WishlistResponse.builder()
                .items(List.copyOf(items))
                .count(items.size())
                .build();
    }

    private String serializeSnapshot(WishlistResponse snapshot) {
        try {
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize wishlist snapshot", e);
            throw new WishlistException("SERIALIZATION_ERROR", "스냅샷 생성에 실패했습니다.");
        }
    }

    private WishlistResponse deserializeSnapshot(String snapshotJson) {
        try {
            return objectMapper.readValue(snapshotJson, WishlistResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize wishlist snapshot", e);
            throw new WishlistException("DESERIALIZATION_ERROR", "스냅샷 조회에 실패했습니다.");
        }
    }

    private Instant calculateExpirationTime(Instant from) {
        return from.plusSeconds(WishlistShareConstants.SHARE_EXPIRE_HOURS * 3600L);
    }

    private String generateShareId() {
        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, WishlistShareConstants.SHARE_ID_LENGTH);

        return WishlistShareConstants.SHARE_ID_PREFIX + uuid;
    }

    private String buildShareUrl(String shareId) {
        return WishlistShareConstants.SHARE_URL_HOST + shareId;
    }

    private WishlistShare getShareOrThrow(String shareId) {
        return wishlistShareRepository.findByShareId(shareId)
                .orElseThrow(() -> {
                    log.warn("Share not found. shareId={}", shareId);
                    return new WishlistException(
                            "SHARE_NOT_FOUND",
                            WishlistShareConstants.ErrorMessage.SHARE_NOT_FOUND
                    );
                });
    }

    private void validateNotExpired(WishlistShare wishlistShare) {
        if (wishlistShare.isExpired()) {
            log.warn("Share expired. shareId={}, expiresAt={}",
                    wishlistShare.getShareId(), wishlistShare.getExpiresAt());
            throw new WishlistException(
                    "SHARE_EXPIRED",
                    WishlistShareConstants.ErrorMessage.SHARE_EXPIRED
            );
        }
    }

    private String formatInstant(Instant instant) {
        return DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneOffset.UTC)
                .format(instant);
    }
}
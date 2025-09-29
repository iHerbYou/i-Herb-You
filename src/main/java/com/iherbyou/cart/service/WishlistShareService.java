package com.iherbyou.cart.service;

import com.iherbyou.cart.dto.CreateWishlistShareResponse;
import com.iherbyou.cart.dto.SharedWishlistResponse;
import com.iherbyou.cart.dto.WishlistItemResponse;
import com.iherbyou.cart.dto.WishlistPageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.iherbyou.security.auth.UserPrincipal;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WishlistShareService {

    private static final int EXPIRE_HOURS = 72;
    private static final int MAX_ITEMS = 20; // 페이지네이션 없음 → 최대 20개
    private static final String SHORT_HOST = "https://iherbyou.store/s/";

    private final WishlistService wishlistService;

    // 메모리 저장(임시). 이후 JPA로 교체 가능
    private static final Map<String, ShareSnapshot> STORE = new ConcurrentHashMap<>();
    // 간단한 멱등 처리: 같은 키로 요청 시 동일 shareId 재사용
    private static final Map<String, String> IDEMPOTENCY = new ConcurrentHashMap<>();

    public WishlistShareService(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * 공유 생성:
     * - 사용자의 위시리스트(최대 20개) 전체를 스냅샷
     * - 멱등키가 있으면 기존 shareId 재사용
     */
    public CreateWishlistShareResponse createShare(String idempotencyKey) {
        int expireHours =  EXPIRE_HOURS;
        Long userId = currentUserId();

        // 멱등 재사용
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String existingShareId = IDEMPOTENCY.get(idempotencyKey);
            if (existingShareId != null) {
                ShareSnapshot ss = STORE.get(existingShareId);
                if (ss != null) {
                    return CreateWishlistShareResponse.builder()
                            .shareId(existingShareId)
                            .shortUrl(SHORT_HOST + existingShareId)
                            .expiresAt(toIsoZ(ss.expiresAt()))
                            .build();
                }
            }
        }

        // 전체 위시리스트(최대 20개) 스냅샷
        WishlistPageResponse fullPage = wishlistService.getMyWishlist(userId, MAX_ITEMS);
        List<WishlistItemResponse> allItems = (fullPage.getItems() == null) ? List.of() : fullPage.getItems();

        if (allItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공유할 위시리스트 아이템이 없습니다.");
        }

        WishlistPageResponse pageSnapshot = WishlistPageResponse.builder()
                .items(allItems)
                .count(allItems.size())
                .build();

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expireHours * 3600L);
        String shareId = "wsh_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        STORE.put(shareId, new ShareSnapshot(now, expiresAt, pageSnapshot));

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            IDEMPOTENCY.put(idempotencyKey, shareId);
        }

        return CreateWishlistShareResponse.builder()
                .shareId(shareId)
                .shortUrl(SHORT_HOST + shareId)
                .expiresAt(toIsoZ(expiresAt))
                .build();
    }

    /**
     * 공개 보기:
     * - 스냅샷 확인 + 만료 검증 후 SharedWishlistResponse(page 포함)로 반환
     */
    public SharedWishlistResponse viewShare(String shareId) {
        ShareSnapshot ss = STORE.get(shareId);
        if (ss == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공유 링크가 존재하지 않습니다.");
        }
        if (Instant.now().isAfter(ss.expiresAt())) {
            throw new ResponseStatusException(HttpStatus.GONE, "공유 링크가 만료되었습니다.");
        }

        return SharedWishlistResponse.builder()
                .shareId(shareId)
                .readonly(true)
                .createdAt(toIsoZ(ss.createdAt()))
                .expiresAt(toIsoZ(ss.expiresAt()))
                .page(ss.page())
                .build();
    }

    private static String toIsoZ(Instant t) {
        return DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).format(t);
    }

    /**
     * 현재 사용자 ID 추출 (SecurityContext만 사용)
     */
    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserPrincipal up) {
                return up.getId();
            }
            if (principal instanceof String s) {
                try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "인증 정보가 유효하지 않습니다.");
    }

    private record ShareSnapshot(Instant createdAt, Instant expiresAt, WishlistPageResponse page) {}
}
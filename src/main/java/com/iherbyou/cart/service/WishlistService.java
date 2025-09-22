package com.iherbyou.cart.service;

import com.iherbyou.cart.dto.AddWishlistItemResponse;
import com.iherbyou.cart.dto.WishlistItemResponse;
import com.iherbyou.cart.dto.WishlistPageResponse;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.cart.entity.Wishlist;
import com.iherbyou.cart.entity.WishlistProduct;
//import com.iherbyou.catalog.repository.ProductRepository;
import com.iherbyou.cart.repository.WishlistProductRepository;
import com.iherbyou.cart.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Wishlist 유스케이스 서비스 레이어 스켈레톤
 * - 조회: 커서 페이징 (id DESC + id < cursor)
 * - 추가: 위시리스트 자동 생성, 중복 방지(no-op), 유니크 충돌 경합 처리
 * - 삭제: 소유권 검증(내 위시리스트 범위에서만 삭제)
 */
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepo;
    private final WishlistProductRepository itemRepo;
//    private final ProductRepository productRepo; // 상품 존재 검증 및 로딩

    // ---------------------------------------------------------------------
    // 1) 내 위시리스트 조회 (커서 페이징)
    // ---------------------------------------------------------------------
    @Transactional(readOnly = true)
    public WishlistPageResponse getMyWishlist(Long userId, Long cursor, int limit) {
        // 1) 내 위시리스트 ID 확보 (없으면 빈 목록 반환)
        Long wishlistId = wishlistRepo.findIdByUserId(userId).orElse(null);
        if (wishlistId == null) {
            return WishlistPageResponse.builder()
                    .items(List.of())
                    .nextCursor(null)
                    .hasNext(false)
                    .count(0)
                    .build();
        }

        // 2) 페이징: id DESC 정렬, 1~50 범위 방어
        int pageSize = Math.min(Math.max(limit, 1), 50);
        // Sorting is now based on createdAt (added date)
        PageRequest pr = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 3) 커서 조건 + fetch join으로 N+1 방지
        LocalDateTime cursorTs = (cursor == null) ? null : LocalDateTime.ofEpochSecond(cursor, 0, ZoneOffset.UTC);
        List<WishlistProduct> rows = itemRepo.findPage(wishlistId, cursorTs, pr);

        // 4) DTO 매핑
        List<WishlistItemResponse> items = rows.stream()
                .map(this::toItemResponse)
                .toList();

        Long nextCursor = rows.isEmpty() ? null : rows.get(rows.size() - 1).getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC);
        boolean hasNext = rows.size() == pageSize; // 간단 추정

        return WishlistPageResponse.builder()
                .items(items)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .count(items.size())
                .build();
    }

    // ---------------------------------------------------------------------
    // 2) 아이템 추가 (중복 no-op 정책)
    // ---------------------------------------------------------------------
//    @Transactional
//    public AddWishlistItemResponse addItem(Long userId, Long productId) {
//        // 1) 위시리스트 없으면 생성(최초 한번)
//        Wishlist wl = wishlistRepo.findByUserId(userId)
//                .orElseGet(() -> wishlistRepo.save(Wishlist.create(userId)));
//
//        // 2) 상품 존재 검증 (판매상태/노출정책 등은 프로젝트 정책에 맞춰 확장)
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new DomainException("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."));
//
//        // 3) 중복 방지: 사전 exists 체크 + 유니크 위반 경합 대응
//        if (itemRepo.existsByWishlistIdAndProductId(wl.getId(), product.getId())) {
//            return AddWishlistItemResponse.builder()
//                    .itemId(null)
//                    .duplicated(true)
//                    .build();
//        }
//
//        try {
//            // 스냅샷 필드가 있다면 여기서 product의 이름/썸네일 등을 복사
//            WishlistProduct saved = itemRepo.save(WishlistProduct.of(wl, product));
//            return AddWishlistItemResponse.builder()
//                    .itemId(saved.getId())
//                    .duplicated(false)
//                    .build();
//        } catch (DataIntegrityViolationException e) {
//            // 경합으로 유니크(wishlist_id, product_id) 위반 시 no-op 응답
//            return AddWishlistItemResponse.builder()
//                    .itemId(null)
//                    .duplicated(true)
//                    .build();
//        }
//    }

    // ---------------------------------------------------------------------
    // 3) 아이템 단건 삭제 (내 위시리스트 범위 한정)
    // ---------------------------------------------------------------------
    @Transactional
    public void removeItem(Long userId, Long itemId) {
        Long wishlistId = wishlistRepo.findIdByUserId(userId)
                .orElseThrow(() -> new DomainException("WISHLIST_NOT_FOUND", "위시리스트가 없습니다."));

        int deleted = itemRepo.deleteByIdAndWishlistId(itemId, wishlistId);
        if (deleted == 0) {
            throw new DomainException("WISHLIST_ITEM_NOT_FOUND", "해당 아이템이 없거나 권한이 없습니다.");
        }
    }

    // ---------------------------------------------------------------------
    // 4) 아이템 벌크 삭제 (선택)
    // ---------------------------------------------------------------------
    @Transactional
    public int bulkRemove(Long userId, List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) return 0;

        Long wishlistId = wishlistRepo.findIdByUserId(userId)
                .orElseThrow(() -> new DomainException("WISHLIST_NOT_FOUND", "위시리스트가 없습니다."));

        return itemRepo.deleteAllByIdInAndWishlistId(itemIds, wishlistId);
    }

    // ---------------------------------------------------------------------
    // 내부 매핑 헬퍼
    // ---------------------------------------------------------------------
    private WishlistItemResponse toItemResponse(WishlistProduct e) {
        return WishlistItemResponse.builder()
                .itemId(e.getId())
                .productId(e.getProduct().getId())
                // 스냅샷 컬럼을 사용한다면 아래 주석을 교체하세요.
                // .productName(e.getProductNameSnapshot())
                // .thumbnailUrl(e.getThumbUrlSnapshot())
                // .createdAt(e.getCreatedAt())
                .build();
    }

    // ---------------------------------------------------------------------
    // 도메인 예외 (실전에서는 @ControllerAdvice로 공통 매핑 권장)
    // ---------------------------------------------------------------------
    public static class DomainException extends RuntimeException {
        private final String code;
        public DomainException(String code, String message) {
            super(message);
            this.code = code;
        }
        public String getCode() { return code; }
    }
}

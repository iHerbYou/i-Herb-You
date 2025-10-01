package com.iherbyou.wishlist.service;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.repository.ProductRepository;
import com.iherbyou.exception.wishlist.WishlistException;
import com.iherbyou.user.entity.User;
import com.iherbyou.wishlist.constant.WishlistConstants;
import com.iherbyou.wishlist.dto.response.AddWishlistItemResponse;
import com.iherbyou.wishlist.dto.response.WishlistResponse;
import com.iherbyou.wishlist.entity.Wishlist;
import com.iherbyou.wishlist.entity.WishlistProduct;
import com.iherbyou.wishlist.mapper.WishlistMapper;
import com.iherbyou.wishlist.repository.WishlistRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 위시리스트 비즈니스 로직 서비스
 *
 * Repository 의존성 최소화:
 * - WishlistRepository: 메인 aggregate root
 * - ProductRepository: 상품 검증용
 *
 * 주요 기능:
 * - 위시리스트 조회 (최대 20개, 생성일 내림차순)
 * - 상품 추가 (자동 위시리스트 생성, 중복 방지, 최대 개수 제한)
 * - 상품 삭제 (소유권 검증)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final WishlistMapper mapper;

    /**
     * 사용자의 위시리스트 조회
     *
     * @param userId 사용자 ID
     * @return 위시리스트 응답 (최대 20개)
     */
    @Transactional(readOnly = true)
    public WishlistResponse getMyWishlist(Long userId) {
        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserIdWithProducts(userId);

        if (wishlistOpt.isEmpty()) {
            log.debug("User {} has no wishlist", userId);
            return mapper.toEmptyResponse();
        }

        Wishlist wishlist = wishlistOpt.get();
        List<WishlistProduct> items = wishlist.getItems(WishlistConstants.MAX_WISHLIST_SIZE);

        return mapper.toResponse(items);
    }

    /**
     * 위시리스트에 상품 추가
     *
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 추가 결과 (중복 여부, 메시지)
     * @throws WishlistException 위시리스트가 가득 찼거나 상품을 찾을 수 없는 경우
     */
    @Transactional
    public AddWishlistItemResponse addItem(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        Product product = validateAndGetProduct(productId);

        return addProductToWishlist(wishlist, product);
    }

    /**
     * 위시리스트에서 상품 제거
     *
     * @param userId 사용자 ID
     * @param itemId 위시리스트 아이템 ID
     * @throws WishlistException 위시리스트나 아이템을 찾을 수 없는 경우
     */
    @Transactional
    public void removeItem(Long userId, Long itemId) {
        Wishlist wishlist = getWishlistOrThrow(userId);

        boolean removed = wishlist.removeProduct(itemId);

        if (!removed) {
            log.warn("Failed to delete wishlist item. userId={}, itemId={}", userId, itemId);
            throw new WishlistException(
                    WishlistConstants.ErrorCode.WISHLIST_ITEM_NOT_FOUND,
                    WishlistConstants.ErrorMessage.WISHLIST_ITEM_NOT_FOUND
            );
        }

        wishlistRepository.save(wishlist);
        log.info("Wishlist item removed. userId={}, itemId={}", userId, itemId);
    }

    // ========== Private Helper Methods ==========

    private Wishlist getOrCreateWishlist(Long userId) {
        return wishlistRepository.findByUserIdWithProducts(userId)  // 이걸로 변경
                .orElseGet(() -> createNewWishlist(userId));
    }

    private Wishlist createNewWishlist(Long userId) {
        User userRef = entityManager.getReference(User.class, userId); // EntityManager 사용 (DB 조회 안 함)

        Wishlist newWishlist = Wishlist.createForUser(userRef);
        Wishlist saved = wishlistRepository.save(newWishlist);

        log.info("New wishlist created for user {}", userId);
        return saved;
    }

    private Wishlist getWishlistOrThrow(Long userId) {
        return wishlistRepository.findByUserIdWithProducts(userId)
                .orElseThrow(() -> {
                    log.warn("Wishlist not found for user {}", userId);
                    return new WishlistException(
                            WishlistConstants.ErrorCode.WISHLIST_NOT_FOUND,
                            WishlistConstants.ErrorMessage.WISHLIST_NOT_FOUND
                    );
                });
    }

    private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found. productId={}", productId);
                    return new WishlistException(
                            WishlistConstants.ErrorCode.PRODUCT_NOT_FOUND,
                            WishlistConstants.ErrorMessage.PRODUCT_NOT_FOUND
                    );
                });
    }

    private AddWishlistItemResponse addProductToWishlist(Wishlist wishlist, Product product) {
        try {
            WishlistProduct added = wishlist.addProduct(product);

            if (added == null) {
                // 중복
                log.debug("Product already in wishlist. wishlistId={}, productId={}",
                        wishlist.getId(), product.getId());
                return mapper.toAddDuplicateResponse();
            }

            wishlistRepository.save(wishlist);

            log.info("Product added to wishlist. wishlistId={}, productId={}, itemId={}",
                    wishlist.getId(), product.getId(), added.getId());

            return mapper.toAddSuccessResponse();

        } catch (IllegalStateException e) {
            log.warn("Wishlist is full. wishlistId={}", wishlist.getId());
            throw new WishlistException(
                    WishlistConstants.ErrorCode.WISHLIST_FULL,
                    e.getMessage()
            );

        } catch (DataIntegrityViolationException e) {
            // 동시성 경합으로 인한 중복
            log.warn("Duplicate product addition detected due to race condition. " +
                    "wishlistId={}, productId={}", wishlist.getId(), product.getId());
            return mapper.toAddDuplicateResponse();
        }
    }
}
package com.iherbyou.cart.service;

import com.iherbyou.cart.dto.*;
import com.iherbyou.cart.entity.Cart;
import com.iherbyou.cart.entity.CartProduct;
import com.iherbyou.cart.exception.CartException.*;
import com.iherbyou.cart.repository.CartProductRepository;
import com.iherbyou.cart.repository.CartRepository;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductImg;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.Stock;
import com.iherbyou.catalog.repository.ProductVariantRepository;
import com.iherbyou.catalog.service.ProductService;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 장바구니 서비스
 * 로그인 사용자와 비로그인 게스트 모두의 장바구니를 관리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private static final Integer SHIPPING_FEE = 2500;

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    /**
     * 장바구니 조회
     * 로그인 사용자의 경우 email로, 비로그인 사용자의 경우 guestToken으로 조회합니다.
     *
     * @param email      로그인 사용자 이메일
     * @param guestToken 게스트 토큰
     * @return 장바구니 응답 DTO (상품 목록, 주문 요약, 추천 상품 포함)
     */
    public CartResponseDTO getCart(String email, String guestToken) {
        Cart cart;

        if (email != null) {
            // 로그인 사용자의 장바구니 조회
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cart = cartRepository.findByUser(user).orElse(null);
        } else if (guestToken != null) {
            // 게스트 사용자의 장바구니 조회
            cart = cartRepository.findByGuestToken(guestToken).orElse(null);
        } else {
            // 토큰이 없는 경우 빈 장바구니 반환
            return createEmptyCartResponse(null);
        }

        if (cart == null) {
            return createEmptyCartResponse(guestToken);
        }

        return buildCartResponse(cart);
    }

    /**
     * 장바구니에 상품 추가
     * 이미 동일한 상품이 있으면 수량을 증가시키고, 없으면 새로 추가합니다.
     * 재고 검증을 수행하며, 게스트의 경우 새로운 토큰을 생성할 수 있습니다.
     *
     * @param email      로그인 사용자 이메일
     * @param guestToken 게스트 토큰
     * @param request    추가할 상품 정보 (상품 변형 ID, 수량)
     * @return 장바구니 메시지 응답 (장바구니 ID, 메시지, 게스트 토큰)
     */
    @Transactional
    public CartMessageResponseDTO addToCart(String email, String guestToken, AddToCartRequestDTO request) {
        // 수량 유효성 검증
        if (request.getQty() == null || request.getQty() < 1) {
            throw new InvalidQuantityException();
        }

        // 상품 변형 조회
        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId()).orElseThrow(ProductVariantNotFoundException::new);

        // 재고 검증
        Stock stock = productVariant.getStock();
        if (stock == null || stock.getAmount() == 0) {
            throw new OutOfStockException();
        }
        if (stock.getAmount() < request.getQty()) {
            throw new InsufficientStockException(request.getQty(), stock.getAmount());
        }

        Cart cart;
        String responseGuestToken = null;

        if (email != null) {
            // 로그인 사용자의 장바구니 조회 또는 생성
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cart = cartRepository.findByUser(user).orElseGet(() -> createCart(user, null));
        } else {
            // 게스트 사용자의 장바구니 조회 또는 생성
            if (guestToken == null) {
                guestToken = UUID.randomUUID().toString();
            }
            responseGuestToken = guestToken;
            String finalGuestToken = guestToken;
            cart = cartRepository.findByGuestToken(guestToken).orElseGet(() -> createCart(null, finalGuestToken));
        }

        // 동일한 상품이 이미 장바구니에 있는지 확인
        CartProduct existingCartProduct = cartProductRepository.findByCartAndProductVariant(cart, productVariant).orElse(null);

        if (existingCartProduct != null) {
            // 기존 상품이 있으면 수량 증가
            int newQty = existingCartProduct.getQty() + request.getQty();
            if (stock.getAmount() < newQty) {
                throw new InsufficientStockException(newQty, stock.getAmount());
            }
            updateCartProductQty(existingCartProduct, newQty);
        } else {
            // 새 상품 추가
            CartProduct cartProduct = CartProduct.builder().cart(cart).productVariant(productVariant).qty(request.getQty()).isSelected(true).build();
            cartProductRepository.save(cartProduct);
            cart.getCartProducts().add(cartProduct);
        }

        // 장바구니 금액 재계산
        recalculateCart(cart);

        return CartMessageResponseDTO.builder().cartId(cart.getId()).message("장바구니에 상품이 추가되었습니다.").guestToken(responseGuestToken).build();
    }

    /**
     * 장바구니 상품 수량 변경
     * 재고를 검증한 후 수량을 업데이트하고 금액을 재계산합니다.
     *
     * @param cartProductId 장바구니 상품 ID
     * @param email         로그인 사용자 이메일
     * @param guestToken    게스트 토큰
     * @param request       변경할 수량 정보
     */
    @Transactional
    public void updateCartProductQty(Long cartProductId, String email, String guestToken, UpdateQtyRequestDTO request) {
        // 수량 유효성 검증
        if (request.getQty() == null || request.getQty() < 1) {
            throw new InvalidQuantityException();
        }

        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow(CartProductNotFoundException::new);

        // 접근 권한 검증
        validateCartAccess(cartProduct.getCart(), email, guestToken);

        // 재고 검증
        Stock stock = cartProduct.getProductVariant().getStock();
        if (stock == null || stock.getAmount() == 0) {
            throw new OutOfStockException();
        }
        if (stock.getAmount() < request.getQty()) {
            throw new InsufficientStockException(request.getQty(), stock.getAmount());
        }

        updateCartProductQty(cartProduct, request.getQty());
        recalculateCart(cartProduct.getCart());
    }

    /**
     * 장바구니 상품 선택 상태 변경
     * 개별 상품의 선택 여부를 변경하고 주문 요약을 재계산합니다.
     *
     * @param cartProductId 장바구니 상품 ID
     * @param email         로그인 사용자 이메일
     * @param guestToken    게스트 토큰
     * @param request       선택 상태 정보
     */
    @Transactional
    public void updateCartProductSelection(Long cartProductId, String email, String guestToken, UpdateSelectionRequestDTO request) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow(CartProductNotFoundException::new);

        validateCartAccess(cartProduct.getCart(), email, guestToken);

        updateCartProductSelection(cartProduct, request.getIsSelected());
        recalculateCart(cartProduct.getCart());
    }

    /**
     * 장바구니 전체 상품 선택 상태 변경
     * 모든 상품의 선택 여부를 일괄 변경합니다.
     *
     * @param email      로그인 사용자 이메일
     * @param guestToken 게스트 토큰
     * @param request    선택 상태 정보
     */
    @Transactional
    public void updateAllSelection(String email, String guestToken, UpdateSelectionRequestDTO request) {
        Cart cart = findCartByUserOrGuest(email, guestToken);
        cartProductRepository.updateAllSelectionByCartId(cart.getId(), request.getIsSelected());

        cart = cartRepository.findById(cart.getId()).orElseThrow(CartNotFoundException::new);
        recalculateCart(cart);
    }

    /**
     * 장바구니 상품 삭제 (단일)
     *
     * @param cartProductId 장바구니 상품 ID
     * @param email         로그인 사용자 이메일
     * @param guestToken    게스트 토큰
     */
    @Transactional
    public void deleteCartProduct(Long cartProductId, String email, String guestToken) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow(CartProductNotFoundException::new);

        validateCartAccess(cartProduct.getCart(), email, guestToken);

        Cart cart = cartProduct.getCart();
        cartProductRepository.delete(cartProduct);
        cart.getCartProducts().remove(cartProduct);
        recalculateCart(cart);
    }

    /**
     * 장바구니 상품 전체 삭제
     *
     * @param email      로그인 사용자 이메일
     * @param guestToken 게스트 토큰
     */
    @Transactional
    public void deleteAllCartProducts(String email, String guestToken) {
        Cart cart = findCartByUserOrGuest(email, guestToken);
        cartProductRepository.deleteAllByCartId(cart.getId());
        cart.getCartProducts().clear();
        recalculateCart(cart);
    }

    /**
     * 장바구니 상품 선택 삭제
     * 여러 개의 상품을 한번에 삭제합니다.
     *
     * @param email          로그인 사용자 이메일
     * @param guestToken     게스트 토큰
     * @param cartProductIds 삭제할 상품 ID 목록
     */
    @Transactional
    public void deleteCartProducts(String email, String guestToken, List<Long> cartProductIds) {
        Cart cart = findCartByUserOrGuest(email, guestToken);

        for (Long cartProductId : cartProductIds) {
            CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow(CartProductNotFoundException::new);

            // 권한 확인
            if (!cartProduct.getCart().getId().equals(cart.getId())) {
                throw new UnauthorizedAccessException();
            }

            cartProductRepository.delete(cartProduct);
            cart.getCartProducts().remove(cartProduct);
        }

        recalculateCart(cart);
    }

    /**
     * 게스트 장바구니를 로그인 사용자 장바구니와 병합
     * 로그인 시 비로그인 상태에서 담았던 상품들을 사용자 장바구니로 이동합니다.
     * 동일한 상품이 있으면 수량을 합산하고, 게스트 장바구니는 삭제합니다.
     *
     * @param email      로그인 사용자 이메일
     * @param guestToken 게스트 토큰
     */
    @Transactional
    public void mergeGuestCart(String email, String guestToken) {
        if (guestToken == null) return;

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Cart guestCart = cartRepository.findByGuestToken(guestToken).orElse(null);
        if (guestCart == null) return;

        Cart userCart = cartRepository.findByUser(user).orElseGet(() -> createCart(user, null));

        for (CartProduct guestCartProduct : guestCart.getCartProducts()) {
            CartProduct existingCartProduct = cartProductRepository.findByCartAndProductVariant(userCart, guestCartProduct.getProductVariant()).orElse(null);

            if (existingCartProduct != null) {
                // 동일 상품이 있으면 수량 합산
                int newQty = existingCartProduct.getQty() + guestCartProduct.getQty();
                updateCartProductQty(existingCartProduct, newQty);
            } else {
                // 새 상품 추가
                CartProduct newCartProduct = CartProduct.builder().cart(userCart).productVariant(guestCartProduct.getProductVariant()).qty(guestCartProduct.getQty()).isSelected(guestCartProduct.getIsSelected()).build();
                cartProductRepository.save(newCartProduct);
                userCart.getCartProducts().add(newCartProduct);
            }
        }

        // 게스트 장바구니 삭제
        cartRepository.delete(guestCart);
        recalculateCart(userCart);
    }

    /**
     * 만료된 게스트 장바구니 자동 삭제
     * 매일 자정에 실행되며, 7일 이상 지난 게스트 장바구니를 삭제합니다.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredGuestCarts() {
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(7);
        cartRepository.deleteExpiredGuestCarts(expiryDate);
    }

    /**
     * 비활성 사용자 장바구니 자동 삭제
     * 매일 자정에 실행되며, 일정 기간 사용하지 않은 장바구니를 삭제합니다.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUserCarts() {
        cartRepository.deleteInactiveUserCarts();
    }

    /**
     * 새로운 장바구니 생성
     *
     * @param user       사용자 (로그인 사용자인 경우)
     * @param guestToken 게스트 토큰 (게스트인 경우)
     * @return 생성된 장바구니
     */
    private Cart createCart(User user, String guestToken) {
        Cart cart = Cart.builder().user(user).guestToken(guestToken).subTotal(0).totalAmount(0).cartProducts(new ArrayList<>()).build();
        return cartRepository.save(cart);
    }

    /**
     * 장바구니 상품 수량 업데이트
     */
    private void updateCartProductQty(CartProduct cartProduct, Integer newQty) {
        CartProduct updated = CartProduct.builder().id(cartProduct.getId()).cart(cartProduct.getCart()).productVariant(cartProduct.getProductVariant()).qty(newQty).isSelected(cartProduct.getIsSelected()).build();
        cartProductRepository.save(updated);
    }

    /**
     * 장바구니 상품 선택 상태 업데이트
     */
    private void updateCartProductSelection(CartProduct cartProduct, Boolean isSelected) {
        CartProduct updated = CartProduct.builder().id(cartProduct.getId()).cart(cartProduct.getCart()).productVariant(cartProduct.getProductVariant()).qty(cartProduct.getQty()).isSelected(isSelected).build();
        cartProductRepository.save(updated);
    }

    /**
     * 장바구니 금액 재계산
     * 선택된 상품들의 소계, 배송비, 총액을 계산합니다.
     *
     * @param cart 재계산할 장바구니
     */
    private void recalculateCart(Cart cart) {
        // 선택된 상품들의 소계 계산
        int subTotal = cart.getCartProducts().stream().filter(cp -> cp.getIsSelected() != null && cp.getIsSelected()).mapToInt(cp -> {
            Integer price = cp.getProductVariant().getSalePrice();
            return (price != null ? price : 0) * cp.getQty();
        }).sum();

        // 총액 계산 (소계가 0보다 크면 배송비 추가)
        int totalAmount = subTotal > 0 ? subTotal + SHIPPING_FEE : 0;

        Cart updatedCart = Cart.builder().id(cart.getId()).user(cart.getUser()).guestToken(cart.getGuestToken()).subTotal(subTotal).totalAmount(totalAmount).createdAt(cart.getCreatedAt()).updatedAt(LocalDateTime.now()).cartProducts(cart.getCartProducts()).build();

        cartRepository.save(updatedCart);
    }

    /**
     * 이메일 또는 게스트 토큰으로 장바구니 조회
     */
    private Cart findCartByUserOrGuest(String email, String guestToken) {
        if (email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            return cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);
        } else if (guestToken != null) {
            return cartRepository.findByGuestToken(guestToken).orElseThrow(CartNotFoundException::new);
        } else {
            throw new CartNotFoundException();
        }
    }

    /**
     * 장바구니 접근 권한 검증
     * 로그인 사용자는 본인의 장바구니만, 게스트는 자신의 토큰이 일치하는 장바구니만 접근 가능합니다.
     */
    private void validateCartAccess(Cart cart, String email, String guestToken) {
        if (email != null) {
            if (cart.getUser() == null || !cart.getUser().getEmail().equals(email)) {
                throw new UnauthorizedAccessException();
            }
        } else if (guestToken != null) {
            if (!guestToken.equals(cart.getGuestToken())) {
                throw new UnauthorizedAccessException();
            }
        } else {
            throw new UnauthorizedAccessException();
        }
    }

    /**
     * 빈 장바구니 응답 생성
     * 장바구니가 없거나 상품이 없는 경우 사용됩니다.
     */
    private CartResponseDTO createEmptyCartResponse(String guestToken) {
        return CartResponseDTO.builder().cartId(null).guestToken(guestToken).items(List.of()).summary(OrderSummaryDTO.builder().selectedItemCount(0).subTotal(0).shippingFee(0).totalAmount(0).build()).recommendedProducts(getRecommendedProducts()).build();
    }

    /**
     * 장바구니 응답 DTO 생성
     * 장바구니 정보를 API 응답 형태로 변환합니다.
     */
    private CartResponseDTO buildCartResponse(Cart cart) {
        List<CartItemDTO> items = cart.getCartProducts().stream().map(this::toCartItemDTO).collect(Collectors.toList());

        int selectedItemCount = (int) cart.getCartProducts().stream().filter(cp -> cp.getIsSelected() != null && cp.getIsSelected()).count();

        OrderSummaryDTO summary = OrderSummaryDTO.builder().selectedItemCount(selectedItemCount).subTotal(cart.getSubTotal()).shippingFee(cart.getSubTotal() > 0 ? SHIPPING_FEE : 0).totalAmount(cart.getTotalAmount()).build();

        return CartResponseDTO.builder().cartId(cart.getId()).guestToken(cart.getGuestToken()).items(items).summary(summary).recommendedProducts(getRecommendedProducts()).build();
    }

    /**
     * CartProduct 엔티티를 CartItemDTO로 변환
     * 상품 정보, 이미지, 재고 정보를 포함합니다.
     */
    private CartItemDTO toCartItemDTO(CartProduct cartProduct) {
        ProductVariant variant = cartProduct.getProductVariant();
        Product product = variant.getProduct();
        Stock stock = variant.getStock();

        // 대표 이미지 또는 첫 번째 이미지 선택
        String imageUrl = product.getProductImgs().stream().filter(img -> img.getIsPrimary() != null && img.getIsPrimary()).findFirst().or(() -> product.getProductImgs().stream().min((a, b) -> Integer.compare(a.getSortIdx() != null ? a.getSortIdx() : Integer.MAX_VALUE, b.getSortIdx() != null ? b.getSortIdx() : Integer.MAX_VALUE))).map(ProductImg::getImageUrl).orElse(null);

        Integer stockQuantity = stock != null ? stock.getAmount() : 0;
        Boolean isOutOfStock = stockQuantity == 0;

        return CartItemDTO.builder().cartProductId(cartProduct.getId()).productVariantId(variant.getId()).productId(product.getId()).productName(product.getName()).brandName(product.getBrand().getName()).imageUrl(imageUrl).price(variant.getSalePrice()).qty(cartProduct.getQty()).isSelected(cartProduct.getIsSelected()).stockQuantity(stockQuantity).isOutOfStock(isOutOfStock).build();
    }

    /**
     * 추천 상품 목록 조회
     * 베스트셀러 상품을 추천 상품으로 반환합니다.
     */
    private List<RecommendedProductDTO> getRecommendedProducts() {
        // ProductService의 기존 findBestsellers() 메서드 재사용
        Page<ProductListDto> bestsellersPage = productService.findBestsellers(null, 4);

        return bestsellersPage.getContent().stream().map(dto -> RecommendedProductDTO.builder().productId(dto.getId()).productName(dto.getName()).imageUrl(dto.getThumbnailUrl()).price(dto.getMinPrice()).build()).collect(Collectors.toList());
    }

    /**
     * Product 엔티티를 RecommendedProductDTO로 변환
     */
    private RecommendedProductDTO toRecommendedProductDTO(Product product) {
        String imageUrl = product.getProductImgs().stream().filter(img -> img.getIsPrimary() != null && img.getIsPrimary()).findFirst().or(() -> product.getProductImgs().stream().min((a, b) -> Integer.compare(a.getSortIdx() != null ? a.getSortIdx() : Integer.MAX_VALUE, b.getSortIdx() != null ? b.getSortIdx() : Integer.MAX_VALUE))).map(ProductImg::getImageUrl).orElse(null);

        return RecommendedProductDTO.builder().productId(product.getId()).productName(product.getName()).imageUrl(imageUrl).price(product.getMinPrice()).build();
    }
}
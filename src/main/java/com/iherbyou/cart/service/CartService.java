package com.iherbyou.cart.service;

import com.iherbyou.cart.dto.CartDTO.*;
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

    public CartResponseDTO getCart(String email, String guestToken) {
        Cart cart;

        if (email != null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cart = cartRepository.findByUser(user).orElse(null);
        } else if (guestToken != null) {
            cart = cartRepository.findByGuestToken(guestToken).orElse(null);
        } else {
            return createEmptyCartResponse(null);
        }

        if (cart == null) {
            return createEmptyCartResponse(guestToken);
        }

        return buildCartResponse(cart);
    }

    @Transactional
    public CartMessageResponseDTO addToCart(String email, String guestToken, AddToCartRequestDTO request) {
        if (request.getQty() == null || request.getQty() < 1) {
            throw new InvalidQuantityException();
        }

        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(ProductVariantNotFoundException::new);

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
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            cart = cartRepository.findByUser(user)
                    .orElseGet(() -> createCart(user, null));
        } else {
            if (guestToken == null) {
                guestToken = UUID.randomUUID().toString();
            }
            responseGuestToken = guestToken;
            String finalGuestToken = guestToken;
            cart = cartRepository.findByGuestToken(guestToken)
                    .orElseGet(() -> createCart(null, finalGuestToken));
        }

        CartProduct existingCartProduct = cartProductRepository
                .findByCartAndProductVariant(cart, productVariant)
                .orElse(null);

        if (existingCartProduct != null) {
            int newQty = existingCartProduct.getQty() + request.getQty();
            if (stock.getAmount() < newQty) {
                throw new InsufficientStockException(newQty, stock.getAmount());
            }
            updateCartProductQty(existingCartProduct, newQty);
        } else {
            CartProduct cartProduct = CartProduct.builder()
                    .cart(cart)
                    .productVariant(productVariant)
                    .qty(request.getQty())
                    .isSelected(true)
                    .build();
            cartProductRepository.save(cartProduct);
            cart.getCartProducts().add(cartProduct);
        }

        recalculateCart(cart);

        return CartMessageResponseDTO.builder()
                .cartId(cart.getId())
                .message("장바구니에 상품이 추가되었습니다.")
                .guestToken(responseGuestToken)
                .build();
    }

    @Transactional
    public void updateCartProductQty(Long cartProductId, String email, String guestToken, UpdateQtyRequestDTO request) {
        if (request.getQty() == null || request.getQty() < 1) {
            throw new InvalidQuantityException();
        }

        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(CartProductNotFoundException::new);

        validateCartAccess(cartProduct.getCart(), email, guestToken);

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

    @Transactional
    public void updateCartProductSelection(Long cartProductId, String email, String guestToken, UpdateSelectionRequestDTO request) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(CartProductNotFoundException::new);

        validateCartAccess(cartProduct.getCart(), email, guestToken);

        updateCartProductSelection(cartProduct, request.getIsSelected());
        recalculateCart(cartProduct.getCart());
    }

    @Transactional
    public void updateAllSelection(String email, String guestToken, UpdateSelectionRequestDTO request) {
        Cart cart = findCartByUserOrGuest(email, guestToken);
        cartProductRepository.updateAllSelectionByCartId(cart.getId(), request.getIsSelected());

        cart = cartRepository.findById(cart.getId())
                .orElseThrow(CartNotFoundException::new);
        recalculateCart(cart);
    }

    @Transactional
    public void deleteCartProduct(Long cartProductId, String email, String guestToken) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(CartProductNotFoundException::new);

        validateCartAccess(cartProduct.getCart(), email, guestToken);

        Cart cart = cartProduct.getCart();
        cartProductRepository.delete(cartProduct);
        cart.getCartProducts().remove(cartProduct);
        recalculateCart(cart);
    }

    @Transactional
    public void deleteAllCartProducts(String email, String guestToken) {
        Cart cart = findCartByUserOrGuest(email, guestToken);
        cartProductRepository.deleteAllByCartId(cart.getId());
        cart.getCartProducts().clear();
        recalculateCart(cart);
    }

    @Transactional
    public void deleteCartProducts(String email, String guestToken, List<Long> cartProductIds) {
        Cart cart = findCartByUserOrGuest(email, guestToken);

        for (Long cartProductId : cartProductIds) {
            CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                    .orElseThrow(CartProductNotFoundException::new);

            // 권한 확인
            if (!cartProduct.getCart().getId().equals(cart.getId())) {
                throw new UnauthorizedAccessException();
            }

            cartProductRepository.delete(cartProduct);
            cart.getCartProducts().remove(cartProduct);
        }

        recalculateCart(cart);
    }

    @Transactional
    public void mergeGuestCart(String email, String guestToken) {
        if (guestToken == null) return;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Cart guestCart = cartRepository.findByGuestToken(guestToken).orElse(null);
        if (guestCart == null) return;

        Cart userCart = cartRepository.findByUser(user)
                .orElseGet(() -> createCart(user, null));

        for (CartProduct guestCartProduct : guestCart.getCartProducts()) {
            CartProduct existingCartProduct = cartProductRepository
                    .findByCartAndProductVariant(userCart, guestCartProduct.getProductVariant())
                    .orElse(null);

            if (existingCartProduct != null) {
                int newQty = existingCartProduct.getQty() + guestCartProduct.getQty();
                updateCartProductQty(existingCartProduct, newQty);
            } else {
                CartProduct newCartProduct = CartProduct.builder()
                        .cart(userCart)
                        .productVariant(guestCartProduct.getProductVariant())
                        .qty(guestCartProduct.getQty())
                        .isSelected(guestCartProduct.getIsSelected())
                        .build();
                cartProductRepository.save(newCartProduct);
                userCart.getCartProducts().add(newCartProduct);
            }
        }

        cartRepository.delete(guestCart);
        recalculateCart(userCart);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredGuestCarts() {
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(7);
        cartRepository.deleteExpiredGuestCarts(expiryDate);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUserCarts() {
        cartRepository.deleteInactiveUserCarts();
    }

    private Cart createCart(User user, String guestToken) {
        Cart cart = Cart.builder()
                .user(user)
                .guestToken(guestToken)
                .subTotal(0)
                .totalAmount(0)
                .cartProducts(new ArrayList<>())  // 빈 리스트로 초기화 ✅
                .build();
        return cartRepository.save(cart);
    }

    private void updateCartProductQty(CartProduct cartProduct, Integer newQty) {
        CartProduct updated = CartProduct.builder()
                .id(cartProduct.getId())
                .cart(cartProduct.getCart())
                .productVariant(cartProduct.getProductVariant())
                .qty(newQty)
                .isSelected(cartProduct.getIsSelected())
                .build();
        cartProductRepository.save(updated);
    }

    private void updateCartProductSelection(CartProduct cartProduct, Boolean isSelected) {
        CartProduct updated = CartProduct.builder()
                .id(cartProduct.getId())
                .cart(cartProduct.getCart())
                .productVariant(cartProduct.getProductVariant())
                .qty(cartProduct.getQty())
                .isSelected(isSelected)
                .build();
        cartProductRepository.save(updated);
    }

    private void recalculateCart(Cart cart) {
        int subTotal = cart.getCartProducts().stream()
                .filter(cp -> cp.getIsSelected() != null && cp.getIsSelected())
                .mapToInt(cp -> {
                    Integer price = cp.getProductVariant().getSalePrice();
                    return (price != null ? price : 0) * cp.getQty();
                })
                .sum();

        int totalAmount = subTotal > 0 ? subTotal + SHIPPING_FEE : 0;

        Cart updatedCart = Cart.builder()
                .id(cart.getId())
                .user(cart.getUser())
                .guestToken(cart.getGuestToken())
                .subTotal(subTotal)
                .totalAmount(totalAmount)
                .createdAt(cart.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .cartProducts(cart.getCartProducts())
                .build();

        cartRepository.save(updatedCart);
    }

    private Cart findCartByUserOrGuest(String email, String guestToken) {
        if (email != null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            return cartRepository.findByUser(user)
                    .orElseThrow(CartNotFoundException::new);
        } else if (guestToken != null) {
            return cartRepository.findByGuestToken(guestToken)
                    .orElseThrow(CartNotFoundException::new);
        } else {
            throw new CartNotFoundException();
        }
    }

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

    private CartResponseDTO createEmptyCartResponse(String guestToken) {
        return CartResponseDTO.builder()
                .cartId(null)
                .guestToken(guestToken)
                .items(List.of())
                .summary(OrderSummaryDTO.builder()
                        .selectedItemCount(0)
                        .subTotal(0)
                        .shippingFee(0)
                        .totalAmount(0)
                        .build())
                .recommendedProducts(getRecommendedProducts())
                .build();
    }

    private CartResponseDTO buildCartResponse(Cart cart) {
        List<CartItemDTO> items = cart.getCartProducts().stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());

        int selectedItemCount = (int) cart.getCartProducts().stream()
                .filter(cp -> cp.getIsSelected() != null && cp.getIsSelected())
                .count();

        OrderSummaryDTO summary = OrderSummaryDTO.builder()
                .selectedItemCount(selectedItemCount)
                .subTotal(cart.getSubTotal())
                .shippingFee(cart.getSubTotal() > 0 ? SHIPPING_FEE : 0)
                .totalAmount(cart.getTotalAmount())
                .build();

        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .guestToken(cart.getGuestToken())
                .items(items)
                .summary(summary)
                .recommendedProducts(getRecommendedProducts())
                .build();
    }

    private CartItemDTO toCartItemDTO(CartProduct cartProduct) {
        ProductVariant variant = cartProduct.getProductVariant();
        Product product = variant.getProduct();
        Stock stock = variant.getStock();

        String imageUrl = product.getProductImgs().stream()
                .filter(img -> img.getIsPrimary() != null && img.getIsPrimary())
                .findFirst()
                .or(() -> product.getProductImgs().stream()
                        .min((a, b) -> Integer.compare(
                                a.getSortIdx() != null ? a.getSortIdx() : Integer.MAX_VALUE,
                                b.getSortIdx() != null ? b.getSortIdx() : Integer.MAX_VALUE)))
                .map(ProductImg::getImageUrl)
                .orElse(null);

        Integer stockQuantity = stock != null ? stock.getAmount() : 0;
        Boolean isOutOfStock = stockQuantity == 0;

        return CartItemDTO.builder()
                .cartProductId(cartProduct.getId())
                .productVariantId(variant.getId())
                .productId(product.getId())
                .productName(product.getName())
                .brandName(product.getBrand().getName())
                .imageUrl(imageUrl)
                .price(variant.getSalePrice())
                .qty(cartProduct.getQty())
                .isSelected(cartProduct.getIsSelected())
                .stockQuantity(stockQuantity)
                .isOutOfStock(isOutOfStock)
                .build();
    }

    private List<RecommendedProductDTO> getRecommendedProducts() {
        // ProductService의 기존 findBestsellers() 메서드 재사용
        Page<ProductListDto> bestsellersPage = productService.findBestsellers(null, 4);

        return bestsellersPage.getContent().stream()
                .map(dto -> RecommendedProductDTO.builder()
                        .productId(dto.getId())
                        .productName(dto.getName())
                        .imageUrl(dto.getThumbnailUrl())  // getThumbnailUrl() 사용
                        .price(dto.getMinPrice())
                        .build())
                .collect(Collectors.toList());
    }

    private RecommendedProductDTO toRecommendedProductDTO(Product product) {
        String imageUrl = product.getProductImgs().stream()
                .filter(img -> img.getIsPrimary() != null && img.getIsPrimary())
                .findFirst()
                .or(() -> product.getProductImgs().stream()
                        .min((a, b) -> Integer.compare(
                                a.getSortIdx() != null ? a.getSortIdx() : Integer.MAX_VALUE,
                                b.getSortIdx() != null ? b.getSortIdx() : Integer.MAX_VALUE)))
                .map(ProductImg::getImageUrl)
                .orElse(null);

        return RecommendedProductDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .imageUrl(imageUrl)
                .price(product.getMinPrice())
                .build();
    }
}

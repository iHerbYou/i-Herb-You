package com.iherbyou.cart.dto;

import lombok.*;

import java.util.List;

public class CartDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long cartProductId;
        private Long productVariantId;
        private Long productId;
        private String productName;
        private String brandName;
        private String imageUrl;
        private Integer price;
        private Integer qty;
        private Boolean isSelected;
        private Integer stockQuantity;
        private Boolean isOutOfStock;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSummaryDTO {
        private Integer selectedItemCount;
        private Integer subTotal;
        private Integer shippingFee;
        private Integer totalAmount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedProductDTO {
        private Long productId;
        private String productName;
        private String imageUrl;
        private Integer price;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartResponseDTO {
        private Long cartId;
        private String guestToken;
        private List<CartItemDTO> items;
        private OrderSummaryDTO summary;
        private List<RecommendedProductDTO> recommendedProducts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddToCartRequestDTO {
        private Long productVariantId;
        private Integer qty;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateQtyRequestDTO {
        private Integer qty;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSelectionRequestDTO {
        private Boolean isSelected;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartMessageResponseDTO {
        private Long cartId;
        private String message;
        private String guestToken;
    }
}
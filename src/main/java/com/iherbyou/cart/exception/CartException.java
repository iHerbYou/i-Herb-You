package com.iherbyou.cart.exception;

public class CartException extends RuntimeException {

    public CartException(String message) {
        super(message);
    }

    public static class CartNotFoundException extends CartException {
        public CartNotFoundException() {
            super("장바구니를 찾을 수 없습니다.");
        }
    }

    public static class CartProductNotFoundException extends CartException {
        public CartProductNotFoundException() {
            super("장바구니에서 해당 상품을 찾을 수 없습니다.");
        }
    }

    public static class InsufficientStockException extends CartException {
        public InsufficientStockException(Integer requested, Integer available) {
            super(String.format("재고가 부족합니다. (요청: %d, 재고: %d)", requested, available));
        }
    }

    public static class OutOfStockException extends CartException {
        public OutOfStockException() {
            super("품절된 상품입니다.");
        }
    }

    public static class ProductVariantNotFoundException extends CartException {
        public ProductVariantNotFoundException() {
            super("상품 옵션을 찾을 수 없습니다.");
        }
    }

    public static class InvalidQuantityException extends CartException {
        public InvalidQuantityException() {
            super("수량은 1개 이상이어야 합니다.");
        }
    }

    public static class UnauthorizedAccessException extends CartException {
        public UnauthorizedAccessException() {
            super("해당 장바구니에 접근 권한이 없습니다.");
        }
    }
}
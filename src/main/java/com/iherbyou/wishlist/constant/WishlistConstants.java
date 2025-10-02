package com.iherbyou.wishlist.constant;

/**
 * 위시리스트 관련 상수 정의
 */
public final class WishlistConstants {

    private WishlistConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    /**
     * 위시리스트 최대 보관 개수
     */
    public static final int MAX_WISHLIST_SIZE = 20;

    /**
     * 에러 코드
     */
    public static final class ErrorCode {
        public static final String WISHLIST_NOT_FOUND = "WISHLIST_NOT_FOUND";
        public static final String WISHLIST_FULL = "WISHLIST_FULL";
        public static final String WISHLIST_ITEM_NOT_FOUND = "WISHLIST_ITEM_NOT_FOUND";
        public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    }

    /**
     * 에러 메시지
     */
    public static final class ErrorMessage {
        public static final String WISHLIST_NOT_FOUND = "위시리스트가 없습니다.";
        public static final String WISHLIST_FULL = "위시리스트는 최대 %d개까지 담을 수 있습니다.";
        public static final String WISHLIST_ITEM_NOT_FOUND = "해당 아이템이 존재하지 않습니다.";
        public static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    }
}
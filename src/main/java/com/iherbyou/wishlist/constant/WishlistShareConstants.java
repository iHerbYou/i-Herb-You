package com.iherbyou.wishlist.constant;

/**
 * 위시리스트 공유 관련 상수
 */
public final class WishlistShareConstants {

    private WishlistShareConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    /**
     * 공유 링크 만료 시간 (시간 단위)
     */
    public static final int SHARE_EXPIRE_HOURS = 72;

    /**
     * 공유 링크 호스트
     */
    public static final String SHARE_URL_HOST = "https://iherbyou.store/s/";

    /**
     * 공유 ID 접두사
     */
    public static final String SHARE_ID_PREFIX = "wsh_";

    /**
     * 공유 ID 랜덤 길이
     */
    public static final int SHARE_ID_LENGTH = 8;

    /**
     * 에러 메시지
     */
    public static final class ErrorMessage {
        public static final String NO_ITEMS_TO_SHARE = "공유할 위시리스트 아이템이 없습니다.";
        public static final String SHARE_NOT_FOUND = "공유 링크가 존재하지 않습니다.";
        public static final String SHARE_EXPIRED = "공유 링크가 만료되었습니다.";
    }
}
package com.iherbyou.wishlist.scheduler;

import com.iherbyou.wishlist.service.WishlistShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WishlistShareScheduler {

    private final WishlistShareService wishlistShareService;

    /**
     * 매일 새벽 3시에 만료된 공유 링크 삭제
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredShares() {
        log.info("Starting cleanup of expired wishlist shares");
        wishlistShareService.deleteExpiredShares();
    }
}
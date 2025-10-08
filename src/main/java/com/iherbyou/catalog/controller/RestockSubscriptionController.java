package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.RestockSubscriptionDto;
import com.iherbyou.catalog.service.RestockSubscriptionService;
import com.iherbyou.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/catalog/restock-subscriptions")
@RequiredArgsConstructor
public class RestockSubscriptionController {

    private final RestockSubscriptionService restockSubscriptionService;

    // 재입고 알림 등록 API
    @PostMapping
    public ResponseEntity<RestockSubscriptionDto> subscribe(
            @RequestParam Long variantId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info("재입고알림 신청 요청 - userId: {}, variantId: {}",
                userPrincipal != null ? userPrincipal.getId() : "null", variantId);

        if (userPrincipal == null) {
            log.error("UserPrincipal이 null입니다. 인증이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            RestockSubscriptionDto dto = restockSubscriptionService.subscribe(userPrincipal.getId(), variantId);
            log.info("재입고알림 신청 성공 - subscriptionId: {}", dto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            log.error("재입고알림 신청 실패 - userId: {}, variantId: {}, error: {}",
                    userPrincipal.getId(), variantId, e.getMessage(), e);
            throw e; // 에러를 다시 던져서 GlobalExceptionHandler가 처리하도록
        }
    }

    // 재입고 알림 비활성 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsubscribe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        restockSubscriptionService.unsubscribe(userPrincipal.getId(), id);

        return ResponseEntity.noContent().build();
    }

}

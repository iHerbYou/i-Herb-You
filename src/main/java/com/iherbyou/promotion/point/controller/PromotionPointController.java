package com.iherbyou.promotion.point.controller;

import com.iherbyou.promotion.point.dto.GrantOrderCompletionPointsRequest;
import com.iherbyou.promotion.point.dto.GrantReviewPointsRequest;
import com.iherbyou.promotion.point.dto.PointBalanceDto;
import com.iherbyou.promotion.point.dto.PointHistoryItemDto;
import com.iherbyou.promotion.point.dto.UsePointsRequest;
import com.iherbyou.promotion.point.service.PromotionPointFacade;
import com.iherbyou.user.entity.PointHistory;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromotionPointController {

    private final PromotionPointFacade promotionPointFacade;

    @PostMapping("/reviews/{reviewId}/points")
    public ResponseEntity<PointBalanceDto> grantReviewPoints(@AuthenticationPrincipal UserPrincipal principal,
                                                             @PathVariable Long reviewId,
                                                             @Valid @RequestBody GrantReviewPointsRequest request) {
        Long principalId = requirePrincipal(principal);
        Optional<PointHistory> history = promotionPointFacade.grantReviewPoints(principalId, reviewId, request.isContainsImage());
        int balance = history.map(PointHistory::getBalanceAfter)
                .orElseGet(() -> promotionPointFacade.currentBalance(principalId));
        return ResponseEntity.ok(PointBalanceDto.of(principalId, balance));
    }

    @PostMapping("/orders/{orderId}/points/earn")
    public ResponseEntity<PointBalanceDto> grantOrderCompletionPoints(@AuthenticationPrincipal UserPrincipal principal,
                                                                      @PathVariable Long orderId,
                                                                      @Valid @RequestBody GrantOrderCompletionPointsRequest request) {
        Long principalId = requirePrincipal(principal);
        Optional<PointHistory> history = promotionPointFacade.grantOrderCompletionPoints(principalId, orderId, request.getPaymentAmount());
        int balance = history.map(PointHistory::getBalanceAfter)
                .orElseGet(() -> promotionPointFacade.currentBalance(principalId));
        return ResponseEntity.ok(PointBalanceDto.of(principalId, balance));
    }

    @PostMapping("/orders/{orderId}/points/use")
    public ResponseEntity<PointBalanceDto> usePoints(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long orderId,
                                                     @Valid @RequestBody UsePointsRequest request) {
        Long principalId = requirePrincipal(principal);
        try {
            promotionPointFacade.usePoints(principalId, orderId, request.getAmount());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
        int balance = promotionPointFacade.currentBalance(principalId);
        return ResponseEntity.ok(PointBalanceDto.of(principalId, balance));
    }

    @PostMapping("/orders/{orderId}/points/restore")
    public ResponseEntity<PointBalanceDto> restorePoints(@AuthenticationPrincipal UserPrincipal principal,
                                                         @PathVariable Long orderId,
                                                         @Valid @RequestBody UsePointsRequest request) {
        Long principalId = requirePrincipal(principal);
        promotionPointFacade.restorePoints(principalId, orderId, request.getAmount());
        int balance = promotionPointFacade.currentBalance(principalId);
        return ResponseEntity.ok(PointBalanceDto.of(principalId, balance));
    }

    @PostMapping("/internal/points/expire")
    public ResponseEntity<Void> expirePoints() {
        promotionPointFacade.expirePoints();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/points")
    public PointBalanceDto getPointBalance(@AuthenticationPrincipal UserPrincipal principal) {
        Long principalId = requirePrincipal(principal);
        int balance = promotionPointFacade.currentBalance(principalId);
        return PointBalanceDto.of(principalId, balance);
    }

    @GetMapping("/users/me/points/history")
    public List<PointHistoryItemDto> getPointHistory(@AuthenticationPrincipal UserPrincipal principal) {
        Long principalId = requirePrincipal(principal);
        List<PointHistory> histories = promotionPointFacade.getHistories(principalId);
        return histories.stream()
                .map(PointHistoryItemDto::from)
                .toList();
    }

    private Long requirePrincipal(UserPrincipal principal) {
        if (principal == null || principal.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authentication required");
        }
        return principal.getId();
    }

}

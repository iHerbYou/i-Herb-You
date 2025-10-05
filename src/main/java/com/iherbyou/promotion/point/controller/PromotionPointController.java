package com.iherbyou.promotion.point.controller;

import com.iherbyou.promotion.point.dto.GrantOrderCompletionPointsRequest;
import com.iherbyou.promotion.point.dto.GrantReviewPointsRequest;
import com.iherbyou.promotion.point.dto.PointBalanceDto;
import com.iherbyou.promotion.point.dto.PointHistoryItemDto;
import com.iherbyou.promotion.point.dto.UsePointsRequest;
import com.iherbyou.promotion.point.service.PromotionPointFacade;
import com.iherbyou.user.entity.PointHistory;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromotionPointController {

    private final PromotionPointFacade promotionPointFacade;

    @PostMapping("/reviews/{reviewId}/points")
    public ResponseEntity<PointBalanceDto> grantReviewPoints(@PathVariable Long reviewId,
                                                             @Valid @RequestBody GrantReviewPointsRequest request) {
        Optional<PointHistory> history = promotionPointFacade.grantReviewPoints(request.getUserId(), reviewId, request.isContainsImage());
        int balance = history.map(PointHistory::getBalanceAfter)
                .orElseGet(() -> promotionPointFacade.currentBalance(request.getUserId()));
        return ResponseEntity.ok(PointBalanceDto.of(request.getUserId(), balance));
    }

    @PostMapping("/orders/{orderId}/points/earn")
    public ResponseEntity<PointBalanceDto> grantOrderCompletionPoints(@PathVariable Long orderId,
                                                                      @Valid @RequestBody GrantOrderCompletionPointsRequest request) {
        Optional<PointHistory> history = promotionPointFacade.grantOrderCompletionPoints(request.getUserId(), orderId, request.getPaymentAmount());
        int balance = history.map(PointHistory::getBalanceAfter)
                .orElseGet(() -> promotionPointFacade.currentBalance(request.getUserId()));
        return ResponseEntity.ok(PointBalanceDto.of(request.getUserId(), balance));
    }

    @PostMapping("/orders/{orderId}/points/use")
    public ResponseEntity<PointBalanceDto> usePoints(@PathVariable Long orderId,
                                                     @Valid @RequestBody UsePointsRequest request) {
        try {
            promotionPointFacade.usePoints(request.getUserId(), orderId, request.getAmount());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
        int balance = promotionPointFacade.currentBalance(request.getUserId());
        return ResponseEntity.ok(PointBalanceDto.of(request.getUserId(), balance));
    }

    @PostMapping("/orders/{orderId}/points/restore")
    public ResponseEntity<PointBalanceDto> restorePoints(@PathVariable Long orderId,
                                                         @Valid @RequestBody UsePointsRequest request) {
        promotionPointFacade.restorePoints(request.getUserId(), orderId, request.getAmount());
        int balance = promotionPointFacade.currentBalance(request.getUserId());
        return ResponseEntity.ok(PointBalanceDto.of(request.getUserId(), balance));
    }

    @PostMapping("/internal/points/expire")
    public ResponseEntity<Void> expirePoints() {
        promotionPointFacade.expirePoints();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/points")
    public PointBalanceDto getPointBalance(@PathVariable Long userId) {
        int balance = promotionPointFacade.currentBalance(userId);
        return PointBalanceDto.of(userId, balance);
    }

    @GetMapping("/users/{userId}/points/history")
    public List<PointHistoryItemDto> getPointHistory(@PathVariable Long userId) {
        List<PointHistory> histories = promotionPointFacade.getHistories(userId);
        return histories.stream()
                .map(PointHistoryItemDto::from)
                .toList();
    }
}

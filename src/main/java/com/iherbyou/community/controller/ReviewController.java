package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.service.ReviewService;
import com.iherbyou.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "id", "rating");

    @PostMapping
    public ResponseEntity<ReviewProduct> createReview(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody ReviewCreateRequest req
    ) {
        Review saved = reviewService.createReview(
                me.getId(),
                req.productId(),
                req.rating(),
                req.text()
        );

        ReviewProduct res = new ReviewProduct(
                saved.getId(),
                saved.getRating(),
                saved.getText(),
                saved.getUser().getName(),
                saved.getCreatedAt().format(ISO)
        );

        return ResponseEntity.created(URI.create("/api/reviews/" + saved.getId()))
                .body(res);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewProduct>> listByProduct(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort, ALLOWED_SORTS);
        Page<Review> result = reviewService.listByProduct(productId, pageable);
        Page<ReviewProduct> body = result.map(r -> new ReviewProduct(
                r.getId(), r.getRating(), r.getText(), r.getUser().getName(), r.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummary> summary(@RequestParam Long productId) {
        long total = reviewService.countByProduct(productId);
        double avg = reviewService.averageRating(productId);
        return ResponseEntity.ok(new ReviewSummary(total, avg));
    }

    private Pageable buildPageable(int page, int size, String sortParam, Set<String> allowedSorts) {
        int p = Math.max(0, page);
        int s = Math.max(1, size);

        String[] sp = sortParam.split(",", 2);
        String prop = sp[0];
        String dirStr = sp.length > 1 ? sp[1] : "desc";
        Sort.Direction dir = "asc".equalsIgnoreCase(dirStr) ? Sort.Direction.ASC : Sort.Direction.DESC;

        String safeProp = allowedSorts.contains(prop) ? prop : "createdAt";
        return PageRequest.of(p, s, Sort.by(dir, safeProp));
    }
}

package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 질문 등록
    // Header: X-USER-ID (로그인 사용자 ID)
    // Body: QnaQuestionCreateRequest { productId, title, content }
    // Response: QnaQuestionProduct (생성된 질문 요약. answers는 빈 리스트/카운트 0)
    // 리뷰: 엔티티 직접 노출 X, DTO로 최소 필드만 반환
    @PostMapping("/questions")
    public ResponseEntity<QnaQuestionProduct> createQuestion(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody QnaQuestionCreateRequest req
    ) {
        QnaQuestion saved = qnaService.createQuestion(userId, req.productId(), req.title(), req.content());
        QnaQuestionProduct res = new QnaQuestionProduct(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getUser().getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO),
                Collections.emptyList(), // 생성 직후에는 답변 없음
                0
        );
        return ResponseEntity.ok(res);
    }

    // 상품별 질문 + (해당 질문의) 답변 목록
    // Query: productId (필수), statusCodeId(선택: 노출 정책), pageable
    // Response: Page<QnaQuestionProduct> (각 질문에 answers 포함, answerCount 포함)
    // 리뷰: 질문/답변 API 따로? -> 한 번에 내려주도록 통합
    @GetMapping("/questions")
    public ResponseEntity<Page<QnaQuestionProduct>> listByProduct(
            @RequestParam Long productId,
            @RequestParam(required = false) Integer statusCodeId,
            Pageable pageable
    ) {
        Page<QnaQuestion> page = qnaService.listByProduct(productId, statusCodeId, pageable);
        Page<QnaQuestionProduct> body = page.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                q.getQnaAnswers().stream() // 필요 시 .limit(2)로 프리뷰만 내려도 됨(응답 축소)
                        .map(a -> new QnaAnswerProduct(
                                a.getId(),
                                a.getUser().getId(),
                                a.getContent(),
                                a.getCreatedAt().format(ISO)
                        ))
                        .toList(),
                q.getQnaAnswers().size()
        ));
        return ResponseEntity.ok(body);
    }

    // 내가 쓴 질문 목록
    // Header: X-USER-ID
    // Response: Page<QnaQuestionProduct>
    // 답 축소: 내 질문 목록에서는 answers를 내려주지 않음
    @GetMapping("/my")
    public ResponseEntity<Page<QnaQuestionProduct>> myQuestions(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        Page<QnaQuestion> page = qnaService.listMyQuestions(userId, pageable);
        Page<QnaQuestionProduct> body = page.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                null, // 목록 응답 축소: answers 미포함
                0
        ));
        return ResponseEntity.ok(body);
    }

    // 답변 등록 (관리자권한 필요)
    // Header: X-USER-ID, X-IS-ADMIN (true여야 등록 가능)
    // Body: QnaAnswerCreateRequest { questionId, content }
    // Response: QnaAnswerProduct (생성된 답변 요약)
    // 리뷰: actorId 파라미터 제거 -> Header 통일
    @PostMapping("/answers")
    public ResponseEntity<QnaAnswerProduct> createAnswer(
            @RequestHeader("X-USER-ID") Long actorId,
            @RequestHeader(name = "X-IS-ADMIN", defaultValue = "false") boolean isAdmin,
            @RequestBody QnaAnswerCreateRequest req
    ) {
        QnaAnswer saved = qnaService.createAnswer(actorId, req.questionId(), req.content(), isAdmin);
        QnaAnswerProduct res = new QnaAnswerProduct(
                saved.getId(),
                saved.getUser().getId(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.ok(res);
    }

    // 답변 삭제 (작성자 본인 또는 관리자)
    // Header: X-USER-ID, X-IS-ADMIN
    // Path: /answers/{id}
    // Response: 204 No Content
    @DeleteMapping("/answers/{id}")
    public ResponseEntity<Void> deleteAnswer(
            @RequestHeader("X-USER-ID") Long actorId,
            @RequestHeader(name = "X-IS-ADMIN", defaultValue = "false") boolean isAdmin,
            @PathVariable Long id
    ) {
        qnaService.deleteAnswer(actorId, id, isAdmin);
        return ResponseEntity.noContent().build();
    }
}

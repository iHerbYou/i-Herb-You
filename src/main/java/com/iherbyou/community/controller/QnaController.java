package com.iherbyou.community.controller;

import com.iherbyou.community.dto.*;
import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;

import com.iherbyou.security.auth.UserPrincipal;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 질문 등록: 201 + Location, answers 제외
    @PostMapping("/questions")
    public ResponseEntity<QnaQuestionCreated> createQuestion(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaQuestionCreateRequest req
    ) {
        QnaQuestion saved = qnaService.createQuestion(me.getId(), req.productId(), req.title(), req.content());

        QnaQuestionCreated body = new QnaQuestionCreated(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getUser().getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO),
                "질문이 등록되었습니다."
        );

        URI location = URI.create("/api/qna/questions/" + saved.getId());
        return ResponseEntity.created(location).body(body);
    }

    // 상품별 질문 목록: statusValue(=101/102/103) 필터
    @GetMapping("/questions")
    public ResponseEntity<Page<QnaQuestionProduct>> listByProduct(
            @RequestParam Long productId,
            @RequestParam(required = false, name = "statusValue") Integer statusValue,
            Pageable pageable
    ) {
        Page<QnaQuestion> page = qnaService.listByProduct(productId, statusValue, pageable);

        Page<QnaQuestionProduct> body = page.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                q.getQnaAnswers().stream()
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

    // 내가 쓴 질문 목록: answers=null로 축소
    @GetMapping("/my")
    public ResponseEntity<Page<QnaQuestionProduct>> myQuestions(
            @AuthenticationPrincipal UserPrincipal me,
            Pageable pageable
    ) {
        Page<QnaQuestion> page = qnaService.listMyQuestions(me.getId(), pageable);
        Page<QnaQuestionProduct> body = page.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                null,
                0
        ));
        return ResponseEntity.ok(body);
    }

    // 답변 등록: 201 + Location
    @PostMapping("/answers")
    public ResponseEntity<QnaAnswerProduct> createAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaAnswerCreateRequest req
    ) {
        QnaAnswer saved = qnaService.createAnswer(me.getId(), req.questionId(), req.content());
        QnaAnswerProduct res = new QnaAnswerProduct(
                saved.getId(),
                saved.getUser().getId(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.created(URI.create("/api/qna/answers/" + saved.getId())).body(res);
    }

    // 답변 삭제: 204
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long answerId
    ) {
        qnaService.deleteAnswer(me.getId(), answerId);
        return ResponseEntity.noContent().build();
    }
}

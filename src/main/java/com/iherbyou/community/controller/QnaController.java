package com.iherbyou.community.controller;

import com.iherbyou.community.dto.QnaAnswerCreateRequest;
import com.iherbyou.community.dto.QnaAnswerProduct;
import com.iherbyou.community.dto.QnaQuestionCreateRequest;
import com.iherbyou.community.dto.QnaQuestionCreated;
import com.iherbyou.community.dto.QnaQuestionProduct;
import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.service.QnaService;
import com.iherbyou.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 질문 등록: POST /api/qna
    @PostMapping("/qna")
    public ResponseEntity<QnaQuestionCreated> createQuestion(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaQuestionCreateRequest req
    ) {
        QnaQuestion saved = qnaService.createQuestion(me.getId(), req.productId(), req.title(), req.content());

        QnaQuestionCreated body = new QnaQuestionCreated(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getUser().getId(),
                saved.getUser().getEmail(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO),
                "질문이 등록되었습니다."
        );

        URI location = URI.create("/api/qna/" + saved.getId());
        return ResponseEntity.created(location).body(body);
    }

    // 상품별 질문 목록: GET /api/qna?productId=&statusValue=&page=&size=&sort=
    @GetMapping("/qna")
    public ResponseEntity<Page<QnaQuestionProduct>> listByProduct(
            @RequestParam Long productId,
            @RequestParam(required = false, name = "statusValue") Integer statusValue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort, List.of("createdAt", "id"));
        Page<QnaQuestion> result = qnaService.listByProduct(productId, statusValue, pageable);

        Page<QnaQuestionProduct> body = result.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getUser().getEmail(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                q.getQnaAnswers().stream()
                        .map(a -> new QnaAnswerProduct(
                                a.getId(),
                                a.getUser().getEmail(),
                                a.getContent(),
                                a.getCreatedAt().format(ISO)
                        ))
                        .toList(),
                q.getQnaAnswers().size()
        ));
        return ResponseEntity.ok(body);
    }

    // 내 질문 목록: GET /api/my-qna?page=&size=&sort=
    @GetMapping("/my-qna")
    public ResponseEntity<Page<QnaQuestionProduct>> myQuestions(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort, List.of("createdAt", "id"));
        Page<QnaQuestion> result = qnaService.listMyQuestions(me.getId(), pageable);

        Page<QnaQuestionProduct> body = result.map(q -> new QnaQuestionProduct(
                q.getId(),
                q.getProduct().getId(),
                q.getUser().getId(),
                q.getUser().getEmail(),
                q.getTitle(),
                q.getContent(),
                q.getCreatedAt().format(ISO),
                null,
                0
        ));
        return ResponseEntity.ok(body);
    }

    // 답변 등록: POST /api/qna/answers
    @PostMapping("/qna/answers")
    public ResponseEntity<QnaAnswerProduct> createAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaAnswerCreateRequest req
    ) {
        QnaAnswer saved = qnaService.createAnswer(me.getId(), req.questionId(), req.content());
        QnaAnswerProduct res = new QnaAnswerProduct(
                saved.getId(),
                saved.getUser().getEmail(),
                saved.getContent(),
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.created(URI.create("/api/qna/answers/" + saved.getId())).body(res);
    }

    // 답변 삭제: DELETE /api/qna/answers/{answerId}
    @DeleteMapping("/qna/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long answerId
    ) {
        qnaService.deleteAnswer(me.getId(), answerId);
        return ResponseEntity.noContent().build();
    }

    // 질문(연관 답변 포함) 삭제: DELETE /api/qna/{questionId}
    @DeleteMapping("/qna/{questionId}")
    public ResponseEntity<Void> deleteQuestionCascade(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long questionId
    ) {
        qnaService.deleteQuestion(me.getId(), questionId); // cascade + orphanRemoval 로 답변도 함께 삭제
        return ResponseEntity.noContent().build();
    }

    // 공통 정렬/페이징 유틸
    private Pageable buildPageable(int page, int size, String sortParam, List<String> allowedSorts) {
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

package com.iherbyou.community.controller;

import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.service.QnaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    // 질문 등록
    @PostMapping("/questions")
    public QnaQuestion createQuestion(@RequestParam Long userId, @RequestParam Long productId,
                                      @RequestParam String title, @RequestParam String content) {
        return qnaService.createQuestion(userId, productId, title, content);
    }

    // 상품별 질문 목록
    @GetMapping("/questions")
    public Page<QnaQuestion> listByProduct(@RequestParam Long productId,
                                           @RequestParam(required = false) Integer statusCodeId,
                                           Pageable pageable) {
        return qnaService.listByProduct(productId, statusCodeId, pageable);
    }

    // 내가 쓴 질문
    @GetMapping("/my")
    public Page<QnaQuestion> myQuestions(@RequestParam Long userId, Pageable pageable) {
        return qnaService.listMyQuestions(userId, pageable);
    }

    // 답변 등록 (관리자)
    @PostMapping("/answers")
    public QnaAnswer createAnswer(@RequestParam Long actorId, @RequestParam Long questionId,
                                  @RequestParam String content,
                                  @RequestParam(defaultValue = "false") boolean isAdmin) {
        return qnaService.createAnswer(actorId, questionId, content, isAdmin);
    }

    // 질문별 답변 목록
    @GetMapping("/answers")
    public List<QnaAnswer> listAnswers(@RequestParam Long questionId) {
        return qnaService.listAnswers(questionId);
    }

    // 답변 삭제 (작성자 or 관리자)
    @DeleteMapping("/answers/{id}")
    public void deleteAnswer(@RequestParam Long actorId, @PathVariable Long id,
                             @RequestParam(defaultValue = "false") boolean isAdmin) {
        qnaService.deleteAnswer(actorId, id, isAdmin);
    }

    // 답변 개수
    @GetMapping("/answers/count")
    public long countAnswers(@RequestParam Long questionId) {
        return qnaService.countAnswers(questionId);
    }
}

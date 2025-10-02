package com.iherbyou.community.controller;

import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.community.dto.QnaAnswerCreateRequest;
import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.service.QnaService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/qna")
@RequiredArgsConstructor
public class AdminQnaController {

    private final QnaService qnaService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 상품별 질문 목록 (statusValue 필터 가능)
    @GetMapping("/questions")
    public Page<QnaQuestion> listQuestions(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam Long productId,
            @RequestParam(required = false, name = "statusValue") Integer statusValue,
            Pageable pageable
    ) {
        ensureAdmin(me);
        return qnaService.listByProduct(productId, statusValue, pageable);
    }

    // 특정 질문의 답변 목록
    @GetMapping("/questions/{questionId}/answers")
    public List<QnaAnswer> listAnswers(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long questionId
    ) {
        ensureAdmin(me);
        return qnaService.listAnswers(questionId);
    }

    // 답변 등록 (관리자)
    @PostMapping("/answers")
    public ResponseEntity<Void> createAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaAnswerCreateRequest req
    ) {
        ensureAdmin(me);
        QnaAnswer saved = qnaService.createAnswer(me.getId(), req.questionId(), req.content());
        return ResponseEntity.created(URI.create("/api/admin/qna/answers/" + saved.getId())).build();
    }

    // 답변 삭제 (관리자)
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long answerId
    ) {
        ensureAdmin(me);
        qnaService.deleteAnswer(me.getId(), answerId);
        return ResponseEntity.noContent().build();
    }

    // === 내부 유틸: 관리자 권한 확인 (토큰 권한 → DB 폴백) ===
    private void ensureAdmin(UserPrincipal me) {
        if (me != null && me.getAuthorities() != null) {
            boolean hasAdmin = me.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> "ROLE_ADMIN".equals(a) || a.endsWith(":ADMIN") || a.contains("ADMIN"));
            if (hasAdmin) return;
        }

        Integer roleValue = em.createQuery(
                        "select rc.value from User u left join u.roleCode rc where u.id = :uid",
                        Integer.class
                )
                .setParameter("uid", me.getId())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (roleValue == null || !codeService.isAdminRole(roleValue)) {
            throw new AccessDeniedException("FORBIDDEN");
        }
    }
}

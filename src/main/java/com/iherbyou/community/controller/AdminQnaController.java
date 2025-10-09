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
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/qna")
@RequiredArgsConstructor
public class AdminQnaController {

    private final QnaService qnaService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "id", "statusCodeId");

    // 상품별 질문 목록 (statusValue 필터 가능)
    @GetMapping("/questions")
    public Page<QnaQuestion> listQuestions(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam Long productId,
            @RequestParam(required = false, name = "statusValue") Integer statusValue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        ensureAdmin(me);
        Pageable pageable = buildPageable(page, size, sort, ALLOWED_SORTS);
        return qnaService.listByProduct(productId, statusValue, pageable);
    }

    // 답변 등록
    @PostMapping("/answers")
    public ResponseEntity<Void> createAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody QnaAnswerCreateRequest req
    ) {
        ensureAdmin(me);
        QnaAnswer saved = qnaService.createAnswer(me.getId(), req.questionId(), req.content());
        return ResponseEntity.created(URI.create("/api/admin/qna/answers/" + saved.getId())).build();
    }

    // 답변 삭제
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long answerId
    ) {
        ensureAdmin(me);
        qnaService.deleteAnswer(me.getId(), answerId);
        return ResponseEntity.noContent().build();
    }

    // ===== 공통: page/size/sort 처리 (화이트리스트 적용) =====
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

    // ===== NPE 방어 포함 관리자 권한 확인 =====
    private void ensureAdmin(UserPrincipal me) {
        // 0) 로그인 자체가 안 되어 있으면 차단
        if (me == null) throw new AccessDeniedException("FORBIDDEN");

        // 1) 토큰 권한에 ADMIN 계열이 있으면 통과
        if (me.getAuthorities() != null) {
            boolean hasAdmin = me.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a != null && ("ROLE_ADMIN".equals(a) || a.endsWith(":ADMIN") || a.contains("ADMIN")));
            if (hasAdmin) return;
        }

        // 2) DB 폴백: role value 조회 후 CodeService로 판별
        Integer roleValue = em.createQuery(
                        "select rc.value from com.iherbyou.user.entity.User u left join u.roleCode rc where u.id = :uid",
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

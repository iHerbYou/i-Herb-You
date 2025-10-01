package com.iherbyou.community.repository;

import com.iherbyou.community.entity.QnaQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaQuestionRepository extends JpaRepository<QnaQuestion, Long> {

    // 1) 상품별 질문 목록 (상품 상세 페이지용)
    // answers까지 읽어오면 컨트롤러에서 N+1 방지됨
    @EntityGraph(attributePaths = {"product", "user", "qnaAnswers", "qnaAnswers.user"})
    Page<QnaQuestion> findByProduct_Id(Long productId, Pageable pageable);

    // 2) 작성자 기준 질문 목록 (마이페이지)
    // 이 API는 answers 내려주지 않으니 product,user만 가져오면 충분
    @EntityGraph(attributePaths = {"product", "user"})
    Page<QnaQuestion> findByUser_Id(Long userId, Pageable pageable);

    // 3) 키워드 검색 (제목+내용) — 현재 안 쓰면 그대로 둬도 OK
    Page<QnaQuestion> findByProduct_IdAndContentContaining(Long productId, String keyword, Pageable pageable);

    // 4) 상태코드 단독 필터 — 사용처가 없으면 유지 or 제거 자유
    Page<QnaQuestion> findByStatusCodeId(Integer statusCodeId, Pageable pageable);

    // 5) 본인 확인용 (수정/삭제 전 권한 검증)
    Optional<QnaQuestion> findByIdAndUser_Id(Long questionId, Long userId);

    // 6) 질문 개수 (상품별 집계)
    long countByProduct_Id(Long productId);

    // 7) 질문 존재 여부 (상품+사용자)
    boolean existsByProduct_IdAndUser_Id(Long productId, Long userId);

    // 8) 상품별 + 상태 필터 (목록용) — answers까지 패치 조인
    @EntityGraph(attributePaths = {"product", "user", "qnaAnswers", "qnaAnswers.user"})
    Page<QnaQuestion> findByProduct_IdAndStatusCodeId(Long productId, Integer statusCodeId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update QnaQuestion q set q.statusCodeId = :status where q.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

}

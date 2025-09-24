package com.iherbyou.community.repository;

import com.iherbyou.community.entity.QnaQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaQuestionRepository extends JpaRepository<QnaQuestion, Long> {

    // 1) 상품별 질문 목록 (상품 상세 페이지용)
    @EntityGraph(attributePaths = {"product", "user"})
    Page<QnaQuestion> findByProduct_Id(Long productId, Pageable pageable);

    // 2) 작성자 기준 질문 목록 (마이페이지)
    @EntityGraph(attributePaths = {"product", "user"})
    Page<QnaQuestion> findByUser_Id(Long userId, Pageable pageable);

    // 3) 키워드 검색 (제목+내용)
    Page<QnaQuestion> findByProduct_IdAndContentContaining(Long productId, String keyword, Pageable pageable);

    // 4) 상태코드 필터 (정수 컬럼일 때)
    Page<QnaQuestion> findByStatusCodeId(Integer statusCodeId, Pageable pageable);

    // 5) 본인 확인용 (수정/삭제 전 권한 검증)
    Optional<QnaQuestion> findByIdAndUser_Id(Long questionId, Long userId);

    // 6) 질문 개수 (상품별 집계, 배지/통계용)
    long countByProduct_Id(Long productId);

    // 7) 질문 존재 여부 (상품+사용자 조합, 구매자 검증 후 QnA 작성 제한)
    boolean existsByProduct_IdAndUser_Id(Long productId, Long userId);

    // 8) 상품별 질문 목록(상태코드 필터 + 페이징)
    Page<QnaQuestion> findByProduct_IdAndStatusCodeId(Long productId, Integer statusCodeId, Pageable pageable);
}

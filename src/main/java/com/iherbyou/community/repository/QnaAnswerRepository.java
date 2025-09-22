package com.iherbyou.community.repository;

import com.iherbyou.community.entity.QnaAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {

    // 1) 질문별 답변 목록 (+ N+1 방지: user까지 로딩)
    @EntityGraph(attributePaths = {"user"})
    List<QnaAnswer> findByQnaQuestion_IdOrderByCreatedAtAsc(Long questionId);

    // 2) 답변자 기준 페이지 목록 (+ N+1 방지)
    @EntityGraph(attributePaths = {"user"})
    Page<QnaAnswer> findByUser_Id(Long userId, Pageable pageable);

    // 3) 질문별 답변 개수
    long countByQnaQuestion_Id(Long questionId);

    // 4) 권한 체크용 단건
    Optional<QnaAnswer> findByIdAndUser_Id(Long answerId, Long userId);

    // 5) 최신 답변 피드
    Page<QnaAnswer> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 6) 질문 존재 + 특정 답변 포함 여부
    boolean existsByIdAndQnaQuestion_Id(Long answerId, Long questionId);

    // 7) 프리뷰용: 첫 답변 1개
    Optional<QnaAnswer> findTop1ByQnaQuestion_IdOrderByCreatedAtAsc(Long questionId);

    // 8) 질문별 페이지형 답변
    Page<QnaAnswer> findByQnaQuestion_Id(Long questionId, Pageable pageable);

}

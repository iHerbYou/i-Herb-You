package com.iherbyou.community.service;

import com.iherbyou.catalog.Product;
import com.iherbyou.community.QnaAnswer;
import com.iherbyou.community.QnaQuestion;
import com.iherbyou.community.repository.QnaAnswerRepository;
import com.iherbyou.community.repository.QnaQuestionRepository;
import com.iherbyou.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QnaService {

    private static final int DEFAULT_STATUS = 101;
    private static final int MAX_CONTENT_LEN = 2000;

    private final QnaQuestionRepository questionRepo;
    private final QnaAnswerRepository answerRepo;

    @PersistenceContext
    private EntityManager em;

    public QnaService(QnaQuestionRepository questionRepo, QnaAnswerRepository answerRepo) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
    }

    // 상품 존재 확인, 제목/내용 검증, 상태 기본값
    @Transactional
    public QnaQuestion createQuestion(Long userId, Long productId, String title, String content) {
        Product product = em.find(Product.class, productId);
        if (product == null) throw new IllegalArgumentException("상품이 없습니다.");

        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("사용자가 없습니다.");

        String t = requireText(title, 200, "제목이 비었습니다.");
        String c = requireText(content, MAX_CONTENT_LEN, "내용이 비었습니다.");

        QnaQuestion q = QnaQuestion.builder()
                .product(product)
                .user(user)
                .title(t)
                .content(c)
                .createdAt(LocalDateTime.now())
                .statusCodeId(DEFAULT_STATUS)
                .build();

        return questionRepo.save(q);
    }

    // 공개/상태 필터
    @Transactional(readOnly = true)
    public Page<QnaQuestion> listByProduct(Long productId, Integer statusCodeId, Pageable pageable) {
        if (statusCodeId == null) {
            return questionRepo.findByProduct_Id(productId, pageable);
        }
        return questionRepo.findByProduct_IdAndStatusCodeId(productId, statusCodeId, pageable);
    }

    // 마이페이지
    @Transactional(readOnly = true)
    public Page<QnaQuestion> listMyQuestions(Long userId, Pageable pageable) {
        return questionRepo.findByUser_Id(userId, pageable);
    }

    // (답변) 권한 체크(관리자). 질문 존재/상태 확인
    @Transactional
    public QnaAnswer createAnswer(Long actorId, Long questionId, String content, boolean isSellerOrAdmin) {
        if (!isSellerOrAdmin) throw new IllegalStateException("권한이 없습니다.");

        QnaQuestion q = questionRepo.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        User actor = em.find(User.class, actorId);
        if (actor == null) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");

        String c = requireText(content, MAX_CONTENT_LEN, "답변 내용이 비었습니다.");

        QnaAnswer a = QnaAnswer.builder()
                .qnaQuestion(q)
                .user(actor)
                .content(c)
                .build();

        return answerRepo.save(a);
    }

    // 정렬
    @Transactional(readOnly = true)
    public List<QnaAnswer> listAnswers(Long questionId) {
        return answerRepo.findByQnaQuestion_IdOrderByCreatedAtAsc(questionId);
    }

    // 답변자 관리자 권한(updateAnswer)
    @Transactional
    public void deleteAnswer(Long actorId, Long answerId, boolean isAdmin) {
        QnaAnswer a = answerRepo.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        boolean owner = a.getUser().getId().equals(actorId);
        if (!owner && !isAdmin) throw new IllegalStateException("권한이 없습니다.");

        answerRepo.delete(a);
    }

    // countAnswer
    @Transactional(readOnly = true)
    public long countAnswers(Long questionId) {
        return answerRepo.countByQnaQuestion_Id(questionId);
    }
    private static String requireText(String src, int max, String emptyMsg) {
        if (src == null) throw new IllegalArgumentException(emptyMsg);
        String v = src.strip();            // trim 대신 strip: 유니코드 공백 포함
        if (v.isEmpty()) throw new IllegalArgumentException(emptyMsg);
        if (v.length() > max) throw new IllegalArgumentException("길이가 너무 깁니다.");
        return v;
    }



}

/* 주요 기능
 * - createQuestion(userId, productId, title, content)
 *   : 상품/사용자 확인 후 질문 등록(제목/내용 필수, 길이 제한, 기본 상태값 설정)
 * - listByProduct(productId, statusCodeId, pageable)
 *   : 상품별 질문 목록(상태값으로 필터 가능)
 * - listMyQuestions(userId, pageable)
 *   : 내가 쓴 질문 목록
 * - createAnswer(actorId, questionId, content, isSellerOrAdmin)
 *   : 관리자/셀러 권한 확인 후 답변 등록(내용 필수, 길이 제한)
 * - listAnswers(questionId)
 *   : 질문에 달린 답변 목록(오래된 순)
 * - deleteAnswer(actorId, answerId, isAdmin)
 *   : 답변 작성자 본인 또는 관리자만 삭제
 * - countAnswers(questionId)
 *   : 답변 개수
 *
 *   < 트랜잭션 >
 * - 쓰기 메서드: @Transactional
 * - 조회 메서드: @Transactional(readOnly = true)
 */

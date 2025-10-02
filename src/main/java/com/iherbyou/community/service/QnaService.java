package com.iherbyou.community.service;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.community.entity.QnaAnswer;
import com.iherbyou.community.entity.QnaQuestion;
import com.iherbyou.community.repository.QnaAnswerRepository;
import com.iherbyou.community.repository.QnaQuestionRepository;
import com.iherbyou.user.entity.User;
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

    private static final int DEFAULT_STATUS = 101;   // WAITING
    private static final int ANSWERED_STATUS = 102;  // ANSWERED
    private static final int MAX_CONTENT_LEN = 2000;

    private final QnaQuestionRepository questionRepo;
    private final QnaAnswerRepository answerRepo;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    public QnaService(QnaQuestionRepository questionRepo,
                      QnaAnswerRepository answerRepo,
                      CodeService codeService) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.codeService = codeService;
    }

    // 질문 생성
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

    // 상품별 목록 (statusValue 필터 가능)
    @Transactional(readOnly = true)
    public Page<QnaQuestion> listByProduct(Long productId, Integer statusValue, Pageable pageable) {
        if (statusValue == null) {
            return questionRepo.findByProduct_Id(productId, pageable);
        }
        return questionRepo.findByProduct_IdAndStatusCodeId(productId, statusValue, pageable);
    }

    // 내 질문 목록
    @Transactional(readOnly = true)
    public Page<QnaQuestion> listMyQuestions(Long userId, Pageable pageable) {
        return questionRepo.findByUser_Id(userId, pageable);
    }

    // 답변 생성 (서버에서 관리자 판별)
    @Transactional
    public QnaAnswer createAnswer(Long actorId, Long questionId, String content) {
        QnaQuestion q = questionRepo.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        // ✅ User/Code 엔티티 변경 없이 role value만 JPQL로 바로 조회
        Integer roleValue = em.createQuery(
                        "select rc.value from User u join u.roleCode rc where u.id = :uid", Integer.class)
                .setParameter("uid", actorId)
                .getSingleResult();

        boolean isAdmin = codeService.isAdminRole(roleValue); // 70그룹의 ADMIN_* 판별
        if (!isAdmin) throw new IllegalStateException("권한이 없습니다.");

        String c = requireText(content, MAX_CONTENT_LEN, "답변 내용이 비었습니다.");

        QnaAnswer saved = answerRepo.save(
                QnaAnswer.builder()
                        .qnaQuestion(q)
                        .user(em.getReference(User.class, actorId))
                        .content(c)
                        .build()
        );

        // 상태를 ANSWERED(102)로 전환
        int updated = questionRepo.updateStatus(q.getId(), ANSWERED_STATUS);
        if (updated == 0) throw new IllegalStateException("질문 상태 갱신에 실패했습니다.");

        return saved;
    }

    // 답변 삭제 (작성자 or 관리자)
    @Transactional
    public void deleteAnswer(Long actorId, Long answerId) {
        QnaAnswer a = answerRepo.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        boolean owner = a.getUser().getId().equals(actorId);

        Integer roleValue = em.createQuery(
                        "select rc.value from User u join u.roleCode rc where u.id = :uid", Integer.class)
                .setParameter("uid", actorId)
                .getSingleResult();
        boolean isAdmin = codeService.isAdminRole(roleValue);

        if (!owner && !isAdmin) throw new IllegalStateException("권한이 없습니다.");

        answerRepo.delete(a);
    }

    // 정렬
    @Transactional(readOnly = true)
    public List<QnaAnswer> listAnswers(Long questionId) {
        return answerRepo.findByQnaQuestion_IdOrderByCreatedAtAsc(questionId);
    }

    // count
    @Transactional(readOnly = true)
    public long countAnswers(Long questionId) {
        return answerRepo.countByQnaQuestion_Id(questionId);
    }

    private static String requireText(String src, int max, String emptyMsg) {
        if (src == null) throw new IllegalArgumentException(emptyMsg);
        String v = src.strip();
        if (v.isEmpty()) throw new IllegalArgumentException(emptyMsg);
        if (v.length() > max) throw new IllegalArgumentException("길이가 너무 깁니다.");
        return v;
    }

    // 질문 상태 변경 (관리자에서 호출)
    @Transactional
    public void changeQuestionStatus(Long questionId, Integer statusCodeValue) {
        // value 자체를 statusCodeId로 쓰는 구조라면 그대로 사용
        int updated = questionRepo.updateStatus(questionId, statusCodeValue);
        if (updated == 0) throw new IllegalStateException("질문 상태 변경에 실패했습니다.");
    }
}

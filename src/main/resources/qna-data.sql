-- 시드용 유저 (없으면 생성)
INSERT INTO user (name, email, password, created_at, updated_at)
SELECT 'Seed Bot', 'seed@iherbyou.com', '{noop}', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM user WHERE email = 'seed@iherbyou.com');

-- 모든 상품에 QnA 질문 1개씩 생성 (멱등)
INSERT INTO qna_question (product_id, user_id, title, content)
SELECT p.id,
       u.id,
       CONCAT('복용 시간은 언제가 좋나요? (상품 #', p.id, ')'),
       '아침 공복에 먹어도 되나요?'
FROM product p
         JOIN user u ON u.email = 'seed@iherbyou.com'
         LEFT JOIN qna_question qq
                   ON qq.product_id = p.id
                       AND qq.user_id = u.id
                       AND qq.title = CONCAT('복용 시간은 언제가 좋나요? (상품 #', p.id, ')')
WHERE qq.id IS NULL;

-- 짝수 번호 질문에만 답변 생성 (멱등)
INSERT INTO qna_answer (qna_question_id, user_id, content, created_at)
SELECT qq.id,
       u.id,
       '식후 30분 이내 복용을 권장드립니다.',
       NOW()
FROM qna_question qq
         JOIN user u ON u.email = 'seed@iherbyou.com'
         LEFT JOIN qna_answer qa ON qa.qna_question_id = qq.id
WHERE qa.id IS NULL
  AND (qq.id % 2) = 0;
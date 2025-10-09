-- 리뷰 더미데이터: 각 상품당 10~15개 보장 (MySQL 8.0+)
-- 전제: `user` 테이블에 최소 1명 이상 존재

INSERT INTO `review` (product_id, user_id, rating, text, created_at, is_deleted)
WITH RECURSIVE
    seq AS (SELECT 1 AS n
            UNION ALL
            SELECT n + 1
            FROM seq
            WHERE n < 15 -- 1..15
    ),
    prod AS (
        -- 각 상품에 10~15개 할당 (상품 id 기반 결정적 분포)
        SELECT p.id AS product_id, 10 + (p.id % 6) AS max_reviews
        FROM `product` p),
    user_pool AS (SELECT u.id, ROW_NUMBER() OVER (ORDER BY u.id) AS u_rn
                  FROM `user` u),
    user_cnt AS (SELECT COUNT(*) AS c
                 FROM `user`)
SELECT p.product_id,
       up.id                                    AS user_id,
       ROUND(3.0 + (RAND() * 2.0), 1)           AS rating, -- 3.0 ~ 5.0
       CASE FLOOR(RAND() * 30)
           WHEN 0 THEN '효과 좋아요. 재구매 의사 있습니다!'
           WHEN 1 THEN '배송 빠르고 제품 품질도 만족스럽습니다.'
           WHEN 2 THEN '가격 대비 괜찮은 것 같아요. 계속 먹어봐야 알 것 같습니다.'
           WHEN 3 THEN '친구 추천으로 구매했는데 좋네요!'
           WHEN 4 THEN '포장도 깔끔하고 먹기 편해요.'
           WHEN 5 THEN '리뷰 보고 샀는데 기대 이상입니다.'
           WHEN 6 THEN '한 달째 복용 중인데 확실히 달라진 느낌이에요.'
           WHEN 7 THEN '가족들과 함께 먹고 있습니다. 모두 만족해요.'
           WHEN 8 THEN '처음 먹어보는 건데 나쁘지 않네요.'
           WHEN 9 THEN '꾸준히 먹으면 좋을 것 같아요. 재구매할게요.'
           WHEN 10 THEN '성분 보고 구매했어요. 믿고 먹습니다.'
           WHEN 11 THEN '아침마다 챙겨먹기 좋아요. 간편합니다.'
           WHEN 12 THEN '선물용으로도 좋을 것 같네요!'
           WHEN 13 THEN '가격이 조금 부담되지만 품질은 확실해요.'
           WHEN 14 THEN '예상보다 빨리 왔어요. 감사합니다.'
           WHEN 15 THEN '건강 챙기려고 샀는데 잘 산 것 같아요.'
           WHEN 16 THEN '맛도 괜찮고 먹기 편해요.'
           WHEN 17 THEN '부모님 드리려고 샀는데 좋아하시네요.'
           WHEN 18 THEN '여러 제품 비교해보고 이걸로 결정했어요. 만족합니다.'
           WHEN 19 THEN '할인할 때 사서 더 좋네요. 추천해요!'
           WHEN 20 THEN '정기배송으로 신청했습니다. 계속 먹을게요.'
           WHEN 21 THEN '피부가 좋아진 느낌이에요. 효과 있는 것 같아요.'
           WHEN 22 THEN '소화도 잘되고 속이 편안해요.'
           WHEN 23 THEN '운동할 때 같이 먹으면 좋을 것 같아요.'
           WHEN 24 THEN '성분표 꼼꼼히 보고 샀어요. 믿을 만합니다.'
           WHEN 25 THEN '다이어트 중인데 도움 되는 것 같아요.'
           WHEN 26 THEN '면역력 높이려고 먹기 시작했어요. 기대됩니다.'
           WHEN 27 THEN '가성비 최고! 다음에도 또 살게요.'
           WHEN 28 THEN '유통기한도 넉넉하고 좋네요.'
           ELSE '전반적으로 만족스러운 제품입니다. 추천해요.'
           END                                  AS text,
       NOW() - INTERVAL FLOOR(RAND() * 180) DAY AS created_at,
       0                                        AS is_deleted
FROM prod p
         JOIN seq s ON s.n <= p.max_reviews
         JOIN user_cnt uc ON uc.c > 0
         JOIN user_pool up
              ON up.u_rn = ((s.n - 1) % uc.c) + 1 -- 유저 라운드로빈
WHERE NOT EXISTS (SELECT 1
                  FROM `review` r
                  WHERE r.product_id = p.product_id
                    AND r.user_id = up.id);
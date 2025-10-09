-- 시드용 유저 (없으면 생성)
INSERT INTO `user` (name, email, password, created_at, updated_at)
SELECT 'Seed Bot', 'seed@iherbyou.com', '{noop}password123', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `user` u WHERE u.email = 'seed@iherbyou.com');

-- 더미 유저 50~100명 자동 생성 (중복 방지)
-- MySQL 8.0 이상: 재귀 CTE 사용
INSERT INTO `user` (name, email, password, created_at, updated_at)
WITH RECURSIVE seq AS (SELECT 1 AS n
                       UNION ALL
                       SELECT n + 1
                       FROM seq
                       WHERE n < (50 + FLOOR(RAND() * 51)) -- 50~100명
)
SELECT CONCAT('User', LPAD(s.n, 3, '0'))                 AS name,
       CONCAT('user', LPAD(s.n, 3, '0'), '@example.com') AS email,
       '{noop}password123'                               AS password, -- 테스트용 평문 비밀번호 (NoOp)
       NOW() - INTERVAL FLOOR(RAND() * 365) DAY          AS created_at,
       NOW()                                             AS updated_at
FROM seq s
WHERE NOT EXISTS (SELECT 1
                  FROM `user` u
                  WHERE u.email = CONCAT('user', LPAD(s.n, 3, '0'), '@example.com'));
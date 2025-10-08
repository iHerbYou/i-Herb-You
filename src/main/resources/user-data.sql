-- 시드용 유저 (없으면 생성)
INSERT INTO user (name, email, password, created_at, updated_at)
SELECT 'Seed Bot', 'seed@iherbyou.com', '{noop}', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM user WHERE email = 'seed@iherbyou.com');

-- 추가 더미 유저들 생성 (리뷰 작성용)
INSERT INTO user (name, email, password, created_at, updated_at)
SELECT
    CONCAT('User', seq.n),
    CONCAT('user', seq.n, '@example.com'),
    '{noop}',
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY,
    NOW()
FROM (
         SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
         UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
         UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
         UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
         UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
         UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
     ) seq
WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE email = CONCAT('user', seq.n, '@example.com')
);
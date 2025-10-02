-- products-and-mapping.sql
START TRANSACTION;

-- 0) 브랜드 준비: 없으면 생성 후 id 보관
INSERT INTO brand (name) VALUES ('iHY Default Brand')
    ON DUPLICATE KEY UPDATE name = VALUES(name);

SET @brand_id := (SELECT id FROM brand WHERE name = 'iHY Default Brand');

-- 카테고리 상수 (top-level)
SET @CAT_SUPP := 1;   -- 영양제
SET @CAT_SPORT := 2;  -- 스포츠
SET @CAT_BEAUTY := 3; -- 뷰티

-- (선택) 소분류 매핑에 쓰는 id (category-data.sql 기준)
SET @CAT_MULTI_VIT  := 13; -- 종합비타민
SET @CAT_PROBIOTIC  := 40; -- 프로바이오틱스
SET @CAT_BCAA       := 74; -- BCAA
SET @CAT_PREWORKOUT := 72; -- 운동 전 각성제
SET @CAT_COLLAGEN   := 87; -- 콜라겐
SET @CAT_HA         := 89; -- 히알루론산
SET @CAT_PROTEINBAR := 80; -- 단백질 바
SET @CAT_PROTEIN    := 62; -- (스포츠) 단백질

/* 1) product 업서트 (id 고정 1~24)
   - 스키마: product(id PK AI, brand_id NOT NULL, code, name, min_price, avg_rating, review_count, ...)
*/
INSERT INTO product (id, brand_id, code, name, min_price, avg_rating, review_count)
VALUES
    (1 ,@brand_id,'P0001','비봇 내추럴 프로틴 바',        20000,4.5,128),
    (2 ,@brand_id,'P0002','액티브엣 오렌지 다이어트 보조제',35000,4.2, 86),
    (3 ,@brand_id,'P0003','엘리피트 에너지 푸드믹스',      25000,4.0, 45),
    (4 ,@brand_id,'P0004','헤르바이츠 베리그래놀라볼',     15000,4.8,203),
    (5 ,@brand_id,'P0005','헬시퍼펙트 프로틴 파우더',      25000,4.3, 59),
    (6 ,@brand_id,'P0006','헬시핏 리플레이스먼트 쉐이크',  25000,4.1, 37),
    (7 ,@brand_id,'P0007','헬씨엣 멀티 건강 파우더',        25000,4.6,142),
    (8 ,@brand_id,'P0008','액티브릿 그린 밸런스 쉐이크',   24000,4.0, 21),
    (9 ,@brand_id,'P0009','액티브핏 그린 헬시 쉐이크',     25000,4.4, 73),
    (10,@brand_id,'P0010','헬시엣 핑크 하이드레이션 믹스', 28000,4.2, 65),
    (11,@brand_id,'P0011','액티브핏 퍼플 쉐이크',          15000,4.1, 18),
    (12,@brand_id,'P0012','헬씨데이 수분충전 믹스',         15000,4.0, 12),
    (13,@brand_id,'P0013','액티브핏 핑크 다이어트 쉐이크',  12000,4.3, 39),
    (14,@brand_id,'P0014','헬시엣 오트밀 하이드레이션 믹스',35000,4.5, 91),
    (15,@brand_id,'P0015','액티브플러스 수분충전 이온믹스', 15000,3.9, 27),
    (16,@brand_id,'P0016','헬씨나잇 오렌지 주스',           7000,4.2, 44),
    (17,@brand_id,'P0017','프리미엄 프로틴 파우더',         45000,4.7,310),
    (18,@brand_id,'P0018','프리미엄 비타민 컴플렉스',       38000,4.6,204),
    (19,@brand_id,'P0019','메가 비타민 데일리팩',           36000,4.5,187),
    (20,@brand_id,'P0020','프로바이오틱스 플러스',          28000,4.3, 92),
    (21,@brand_id,'P0021','스포츠 리커버리 BCAA',           22000,4.2, 74),
    (22,@brand_id,'P0022','하이퍼 포커스 프리워크아웃',     27000,4.1, 53),
    (23,@brand_id,'P0023','뷰티 콜라겐 펩타이드 파우더',    32000,4.4, 88),
    (24,@brand_id,'P0024','하이드라 글로우 히알루론 캡슐',  34000,4.6,132)
    ON DUPLICATE KEY UPDATE
                         brand_id=VALUES(brand_id),
                         code     =VALUES(code),
                         name     =VALUES(name),
                         min_price=VALUES(min_price),
                         avg_rating=VALUES(avg_rating),
                         review_count=VALUES(review_count);

-- 2) 대표 이미지 재적재 (멱등성 확보: 기존 것 삭제 후 재삽입)
DELETE FROM product_img WHERE product_id BETWEEN 1 AND 24;

INSERT INTO product_img (product_id, is_primary, sort_idx, image_url, alt_text) VALUES
                                                                                    (1 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/215b67d8800ede97a2a3fdee95d3a8ea.jpg','비봇 내추럴 프로틴 바'),
                                                                                    (2 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/3bec1ce45301ead6c688a85f57c8dc7d.jpg','액티브엣 오렌지 다이어트 보조제'),
                                                                                    (3 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/523932a5c5e7210759793a4d9d14a75e.jpg','엘리피트 에너지 푸드믹스'),
                                                                                    (4 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/673b98b471baf11002e5bab36503c51c.jpg','헤르바이츠 베리그래놀라볼'),
                                                                                    (5 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/77eb854fdaeb25d9ce1e721d33de36f8.jpg','헬시퍼펙트 프로틴 파우더'),
                                                                                    (6 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/9cbb7df27c878dcaa0c2f1f40478fc6b.jpg','헬시핏 리플레이스먼트 쉐이크'),
                                                                                    (7 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/9f4a5198463132562a84f88d94bf4e59.jpg','헬씨엣 멀티 건강 파우더'),
                                                                                    (8 ,1,0,'https://ecimg.cafe24img.com/pg2205b12386875045/sojjung3/web/product/medium/20250906/b1db6f8b9b3265807603f0236e5f05ae.jpg','액티브릿 그린 밸런스 쉐이크'),
                                                                                    (9 ,1,0,'/images/b864aeace600d99ee6b4b23c8fce5c46.jpg','액티브핏 그린 헬시 쉐이크'),
                                                                                    (10,1,0,'/images/ba076ba8dc27cd49d1200ace2a3a1b8d.jpg','헬시엣 핑크 하이드레이션 믹스'),
                                                                                    (11,1,0,'/images/c15ad09850349c88123e222b101d6e58.jpg','액티브핏 퍼플 쉐이크'),
                                                                                    (12,1,0,'/images/ca30d9596a9ef11bd8084a538fedefd3.jpg','헬씨데이 수분충전 믹스'),
                                                                                    (13,1,0,'/images/d01e3df9cea446dedc4748c6b835f073.jpg','액티브핏 핑크 다이어트 쉐이크'),
                                                                                    (14,1,0,'/images/e112d84251661d572454e7e00d4889de.jpg','헬시엣 오트밀 하이드레이션 믹스'),
                                                                                    (15,1,0,'/images/f45a1bd8675ca4b292ac38027f3e1336.jpg','액티브플러스 수분충전 이온믹스'),
                                                                                    (16,1,0,'/images/f4ae363f5d361ad62a6c06ed0dde9cfd.jpg','헬씨나잇 오렌지 주스'),
                                                                                    (17,1,0,'/images/215b67d8800ede97a2a3fdee95d3a8ea.jpg','프리미엄 프로틴 파우더'),
                                                                                    (18,1,0,'/images/3bec1ce45301ead6c688a85f57c8dc7d.jpg','프리미엄 비타민 컴플렉스'),
                                                                                    (19,1,0,'/images/3bec1ce45301ead6c688a85f57c8dc7d.jpg','메가 비타민 데일리팩'),
                                                                                    (20,1,0,'/images/215b67d8800ede97a2a3fdee95d3a8ea.jpg','프로바이오틱스 플러스'),
                                                                                    (21,1,0,'/images/b1db6f8b9b3265807603f0236e5f05ae.jpg','스포츠 리커버리 BCAA'),
                                                                                    (22,1,0,'/images/9f4a5198463132562a84f88d94bf4e59.jpg','하이퍼 포커스 프리워크아웃'),
                                                                                    (23,1,0,'/images/ba076ba8dc27cd49d1200ace2a3a1b8d.jpg','뷰티 콜라겐 펩타이드 파우더'),
                                                                                    (24,1,0,'/images/ca30d9596a9ef11bd8084a538fedefd3.jpg','하이드라 글로우 히알루론 캡슐');

-- 3) 카테고리 매핑 재적재 (멱등: 해당 product 범위 삭제 후 재삽입)
DELETE FROM product_category WHERE product_id BETWEEN 1 AND 24;

-- 3-1) Top-level 매핑 (목데이터 category 필드 기준)
INSERT INTO product_category (product_id, category_id) VALUES
                                                           (1 ,@CAT_SPORT),
                                                           (2 ,@CAT_SUPP),
                                                           (3 ,@CAT_SPORT),
                                                           (4 ,@CAT_SUPP),
                                                           (5 ,@CAT_SPORT),
                                                           (6 ,@CAT_SPORT),
                                                           (7 ,@CAT_SUPP),
                                                           (8 ,@CAT_SPORT),
                                                           (9 ,@CAT_SPORT),
                                                           (10,@CAT_BEAUTY),
                                                           (11,@CAT_SPORT),
                                                           (12,@CAT_BEAUTY),
                                                           (13,@CAT_SPORT),
                                                           (14,@CAT_BEAUTY),
                                                           (15,@CAT_SUPP),
                                                           (16,@CAT_SPORT),
                                                           (17,@CAT_SPORT),
                                                           (18,@CAT_SUPP),
                                                           (19,@CAT_SUPP),
                                                           (20,@CAT_SUPP),
                                                           (21,@CAT_SPORT),
                                                           (22,@CAT_SPORT),
                                                           (23,@CAT_BEAUTY),
                                                           (24,@CAT_BEAUTY);

-- 3-2) 명확한 소분류 추가 매핑 (있어도 되고 없어도 됨; 검색/필터 정밀도 향상)
INSERT INTO product_category (product_id, category_id) VALUES
                                                           (1 ,@CAT_PROTEINBAR),   -- 비봇 내추럴 프로틴 바 → 단백질 바
                                                           (17,@CAT_PROTEIN),      -- 프리미엄 프로틴 파우더 → 단백질(대분류)
                                                           (18,@CAT_MULTI_VIT),    -- 프리미엄 비타민 컴플렉스 → 종합비타민
                                                           (19,@CAT_MULTI_VIT),    -- 메가 비타민 데일리팩 → 종합비타민
                                                           (20,@CAT_PROBIOTIC),    -- 프로바이오틱스 플러스 → 프로바이오틱스
                                                           (21,@CAT_BCAA),         -- 스포츠 리커버리 BCAA → BCAA
                                                           (22,@CAT_PREWORKOUT),   -- 프리워크아웃 → 운동 전 각성제
                                                           (23,@CAT_COLLAGEN),     -- 뷰티 콜라겐 파우더 → 콜라겐
                                                           (24,@CAT_HA);           -- 히알루론산 캡슐 → 히알루론산

COMMIT;
-- =====================
-- 테이블 초기화
-- =====================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE product_category;
TRUNCATE TABLE category;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================
-- 대분류
-- =====================
INSERT INTO category (name, parent_id) VALUES ('영양제', NULL); -- id=1
SET @c1 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('스포츠', NULL); -- id=2
SET @c2 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('뷰티', NULL);   -- id=3
SET @c3 := LAST_INSERT_ID();

-- =====================
-- 영양제 하위
-- =====================
INSERT INTO category (name, parent_id) VALUES ('비타민', @c1);   
SET @c4 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('미네랄', @c1);   
SET @c5 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('수면', @c1);     
SET @c6 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('항산화제', @c1);
SET @c7 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('장 건강', @c1);
SET @c8 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('뼈, 관절&연골', @c1);
SET @c9 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('두뇌 & 인지', @c1);
SET @c10 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('오메가 & 피쉬오일', @c1);
SET @c11 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('아미노산', @c1);
SET @c12 := LAST_INSERT_ID();

-- 영양제 > 비타민
INSERT INTO category (name, parent_id) VALUES ('종합비타민', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 A', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 B', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 C', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 D', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 E', @c4);
INSERT INTO category (name, parent_id) VALUES ('비타민 K', @c4);

-- 영양제 > 미네랄
INSERT INTO category (name, parent_id) VALUES ('칼슘', @c5);
INSERT INTO category (name, parent_id) VALUES ('마그네슘', @c5);
INSERT INTO category (name, parent_id) VALUES ('철분', @c5);
INSERT INTO category (name, parent_id) VALUES ('아연', @c5);
INSERT INTO category (name, parent_id) VALUES ('셀레늄', @c5);
INSERT INTO category (name, parent_id) VALUES ('요오드', @c5);
INSERT INTO category (name, parent_id) VALUES ('칼륨', @c5);
INSERT INTO category (name, parent_id) VALUES ('바다 이끼', @c5);

-- 영양제 > 수면
INSERT INTO category (name, parent_id) VALUES ('멜라토닌', @c6);
INSERT INTO category (name, parent_id) VALUES ('마그네슘', @c6);
INSERT INTO category (name, parent_id) VALUES ('5-HTP', @c6);
INSERT INTO category (name, parent_id) VALUES ('트립토판', @c6);
INSERT INTO category (name, parent_id) VALUES ('길초근', @c6);

-- 영양제 > 항산화제
INSERT INTO category (name, parent_id) VALUES ('Coenzyme Q10 (Ubiquinone)', @c7);
INSERT INTO category (name, parent_id) VALUES ('강황&커큐민', @c7);
INSERT INTO category (name, parent_id) VALUES ('글루타치온', @c7);
INSERT INTO category (name, parent_id) VALUES ('루테인', @c7);
INSERT INTO category (name, parent_id) VALUES ('제아잔틴', @c7);
INSERT INTO category (name, parent_id) VALUES ('NAC(N-아세틸 시스테인)', @c7);
INSERT INTO category (name, parent_id) VALUES ('아스타잔틴', @c7);

-- 영양제 > 장 건강
INSERT INTO category (name, parent_id) VALUES ('프로바이오틱스', @c8);
INSERT INTO category (name, parent_id) VALUES ('프리바이오틱스', @c8);
INSERT INTO category (name, parent_id) VALUES ('소화 효소', @c8);
INSERT INTO category (name, parent_id) VALUES ('섬유소', @c8);

-- 영양제 > 뼈, 관절&연골
INSERT INTO category (name, parent_id) VALUES ('칼슘', @c9);
INSERT INTO category (name, parent_id) VALUES ('콜라겐 보충제', @c9);
INSERT INTO category (name, parent_id) VALUES ('클루코사민', @c9);
INSERT INTO category (name, parent_id) VALUES ('콘드로이틴', @c9);
INSERT INTO category (name, parent_id) VALUES ('MSM', @c9);
INSERT INTO category (name, parent_id) VALUES ('강황&커큐민', @c9);

-- 영양제 > 두뇌 & 인지
INSERT INTO category (name, parent_id) VALUES ('집중력&기억력', @c10);
INSERT INTO category (name, parent_id) VALUES ('크레아틴', @c10);

-- 영양제 > 오메가 & 피쉬오일
INSERT INTO category (name, parent_id) VALUES ('오메가3 피쉬 오일', @c11);
INSERT INTO category (name, parent_id) VALUES ('DHA', @c11);
INSERT INTO category (name, parent_id) VALUES ('크릴 오일', @c11);
INSERT INTO category (name, parent_id) VALUES ('아마씨유&보충제', @c11);
INSERT INTO category (name, parent_id) VALUES ('오메가 3-6-9', @c11);
INSERT INTO category (name, parent_id) VALUES ('해조류 오메가3', @c11);

-- 영양제 > 아미노산
INSERT INTO category (name, parent_id) VALUES ('아르기닌', @c12);
INSERT INTO category (name, parent_id) VALUES ('글루타민', @c12);
INSERT INTO category (name, parent_id) VALUES ('테아닌', @c12);
INSERT INTO category (name, parent_id) VALUES ('아미노산 블렌드', @c12);

-- =====================
-- 스포츠 하위
-- =====================
INSERT INTO category (name, parent_id) VALUES ('단백질', @c2);
SET @c13 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('운동 전 보충제', @c2);
SET @c14 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('운동 후 회복', @c2);
SET @c15 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('바, 쿠키, 스낵', @c2);
SET @c16 := LAST_INSERT_ID();

-- 스포츠 > 단백질
INSERT INTO category (name, parent_id) VALUES ('유청 단백질', @c13);
INSERT INTO category (name, parent_id) VALUES ('미셀라 카제인 단백질', @c13);
INSERT INTO category (name, parent_id) VALUES ('식물성 단백질', @c13);
INSERT INTO category (name, parent_id) VALUES ('동물성 단백질', @c13);

-- 스포츠 > 운동 전 보충제
INSERT INTO category (name, parent_id) VALUES ('카페인', @c14);
INSERT INTO category (name, parent_id) VALUES ('베타 알라닌', @c14);
INSERT INTO category (name, parent_id) VALUES ('운동 전 각성제', @c14);
INSERT INTO category (name, parent_id) VALUES ('비각성 운동 전 보충제', @c14);

-- 스포츠 > 운동 후 회복
INSERT INTO category (name, parent_id) VALUES ('BCAA', @c15);
INSERT INTO category (name, parent_id) VALUES ('필수 아미노산', @c15);
INSERT INTO category (name, parent_id) VALUES ('류신', @c15);
INSERT INTO category (name, parent_id) VALUES ('L-글루타민', @c15);
INSERT INTO category (name, parent_id) VALUES ('탄수화물 분말', @c15);
INSERT INTO category (name, parent_id) VALUES ('아연 마그네슘 아스파테이트', @c15);

-- 스포츠 > 바, 쿠키, 스낵
INSERT INTO category (name, parent_id) VALUES ('단백질 바', @c16);
INSERT INTO category (name, parent_id) VALUES ('단백질 스낵', @c16);
INSERT INTO category (name, parent_id) VALUES ('다이어트 바', @c16);

-- =====================
-- 뷰티 하위
-- =====================
INSERT INTO category (name, parent_id) VALUES ('모발, 피부, 손발톱', @c3);
SET @c17 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('다이어트', @c3);
SET @c18 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('여성 건강', @c3);
SET @c19 := LAST_INSERT_ID();

INSERT INTO category (name, parent_id) VALUES ('체중 관리', @c3);
SET @c20 := LAST_INSERT_ID();

-- 뷰티 > 모발, 피부, 손발톱
INSERT INTO category (name, parent_id) VALUES ('콜라겐', @c17);
INSERT INTO category (name, parent_id) VALUES ('비오틴(비타민B7)', @c17);
INSERT INTO category (name, parent_id) VALUES ('히알루론산', @c17);

-- 뷰티 > 다이어트
INSERT INTO category (name, parent_id) VALUES ('당 제어', @c18);
INSERT INTO category (name, parent_id) VALUES ('에너지', @c18);

-- 뷰티 > 여성 건강
INSERT INTO category (name, parent_id) VALUES ('여성용 종합비타민', @c19);
INSERT INTO category (name, parent_id) VALUES ('월경 전 증후군 지원', @c19);
INSERT INTO category (name, parent_id) VALUES ('폐경 이행기 & 폐경기 지원', @c19);

-- 뷰티 > 체중 관리
INSERT INTO category (name, parent_id) VALUES ('다이어트 포뮬라', @c20);
INSERT INTO category (name, parent_id) VALUES ('지방 연소제', @c20);
INSERT INTO category (name, parent_id) VALUES ('식사 대용품', @c20);
INSERT INTO category (name, parent_id) VALUES ('식욕 보조제', @c20);
INSERT INTO category (name, parent_id) VALUES ('녹차 추출물', @c20);

START TRANSACTION;

-- Top (depth 1)
INSERT INTO category (id, parent_id, name) VALUES
                                               (1, NULL, '영양제'),
                                               (2, NULL, '스포츠'),
                                               (3, NULL, '뷰티')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Mid under '영양제' (depth 2)
INSERT INTO category (id, parent_id, name) VALUES
                                               (4, 1, '비타민'),
                                               (5, 1, '미네랄'),
                                               (6, 1, '수면'),
                                               (7, 1, '항산화제'),
                                               (8, 1, '장 건강'),
                                               (9, 1, '뼈, 관절&연골'),
                                               (10, 1, '두뇌 & 인지'),
                                               (11, 1, '오메가 & 피쉬오일'),
                                               (12, 1, '아미노산')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '비타민' (parent=4)
INSERT INTO category (id, parent_id, name) VALUES
                                               (13, 4, '종합비타민'),
                                               (14, 4, '비타민 A'),
                                               (15, 4, '비타민 B'),
                                               (16, 4, '비타민 C'),
                                               (17, 4, '비타민 D'),
                                               (18, 4, '비타민 E'),
                                               (19, 4, '비타민 K')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '미네랄' (parent=5)
INSERT INTO category (id, parent_id, name) VALUES
                                               (20, 5, '칼슘'),
                                               (21, 5, '마그네슘'),
                                               (22, 5, '철분'),
                                               (23, 5, '아연'),
                                               (24, 5, '셀레늄'),
                                               (25, 5, '요오드'),
                                               (26, 5, '칼륨'),
                                               (27, 5, '바다 이끼')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '수면' (parent=6)
INSERT INTO category (id, parent_id, name) VALUES
                                               (28, 6, '멜라토닌'),
                                               (29, 6, '마그네슘'),
                                               (30, 6, '5-HTP'),
                                               (31, 6, '트립토판'),
                                               (32, 6, '길초근')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '항산화제' (parent=7)
INSERT INTO category (id, parent_id, name) VALUES
                                               (33, 7, 'Coenzyme Q10 (Ubiquinone)'),
                                               (34, 7, '강황&커큐민'),
                                               (35, 7, '글루타치온'),
                                               (36, 7, '루테인'),
                                               (37, 7, '제아잔틴'),
                                               (38, 7, 'NAC(N-아세틸 시스테인)'),
                                               (39, 7, '아스타잔틴')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '장 건강' (parent=8)
INSERT INTO category (id, parent_id, name) VALUES
                                               (40, 8, '프로바이오틱스'),
                                               (41, 8, '프리바이오틱스'),
                                               (42, 8, '소화 효소'),
                                               (43, 8, '섬유소')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '뼈, 관절&연골' (parent=9)
INSERT INTO category (id, parent_id, name) VALUES
                                               (44, 9, '칼슘'),
                                               (45, 9, '콜라겐 보충제'),
                                               (46, 9, '클루코사민'),
                                               (47, 9, '콘드로이틴'),
                                               (48, 9, 'MSM'),
                                               (49, 9, '강황&커큐민')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '두뇌 & 인지' (parent=10)
INSERT INTO category (id, parent_id, name) VALUES
                                               (50, 10, '집중력&기억력'),
                                               (51, 10, '크레아틴')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '오메가 & 피쉬오일' (parent=11)
INSERT INTO category (id, parent_id, name) VALUES
                                               (52, 11, '오메가3 피쉬 오일'),
                                               (53, 11, 'DHA'),
                                               (54, 11, '크릴 오일'),
                                               (55, 11, '아마씨유&보충제'),
                                               (56, 11, '오메가 3-6-9'),
                                               (57, 11, '해조류 오메가3')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '아미노산' (parent=12)
INSERT INTO category (id, parent_id, name) VALUES
                                               (58, 12, '아르기닌'),
                                               (59, 12, '글루타민'),
                                               (60, 12, '테아닌'),
                                               (61, 12, '아미노산 블렌드')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Mid under '스포츠' (depth 2)
INSERT INTO category (id, parent_id, name) VALUES
                                               (62, 2, '단백질'),
                                               (63, 2, '운동 전 보충제'),
                                               (64, 2, '운동 후 회복'),
                                               (65, 2, '바, 쿠키, 스낵')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '단백질' (parent=62)
INSERT INTO category (id, parent_id, name) VALUES
                                               (66, 62, '유청 단백질'),
                                               (67, 62, '미셀라 카제인 단백질'),
                                               (68, 62, '식물성 단백질'),
                                               (69, 62, '동물성 단백질')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '운동 전 보충제' (parent=63)
INSERT INTO category (id, parent_id, name) VALUES
                                               (70, 63, '카페인'),
                                               (71, 63, '베타 알라닌'),
                                               (72, 63, '운동 전 각성제'),
                                               (73, 63, '비각성 운동 전 보충제')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '운동 후 회복' (parent=64)
INSERT INTO category (id, parent_id, name) VALUES
                                               (74, 64, 'BCAA'),
                                               (75, 64, '필수 아미노산'),
                                               (76, 64, '류신'),
                                               (77, 64, 'L-글루타민'),
                                               (78, 64, '탄수화물 분말'),
                                               (79, 64, '아연 마그네슘 아스파테이트')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '바, 쿠키, 스낵' (parent=65)
INSERT INTO category (id, parent_id, name) VALUES
                                               (80, 65, '단백질 바'),
                                               (81, 65, '단백질 스낵'),
                                               (82, 65, '다이어트 바')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Mid under '뷰티' (depth 2)
INSERT INTO category (id, parent_id, name) VALUES
                                               (83, 3, '모발, 피부, 손발톱'),
                                               (84, 3, '다이어트'),
                                               (85, 3, '여성 건강'),
                                               (86, 3, '체중 관리')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '모발, 피부, 손발톱' (parent=83)
INSERT INTO category (id, parent_id, name) VALUES
                                               (87, 83, '콜라겐'),
                                               (88, 83, '비오틴(비타민B7)'),
                                               (89, 83, '히알루론산')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '다이어트' (parent=84)
INSERT INTO category (id, parent_id, name) VALUES
                                               (90, 84, '당 제어'),
                                               (91, 84, '에너지')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '여성 건강' (parent=85)
INSERT INTO category (id, parent_id, name) VALUES
                                               (92, 85, '여성용 종합비타민'),
                                               (93, 85, '월경 전 증후군 지원'),
                                               (94, 85, '폐경 이행기 & 폐경기 지원')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

-- Small under '체중 관리' (parent=86)
INSERT INTO category (id, parent_id, name) VALUES
                                               (95, 86, '다이어트 포뮬라'),
                                               (96, 86, '지방 연소제'),
                                               (97, 86, '식사 대용품'),
                                               (98, 86, '식욕 보조제'),
                                               (99, 86, '녹차 추출물')
ON DUPLICATE KEY UPDATE name=VALUES(name), parent_id=VALUES(parent_id);

COMMIT;
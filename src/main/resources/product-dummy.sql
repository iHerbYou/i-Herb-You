INSERT IGNORE INTO brand (name) VALUES ('센트룸');
INSERT IGNORE INTO brand (name) VALUES ('종근당건강');
INSERT IGNORE INTO brand (name) VALUES ('오쏘몰');
INSERT IGNORE INTO brand (name) VALUES ('부강 프로텍트');
INSERT IGNORE INTO brand (name) VALUES ('뉴트리 포유');
INSERT IGNORE INTO brand (name) VALUES ('아임비타');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 맨 종합 멀티 비타민 112정 코스트코 남성 50+ 대용량 영양제',
 'P86788593520','50세 이상 남성을 위한 종합 멀티비타민, 면역력 강화 및 활력 증진에 도움',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,1890,4.5,320,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86788593520'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86788593520'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86788593520'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86788593520'),'112정',45000,39990,112,'600000000001','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86788593520'),'56정',25000,22900,56,'600000000002','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='112정' AND product_id=(SELECT id FROM product WHERE code='P86788593520')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='56정' AND product_id=(SELECT id FROM product WHERE code='P86788593520')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86788593520'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86788593520'),1890,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 포 맨 더블업 종합 멀티 비타민 112정 코스트코 남성 대용량 영양제',
 'P88179641598','남성용 더블업 멀티비타민, 영양소 2배 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분, 셀레늄',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-10 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179641598'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179641598'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179641598'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179641598'),'더블업 112정',45000,39990,112,'600000000003','10x10x5mm','남성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179641598'),'더블업 56정',25000,22900,56,'600000000004','10x10x5mm','남성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179641598')),
 203,'2025-01-26 09:15:00','2025-01-22 14:00:00'),
((SELECT id FROM product_variant WHERE variant_name='더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179641598')),
 127,'2025-01-26 09:15:00','2025-01-19 11:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179641598'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179641598'),2340,'2025-01-27 15:30:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 우먼 종합 멀티 비타민 영양제 112정 코스트코 비타민B12 대용량',
 'P86787803460','50세 이상 여성을 위한 종합 멀티비타민, 비타민B12 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86787803460'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86787803460'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86787803460'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86787803460'),'우먼 112정',45000,39990,112,'600000000005','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86787803460'),'우먼 56정',25000,22900,56,'600000000006','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='우먼 112정' AND product_id=(SELECT id FROM product WHERE code='P86787803460')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='우먼 56정' AND product_id=(SELECT id FROM product WHERE code='P86787803460')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86787803460'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86787803460'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당건강'),
 '[1+1]종근당 아임비타 이뮨플러스 2박스 샷 올인원영양제 마시는 액상 비타민',
 'P86110690330','액상 비타민 샷, 1+1 이벤트로 2박스 제공, 면역력 강화',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 엽산, 판토텐산, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-05 00:00:00',24,6,1890,4.4,280,21500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86110690330'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86110690330'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86110690330'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86110690330'),'종근당 2박스 (1+1)',43000,21500,60,'700000000001','액상 20ml','액상 비타민 샷',6,NULL),
((SELECT id FROM product WHERE code='P86110690330'),'종근당 1박스',25000,22900,30,'700000000002','액상 20ml','액상 비타민 샷',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 2박스 (1+1)' AND product_id=(SELECT id FROM product WHERE code='P86110690330')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 1박스' AND product_id=(SELECT id FROM product WHERE code='P86110690330')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86110690330'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86110690330'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '오쏘몰 이뮨 멀티비타민 미네랄 (액상 20ml + 정제 919mg) x 30개입, 1개',
 'P51929317174','액상과 정제를 함께 제공하는 이뮨 멀티비타민, 면역력 강화',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-12 00:00:00',36,6,1570,4.3,187,80520);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929317174'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929317174'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929317174'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929317174'),'오쏘몰 30개입',85000,80520,30,'800000000001','액상+정제','이뮨 멀티비타민 미네랄',6,NULL),
((SELECT id FROM product WHERE code='P51929317174'),'오쏘몰 15개입',45000,42900,15,'800000000002','액상+정제','이뮨 멀티비타민 미네랄',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 30개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 15개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929317174'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929317174'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='부강 프로텍트'),
 '이뮨 멀티비타민 올인원영양제 여성 남성 종합비타민 멀티팩',
 'P86602293233','남녀 모두 섭취 가능한 종합 멀티비타민, 면역력 강화용',
 '하루 1정, 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘',
 '권장량을 초과하지 말고 특정 질환이 있는 경우 전문가 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-03-15 00:00:00',24,6,1240,4.3,187,38800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86602293233'),
 'https://shopping-phinf.pstatic.net/main_8660229/86602293233.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86602293233'),
 'https://shopping-phinf.pstatic.net/main_8660229/86602293233.2.jpg','추가 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86602293233'),'부강 90정',38800,35800,90,'111111111111','10x10x5mm','멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86602293233'),'부강 180정 (대용량)',72000,65000,180,'222222222222','10x10x5mm','멀티비타민 포뮬라',6,'2025-10-15 00:00:00');
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='부강 90정' AND product_id=(SELECT id FROM product WHERE code='P86602293233')),
 122,'2025-09-29 12:00:00','2025-07-15 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='부강 180정 (대용량)' AND product_id=(SELECT id FROM product WHERE code='P86602293233')),
 0,'2025-09-29 12:00:00',NULL);
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86602293233'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86602293233'),1240,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 우먼 더블업 멀티비타민 112정 코스트코',
 'P88179999195','여성용 더블업 멀티비타민, 영양소 2배 강화',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-20 00:00:00',30,6,2340,4.6,420,37990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179999195'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179999195'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179999195'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179999195'),'센트룸 우먼 더블업 112정',42000,37990,112,'600000000007','10x10x5mm','여성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179999195'),'센트룸 우먼 더블업 56정',25000,22900,56,'600000000008','10x10x5mm','여성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179999195')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179999195')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179999195'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179999195'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴트리 포유'),
 '뉴트리포유 듀얼 이뮨샷 멀티비타민 영양제 30개입 피로회복 액상 마시는 미네랄 종합영양제',
 'P88077354091','액상 멀티비타민, 피로회복 및 면역력 강화용',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 마그네슘, 칼슘, 철분',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-01 00:00:00',24,6,1570,4.3,187,54900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88077354091'),
 'https://shopping-phinf.pstatic.net/main_8807735/88077354091.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88077354091'),
 'https://shopping-phinf.pstatic.net/main_8807735/88077354091.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88077354091'),
 'https://shopping-phinf.pstatic.net/main_8807735/88077354091.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88077354091'),'뉴트리포유 30개입',60000,54900,30,'900000000001','액상 20ml','듀얼 이뮨샷 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88077354091'),'뉴트리포유 15개입',35000,32900,15,'900000000002','액상 20ml','듀얼 이뮨샷 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='뉴트리포유 30개입' AND product_id=(SELECT id FROM product WHERE code='P88077354091')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='뉴트리포유 15개입' AND product_id=(SELECT id FROM product WHERE code='P88077354091')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88077354091'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88077354091'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='아임비타'),
 '아임비타 멀티비타민 이뮨샷 선물세트 (액상 20ml + 정제 700mg + 캡슐 500mg) x 20개입, 1개',
 'P56465476117','액상, 정제, 캡슐을 함께 제공하는 이뮨샷 선물세트',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-15 00:00:00',24,6,1570,4.3,187,42300);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P56465476117'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P56465476117'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P56465476117'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P56465476117'),'아임비타 20개입',45000,42300,20,'100000000001','액상+정제+캡슐','이뮨샷 선물세트',6,NULL),
((SELECT id FROM product WHERE code='P56465476117'),'아임비타 10개입',25000,22900,10,'100000000002','액상+정제+캡슐','이뮨샷 선물세트',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='아임비타 20개입' AND product_id=(SELECT id FROM product WHERE code='P56465476117')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='아임비타 10개입' AND product_id=(SELECT id FROM product WHERE code='P56465476117')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P56465476117'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P56465476117'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='아임비타'),
 '종근당 아임비타 멀티비타민 이뮨플러스 14병 샷 마시는 액상 비타민 올인원영양제',
 'P88641950409','액상 비타민 샷, 올인원 영양제',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 엽산, 판토텐산, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-10 00:00:00',24,6,1890,4.4,280,22900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88641950409'),
 'https://shopping-phinf.pstatic.net/main_8864195/88641950409.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88641950409'),
 'https://shopping-phinf.pstatic.net/main_8864195/88641950409.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88641950409'),'아임비타 14병',25000,22900,14,'110000000001','액상 20ml','액상 비타민 샷',6,NULL),
((SELECT id FROM product WHERE code='P88641950409'),'아임비타 7병',15000,13900,7,'110000000002','액상 20ml','액상 비타민 샷',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='아임비타 14병' AND product_id=(SELECT id FROM product WHERE code='P88641950409')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='아임비타 7병' AND product_id=(SELECT id FROM product WHERE code='P88641950409')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88641950409'),(SELECT id FROM category WHERE name='종합비타민'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88641950409'),1890,'2025-01-26 11:45:00');


INSERT IGNORE INTO brand (name) VALUES ('고려은단');
INSERT IGNORE INTO brand (name) VALUES ('동아제약');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당건강'),
 '[1+1]종근당 아임비타 이뮨플러스 2박스 샷 올인원영양제 마시는 액상 비타민',
 'P86110690330_2','액상 비타민 샷, 1+1 이벤트로 2박스 제공, 면역력 강화',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 엽산, 판토텐산, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-05 00:00:00',24,6,1890,4.4,280,21500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86110690330_2'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86110690330_2'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86110690330_2'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86110690330_2'),'종근당 2박스 (1+1)',43000,21500,60,'700000000003','액상 20ml','액상 비타민 샷',6,NULL),
((SELECT id FROM product WHERE code='P86110690330_2'),'종근당 1박스',25000,22900,30,'700000000004','액상 20ml','액상 비타민 샷',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 2박스 (1+1)' AND product_id=(SELECT id FROM product WHERE code='P86110690330_2')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 1박스' AND product_id=(SELECT id FROM product WHERE code='P86110690330_2')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86110690330_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86110690330_2'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '오쏘몰 이뮨 멀티비타민 미네랄 (액상 20ml + 정제 919mg) x 30개입, 1개',
 'P51929317174_2','액상과 정제를 함께 제공하는 이뮨 멀티비타민, 면역력 강화',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-12 00:00:00',36,6,1570,4.3,187,80520);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929317174_2'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929317174_2'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929317174_2'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929317174_2'),'오쏘몰 30개입',85000,80520,30,'800000000003','액상+정제','이뮨 멀티비타민 미네랄',6,NULL),
((SELECT id FROM product WHERE code='P51929317174_2'),'오쏘몰 15개입',45000,42900,15,'800000000004','액상+정제','이뮨 멀티비타민 미네랄',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 30개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174_2')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 15개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174_2')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929317174_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929317174_2'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 맨 종합 멀티 비타민 112정 코스트코 남성 50+ 대용량 영양제',
 'P86788593520_2','50세 이상 남성을 위한 종합 멀티비타민, 면역력 강화 및 활력 증진에 도움',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,1890,4.5,320,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86788593520_2'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86788593520_2'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86788593520_2'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86788593520_2'),'112정',45000,39990,112,'600000000009','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86788593520_2'),'56정',25000,22900,56,'600000000010','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='112정' AND product_id=(SELECT id FROM product WHERE code='P86788593520_2')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='56정' AND product_id=(SELECT id FROM product WHERE code='P86788593520_2')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86788593520_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86788593520_2'),1890,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 포 맨 더블업 종합 멀티 비타민 112정 코스트코 남성 대용량 영양제',
 'P88179641598_2','남성용 더블업 멀티비타민, 영양소 2배 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분, 셀레늄',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-10 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179641598_2'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179641598_2'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179641598_2'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179641598_2'),'더블업 112정',45000,39990,112,'600000000011','10x10x5mm','남성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179641598_2'),'더블업 56정',25000,22900,56,'600000000012','10x10x5mm','남성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179641598_2')),
 203,'2025-01-26 09:15:00','2025-01-22 14:00:00'),
((SELECT id FROM product_variant WHERE variant_name='더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179641598_2')),
 127,'2025-01-26 09:15:00','2025-01-19 11:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179641598_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179641598_2'),2340,'2025-01-27 15:30:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '[유통기한 25년 11월 21일] 멀티비타민 오쏘몰 바이탈 F 30일분 + 쇼핑백',
 'P89432010130','멀티비타민 바이탈 F, 30일분 + 쇼핑백 제공',
 '하루 1정을 식후에 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-21 00:00:00',30,6,1570,4.3,187,60830);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P89432010130'),
 'https://shopping-phinf.pstatic.net/main_8943201/89432010130.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P89432010130'),
 'https://shopping-phinf.pstatic.net/main_8943201/89432010130.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P89432010130'),'오쏘몰 바이탈 F 30일분',65000,60830,30,'120000000001','10x10x5mm','멀티비타민 바이탈 F',6,NULL),
((SELECT id FROM product WHERE code='P89432010130'),'오쏘몰 바이탈 F 15일분',35000,32900,15,'120000000002','10x10x5mm','멀티비타민 바이탈 F',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 바이탈 F 30일분' AND product_id=(SELECT id FROM product WHERE code='P89432010130')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 바이탈 F 15일분' AND product_id=(SELECT id FROM product WHERE code='P89432010130')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P89432010130'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P89432010130'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 우먼 더블업 멀티비타민 112정 코스트코',
 'P88179999195_2','여성용 더블업 멀티비타민, 영양소 2배 강화',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-20 00:00:00',30,6,2340,4.6,420,37990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179999195_2'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179999195_2'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179999195_2'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179999195_2'),'센트룸 우먼 더블업 112정',42000,37990,112,'600000000013','10x10x5mm','여성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179999195_2'),'센트룸 우먼 더블업 56정',25000,22900,56,'600000000014','10x10x5mm','여성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179999195_2')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179999195_2')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179999195_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179999195_2'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 우먼 종합 멀티 비타민 영양제 112정 코스트코 비타민B12 대용량',
 'P86787803460_2','50세 이상 여성을 위한 종합 멀티비타민, 비타민B12 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86787803460_2'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86787803460_2'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86787803460_2'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86787803460_2'),'우먼 112정',45000,39990,112,'600000000015','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86787803460_2'),'우먼 56정',25000,22900,56,'600000000016','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='우먼 112정' AND product_id=(SELECT id FROM product WHERE code='P86787803460_2')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='우먼 56정' AND product_id=(SELECT id FROM product WHERE code='P86787803460_2')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86787803460_2'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86787803460_2'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 멀티비타민 올인원 1560mg x 60정, 3개',
 'P51929229300','멀티비타민 올인원, 3개 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-01 00:00:00',30,6,1570,4.3,187,59750);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929229300'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929229300'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929229300'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929229300'),'고려은단 3개 세트',65000,59750,180,'130000000001','10x10x5mm','멀티비타민 올인원',6,NULL),
((SELECT id FROM product WHERE code='P51929229300'),'고려은단 1개',25000,22900,60,'130000000002','10x10x5mm','멀티비타민 올인원',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 3개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929229300')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929229300')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929229300'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929229300'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='아임비타'),
 '아임비타 멀티비타민 이뮨플러스 (액상 20ml + 정제 600mg + 캡슐 500mg) x 7개입, 1개',
 'P51929307661','액상, 정제, 캡슐을 함께 제공하는 이뮨플러스',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-15 00:00:00',24,6,1570,4.3,187,9500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929307661'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929307661.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929307661'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929307661.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929307661'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929307661.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929307661'),'아임비타 7개입',12000,9500,7,'140000000001','액상+정제+캡슐','이뮨플러스',6,NULL),
((SELECT id FROM product WHERE code='P51929307661'),'아임비타 3개입',6000,5500,3,'140000000002','액상+정제+캡슐','이뮨플러스',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='아임비타 7개입' AND product_id=(SELECT id FROM product WHERE code='P51929307661')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='아임비타 3개입' AND product_id=(SELECT id FROM product WHERE code='P51929307661')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929307661'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929307661'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '오쏘몰 이뮨 멀티비타민 미네랄 (액상 20ml + 정제 919mg) x 7개입, 1개',
 'P51929498639','액상과 정제를 함께 제공하는 이뮨 멀티비타민',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-12 00:00:00',24,6,1570,4.3,187,27850);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929498639'),
 'https://shopping-phinf.pstatic.net/main_5192949/51929498639.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929498639'),
 'https://shopping-phinf.pstatic.net/main_5192949/51929498639.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929498639'),
 'https://shopping-phinf.pstatic.net/main_5192949/51929498639.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929498639'),'오쏘몰 7개입',30000,27850,7,'150000000001','액상+정제','이뮨 멀티비타민 미네랄',6,NULL),
((SELECT id FROM product WHERE code='P51929498639'),'오쏘몰 3개입',15000,13900,3,'150000000002','액상+정제','이뮨 멀티비타민 미네랄',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 7개입' AND product_id=(SELECT id FROM product WHERE code='P51929498639')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 3개입' AND product_id=(SELECT id FROM product WHERE code='P51929498639')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929498639'),(SELECT id FROM category WHERE name='비타민 A'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929498639'),1570,'2025-01-25 14:20:00');


INSERT IGNORE INTO brand (name) VALUES ('종근당');
INSERT IGNORE INTO brand (name) VALUES ('에코 뉴트리션');
INSERT IGNORE INTO brand (name) VALUES ('배석철의 아미나');
INSERT IGNORE INTO brand (name) VALUES ('칼맥');
INSERT IGNORE INTO brand (name) VALUES ('튼튼백서');
INSERT IGNORE INTO brand (name) VALUES ('솔가');
INSERT IGNORE INTO brand (name) VALUES ('파미나');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 고함량 비타민B 비타민비 군 영양제 활성 수용성 컴플렉스 B12 B1 B3 B6',
 'P81588702516','고함량 비타민B군 영양제, 활성 수용성 컴플렉스',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',24,6,1890,4.4,280,14900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P81588702516'),
 'https://shopping-phinf.pstatic.net/main_8158870/81588702516.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P81588702516'),
 'https://shopping-phinf.pstatic.net/main_8158870/81588702516.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P81588702516'),
 'https://shopping-phinf.pstatic.net/main_8158870/81588702516.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P81588702516'),'종근당 60정',18000,14900,60,'160000000001','10x10x5mm','고함량 비타민B군',6,NULL),
((SELECT id FROM product WHERE code='P81588702516'),'종근당 120정',32000,28900,120,'160000000002','10x10x5mm','고함량 비타민B군',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 60정' AND product_id=(SELECT id FROM product WHERE code='P81588702516')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 120정' AND product_id=(SELECT id FROM product WHERE code='P81588702516')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P81588702516'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P81588702516'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='에코 뉴트리션'),
 '에코뉴트리션 3개월분 캐나다산 비타민B B12 B1 B2 B3 B6 B군 컴플렉스 영양제',
 'P83373983481','캐나다산 비타민B군 컴플렉스, 3개월분',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-20 00:00:00',36,6,1570,4.3,187,19800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83373983481'),
 'https://shopping-phinf.pstatic.net/main_8337398/83373983481.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83373983481'),
 'https://shopping-phinf.pstatic.net/main_8337398/83373983481.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P83373983481'),
 'https://shopping-phinf.pstatic.net/main_8337398/83373983481.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83373983481'),'에코뉴트리션 90정',23000,19800,90,'170000000001','10x10x5mm','캐나다산 비타민B군',6,NULL),
((SELECT id FROM product WHERE code='P83373983481'),'에코뉴트리션 180정',42000,38900,180,'170000000002','10x10x5mm','캐나다산 비타민B군',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='에코뉴트리션 90정' AND product_id=(SELECT id FROM product WHERE code='P83373983481')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='에코뉴트리션 180정' AND product_id=(SELECT id FROM product WHERE code='P83373983481')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83373983481'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83373983481'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 메가도스B 비타민B군 850mg x 60정, 3개',
 'P51929354084','메가도스B 비타민B군, 3개 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-05 00:00:00',30,6,1570,4.3,187,46120);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929354084'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354084.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929354084'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354084.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929354084'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354084.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929354084'),'고려은단 3개 세트',52000,46120,180,'180000000001','10x10x5mm','메가도스B 비타민B군',6,NULL),
((SELECT id FROM product WHERE code='P51929354084'),'고려은단 1개',20000,18900,60,'180000000002','10x10x5mm','메가도스B 비타민B군',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 3개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929354084')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929354084')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929354084'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929354084'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='배석철의 아미나'),
 '배석철의 아미나 고용량 비타민B3 500mg 미국산 니코틴산아미드 100캡슐, 1개',
 'P88837633834','고용량 비타민B3, 미국산 니코틴산아미드 500mg',
 '하루 1-2캡슐을 식후에 섭취하세요.','니코틴산아미드 500mg, 비타민B3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-01 00:00:00',24,6,1570,4.3,187,48000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88837633834'),
 'https://shopping-phinf.pstatic.net/main_8883763/88837633834.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88837633834'),
 'https://shopping-phinf.pstatic.net/main_8883763/88837633834.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88837633834'),
 'https://shopping-phinf.pstatic.net/main_8883763/88837633834.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88837633834'),'배석철의 아미나 100캡슐',55000,48000,100,'190000000001','10x10x5mm','고용량 비타민B3',6,NULL),
((SELECT id FROM product WHERE code='P88837633834'),'배석철의 아미나 50캡슐',30000,27900,50,'190000000002','10x10x5mm','고용량 비타민B3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='배석철의 아미나 100캡슐' AND product_id=(SELECT id FROM product WHERE code='P88837633834')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='배석철의 아미나 50캡슐' AND product_id=(SELECT id FROM product WHERE code='P88837633834')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88837633834'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88837633834'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='칼맥'),
 '종근당 비타민B군 비군 고함량 B3 B5 B7 B9 B12 영양제 수용성 컴플렉스',
 'P82310385955','고함량 비타민B군, 수용성 컴플렉스',
 '하루 1정을 식후에 섭취하세요.','비타민 B3, B5, B7, B9, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-25 00:00:00',24,6,1890,4.4,280,14900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82310385955'),
 'https://shopping-phinf.pstatic.net/main_8231038/82310385955.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82310385955'),
 'https://shopping-phinf.pstatic.net/main_8231038/82310385955.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82310385955'),'칼맥 60정',18000,14900,60,'200000000001','10x10x5mm','고함량 비타민B군',6,NULL),
((SELECT id FROM product WHERE code='P82310385955'),'칼맥 120정',32000,28900,120,'200000000002','10x10x5mm','고함량 비타민B군',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='칼맥 60정' AND product_id=(SELECT id FROM product WHERE code='P82310385955')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='칼맥 120정' AND product_id=(SELECT id FROM product WHERE code='P82310385955')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82310385955'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82310385955'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='튼튼백서'),
 '튼튼백서 활력 튼튼 비타민B 컴플렉스 500mg x 90정, 1개',
 'P51929252328','활력 튼튼 비타민B 컴플렉스, 500mg',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-10 00:00:00',24,6,1570,4.3,187,9900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929252328'),
 'https://shopping-phinf.pstatic.net/main_5192925/51929252328.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929252328'),
 'https://shopping-phinf.pstatic.net/main_5192925/51929252328.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929252328'),
 'https://shopping-phinf.pstatic.net/main_5192925/51929252328.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929252328'),'튼튼백서 90정',12000,9900,90,'210000000001','10x10x5mm','활력 튼튼 비타민B',6,NULL),
((SELECT id FROM product WHERE code='P51929252328'),'튼튼백서 180정',22000,19900,180,'210000000002','10x10x5mm','활력 튼튼 비타민B',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='튼튼백서 90정' AND product_id=(SELECT id FROM product WHERE code='P51929252328')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='튼튼백서 180정' AND product_id=(SELECT id FROM product WHERE code='P51929252328')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929252328'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929252328'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='솔가'),
 '솔가 비타민 B 컴플렉스 100 100베지캡슐',
 'P35322629619','솔가 비타민B 컴플렉스, 100베지캡슐',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-30 00:00:00',24,6,1890,4.4,280,16800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P35322629619'),
 'https://shopping-phinf.pstatic.net/main_3532262/35322629619.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P35322629619'),
 'https://shopping-phinf.pstatic.net/main_3532262/35322629619.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P35322629619'),
 'https://shopping-phinf.pstatic.net/main_3532262/35322629619.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P35322629619'),'솔가 100캡슐',20000,16800,100,'220000000001','10x10x5mm','솔가 비타민B 컴플렉스',6,NULL),
((SELECT id FROM product WHERE code='P35322629619'),'솔가 200캡슐',38000,34900,200,'220000000002','10x10x5mm','솔가 비타민B 컴플렉스',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='솔가 100캡슐' AND product_id=(SELECT id FROM product WHERE code='P35322629619')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='솔가 200캡슐' AND product_id=(SELECT id FROM product WHERE code='P35322629619')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P35322629619'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P35322629619'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='파미나'),
 '배석철의 아미나 고용량 비타민B3 500mg 미국산 니코틴산아미드 3개 묶음 선물세트',
 'P89324158747','고용량 비타민B3, 3개 묶음 선물세트',
 '하루 1-2캡슐을 식후에 섭취하세요.','니코틴산아미드 500mg, 비타민B3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-20 00:00:00',24,6,1570,4.3,187,121500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P89324158747'),
 'https://shopping-phinf.pstatic.net/main_8932415/89324158747.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P89324158747'),
 'https://shopping-phinf.pstatic.net/main_8932415/89324158747.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P89324158747'),
 'https://shopping-phinf.pstatic.net/main_8932415/89324158747.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P89324158747'),'파미나 3개 세트',135000,121500,300,'230000000001','10x10x5mm','고용량 비타민B3 세트',6,NULL),
((SELECT id FROM product WHERE code='P89324158747'),'파미나 1개',50000,45900,100,'230000000002','10x10x5mm','고용량 비타민B3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='파미나 3개 세트' AND product_id=(SELECT id FROM product WHERE code='P89324158747')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='파미나 1개' AND product_id=(SELECT id FROM product WHERE code='P89324158747')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P89324158747'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P89324158747'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='솔가'),
 '솔가 비타민B 컴플렉스 100 250베지캡슐',
 'P36465556619','솔가 비타민B 컴플렉스, 250베지캡슐',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-10 00:00:00',24,6,1890,4.4,280,34280);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P36465556619'),
 'https://shopping-phinf.pstatic.net/main_3646555/36465556619.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P36465556619'),
 'https://shopping-phinf.pstatic.net/main_3646555/36465556619.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P36465556619'),
 'https://shopping-phinf.pstatic.net/main_3646555/36465556619.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P36465556619'),'솔가 250캡슐',38000,34280,250,'240000000001','10x10x5mm','솔가 비타민B 컴플렉스',6,NULL),
((SELECT id FROM product WHERE code='P36465556619'),'솔가 500캡슐',72000,65900,500,'240000000002','10x10x5mm','솔가 비타민B 컴플렉스',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='솔가 250캡슐' AND product_id=(SELECT id FROM product WHERE code='P36465556619')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='솔가 500캡슐' AND product_id=(SELECT id FROM product WHERE code='P36465556619')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P36465556619'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P36465556619'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 활력 비타민B 플러스 800mg x 60정, 2개',
 'P51929174017','활력 비타민B 플러스, 2개 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','비타민 B1, B2, B3, B6, B12, 엽산, 판토텐산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-15 00:00:00',24,6,1890,4.4,280,25900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929174017'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929174017.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929174017'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929174017.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929174017'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929174017.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929174017'),'종근당 2개 세트',30000,25900,120,'250000000001','10x10x5mm','활력 비타민B 플러스',6,NULL),
((SELECT id FROM product WHERE code='P51929174017'),'종근당 1개',16000,14900,60,'250000000002','10x10x5mm','활력 비타민B 플러스',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 2개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929174017')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 1개' AND product_id=(SELECT id FROM product WHERE code='P51929174017')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929174017'),(SELECT id FROM category WHERE name='비타민 B'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929174017'),1890,'2025-01-26 11:45:00');


INSERT IGNORE INTO brand (name) VALUES ('고려은단비타민C1000');
INSERT IGNORE INTO brand (name) VALUES ('ALLBARUN');
INSERT IGNORE INTO brand (name) VALUES ('닥터가');
INSERT IGNORE INTO brand (name) VALUES ('유한양행');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단비타민C1000'),
 '고려은단비타민C1000 1080mg x 600정, 1개',
 'P51929305232','고려은단 비타민C1000, 1080mg 고함량',
 '하루 1-2정을 식후에 섭취하세요.','아스코르빈산 1080mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-03 00:00:00',30,6,1890,4.4,280,40480);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929305232'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929305232.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929305232'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929305232.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929305232'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929305232.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929305232'),'고려은단 600정',45000,40480,600,'260000000001','10x10x5mm','비타민C 1080mg',6,NULL),
((SELECT id FROM product WHERE code='P51929305232'),'고려은단 300정',25000,22900,300,'260000000002','10x10x5mm','비타민C 1080mg',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 600정' AND product_id=(SELECT id FROM product WHERE code='P51929305232')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 300정' AND product_id=(SELECT id FROM product WHERE code='P51929305232')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929305232'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929305232'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 메가도스C 비타민C 3000mg 3g x 100포, 2개',
 'P51929176649','메가도스C 비타민C, 3000mg 고함량',
 '하루 1-2포를 물에 타서 섭취하세요.','아스코르빈산 3000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1570,4.3,187,49900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929176649'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929176649.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929176649'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929176649.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929176649'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929176649.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929176649'),'고려은단 2개 세트',55000,49900,200,'270000000001','포장 3g','메가도스C 비타민C',6,NULL),
((SELECT id FROM product WHERE code='P51929176649'),'고려은단 1개',30000,27900,100,'270000000002','포장 3g','메가도스C 비타민C',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 2개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929176649')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929176649')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929176649'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929176649'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 고려은단비타민C1000 이지 + 비타민D 600mg x 180정, 2개',
 'P51929436663','비타민C1000 이지 + 비타민D, 2개 묶음 세트',
 '하루 1-2정을 식후에 섭취하세요.','아스코르빈산 1000mg, 비타민D 600mg',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-02-18 00:00:00',30,6,1890,4.4,280,29900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929436663'),
 'https://shopping-phinf.pstatic.net/main_5192943/51929436663.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929436663'),
 'https://shopping-phinf.pstatic.net/main_5192943/51929436663.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929436663'),
 'https://shopping-phinf.pstatic.net/main_5192943/51929436663.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929436663'),'고려은단 2개 세트',35000,29900,360,'280000000001','10x10x5mm','비타민C1000 + 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929436663'),'고려은단 1개',20000,18900,180,'280000000002','10x10x5mm','비타민C1000 + 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 2개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929436663')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929436663')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929436663'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929436663'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 메가도스C 2000 2g x 100포, 2개',
 'P51929173380','메가도스C 2000, 2g 고함량',
 '하루 1-2포를 물에 타서 섭취하세요.','아스코르빈산 2000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1570,4.3,187,35900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929173380'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929173380.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929173380'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929173380.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929173380'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929173380.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929173380'),'고려은단 2개 세트',40000,35900,200,'290000000001','포장 2g','메가도스C 2000',6,NULL),
((SELECT id FROM product WHERE code='P51929173380'),'고려은단 1개',25000,22900,100,'290000000002','포장 2g','메가도스C 2000',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 2개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929173380')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929173380')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929173380'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929173380'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='ALLBARUN'),
 'ALLBARUN 올바른 맥스 비타민C 3000 3g x 90포, 1개',
 'P51929519350','올바른 맥스 비타민C, 3000mg 고함량',
 '하루 1-2포를 물에 타서 섭취하세요.','아스코르빈산 3000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-02-05 00:00:00',30,6,1570,4.3,187,12900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929519350'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929519350.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929519350'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929519350.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929519350'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929519350.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929519350'),'ALLBARUN 90포',15000,12900,90,'300000000001','포장 3g','올바른 맥스 비타민C',6,NULL),
((SELECT id FROM product WHERE code='P51929519350'),'ALLBARUN 45포',8000,7500,45,'300000000002','포장 3g','올바른 맥스 비타민C',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='ALLBARUN 90포' AND product_id=(SELECT id FROM product WHERE code='P51929519350')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='ALLBARUN 45포' AND product_id=(SELECT id FROM product WHERE code='P51929519350')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929519350'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929519350'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당건강'),
 '비타민c 종근당 1000mg 600정 바이탈프로그램 비타민씨 선물용 아스코르빈산',
 'P83642336651','종근당 비타민C, 1000mg 고함량',
 '하루 1-2정을 식후에 섭취하세요.','아스코르빈산 1000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,28500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83642336651'),
 'https://shopping-phinf.pstatic.net/main_8364233/83642336651.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83642336651'),
 'https://shopping-phinf.pstatic.net/main_8364233/83642336651.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P83642336651'),
 'https://shopping-phinf.pstatic.net/main_8364233/83642336651.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83642336651'),'종근당 600정',32000,28500,600,'310000000001','10x10x5mm','비타민C 1000mg',6,NULL),
((SELECT id FROM product WHERE code='P83642336651'),'종근당 300정',18000,16900,300,'310000000002','10x10x5mm','비타민C 1000mg',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 600정' AND product_id=(SELECT id FROM product WHERE code='P83642336651')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 300정' AND product_id=(SELECT id FROM product WHERE code='P83642336651')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83642336651'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83642336651'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단비타민C1000'),
 '고려은단비타민C1000 1080mg x 300정, 1개',
 'P51929260810','고려은단 비타민C1000, 1080mg 고함량',
 '하루 1-2정을 식후에 섭취하세요.','아스코르빈산 1080mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-03 00:00:00',30,6,1890,4.4,280,24510);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929260810'),
 'https://shopping-phinf.pstatic.net/main_5192926/51929260810.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929260810'),
 'https://shopping-phinf.pstatic.net/main_5192926/51929260810.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929260810'),
 'https://shopping-phinf.pstatic.net/main_5192926/51929260810.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929260810'),'고려은단 300정',28000,24510,300,'320000000001','10x10x5mm','비타민C 1080mg',6,NULL),
((SELECT id FROM product WHERE code='P51929260810'),'고려은단 150정',15000,13900,150,'320000000002','10x10x5mm','비타민C 1080mg',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 300정' AND product_id=(SELECT id FROM product WHERE code='P51929260810')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 150정' AND product_id=(SELECT id FROM product WHERE code='P51929260810')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929260810'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929260810'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터가'),
 '닥터가 비타도스C 3g x 30포, 1개',
 'P55697462292','닥터가 비타도스C, 3g 고함량',
 '하루 1-2포를 물에 타서 섭취하세요.','아스코르빈산 3000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-07-08 00:00:00',30,6,1570,4.3,187,3900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P55697462292'),
 'https://shopping-phinf.pstatic.net/main_5569746/55697462292.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P55697462292'),
 'https://shopping-phinf.pstatic.net/main_5569746/55697462292.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P55697462292'),
 'https://shopping-phinf.pstatic.net/main_5569746/55697462292.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P55697462292'),'닥터가 30포',5000,3900,30,'330000000001','포장 3g','비타도스C',6,NULL),
((SELECT id FROM product WHERE code='P55697462292'),'닥터가 15포',3000,2500,15,'330000000002','포장 3g','비타도스C',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터가 30포' AND product_id=(SELECT id FROM product WHERE code='P55697462292')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터가 15포' AND product_id=(SELECT id FROM product WHERE code='P55697462292')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P55697462292'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P55697462292'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 비타민C 1000mg 1100mg x 600정, 1개',
 'P51929353863','종근당 비타민C, 1100mg 고함량',
 '하루 1-2정을 식후에 섭취하세요.','아스코르빈산 1100mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-07-11 00:00:00',30,6,1890,4.4,280,29270);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929353863'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929353863.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929353863'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929353863.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929353863'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929353863.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929353863'),'종근당 600정',33000,29270,600,'340000000001','10x10x5mm','비타민C 1100mg',6,NULL),
((SELECT id FROM product WHERE code='P51929353863'),'종근당 300정',18000,16900,300,'340000000002','10x10x5mm','비타민C 1100mg',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 600정' AND product_id=(SELECT id FROM product WHERE code='P51929353863')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 300정' AND product_id=(SELECT id FROM product WHERE code='P51929353863')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929353863'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929353863'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='유한양행'),
 '유한양행 비타 바이탈씨 유한 비타민C 가루 1000mg 120포',
 'P82342426626','유한양행 비타 바이탈씨, 1000mg 가루형',
 '하루 1-2포를 물에 타서 섭취하세요.','아스코르빈산 1000mg, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1570,4.3,187,9400);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82342426626'),
 'https://shopping-phinf.pstatic.net/main_8234242/82342426626.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82342426626'),
 'https://shopping-phinf.pstatic.net/main_8234242/82342426626.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P82342426626'),
 'https://shopping-phinf.pstatic.net/main_8234242/82342426626.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82342426626'),'유한양행 120포',12000,9400,120,'350000000001','포장 1g','비타 바이탈씨',6,NULL),
((SELECT id FROM product WHERE code='P82342426626'),'유한양행 60포',7000,6500,60,'350000000002','포장 1g','비타 바이탈씨',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='유한양행 120포' AND product_id=(SELECT id FROM product WHERE code='P82342426626')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='유한양행 60포' AND product_id=(SELECT id FROM product WHERE code='P82342426626')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82342426626'),(SELECT id FROM category WHERE name='비타민 C'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82342426626'),1570,'2025-01-25 14:20:00');


INSERT IGNORE INTO brand (name) VALUES ('더리얼');
INSERT IGNORE INTO brand (name) VALUES ('포뉴');
INSERT IGNORE INTO brand (name) VALUES ('바료랑');
INSERT IGNORE INTO brand (name) VALUES ('닥터파이토');
INSERT IGNORE INTO brand (name) VALUES ('뉴트리모어');
INSERT IGNORE INTO brand (name) VALUES ('뉴네이처');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 비타민d 2000iu 임산부 성인 비타민d3 3개월분',
 'P85782904066','종근당 비타민D, 2000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 2000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,11900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P85782904066'),
 'https://shopping-phinf.pstatic.net/main_8578290/85782904066.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P85782904066'),
 'https://shopping-phinf.pstatic.net/main_8578290/85782904066.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P85782904066'),
 'https://shopping-phinf.pstatic.net/main_8578290/85782904066.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P85782904066'),'종근당 90정',14000,11900,90,'360000000001','10x10x5mm','비타민D3 2000IU',6,NULL),
((SELECT id FROM product WHERE code='P85782904066'),'종근당 180정',25000,22900,180,'360000000002','10x10x5mm','비타민D3 2000IU',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 90정' AND product_id=(SELECT id FROM product WHERE code='P85782904066')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 180정' AND product_id=(SELECT id FROM product WHERE code='P85782904066')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P85782904066'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P85782904066'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='더리얼'),
 '더리얼 비타민D3 5000IU 성인 어린이 아기 임산부',
 'P81386103221','더리얼 비타민D3, 5000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 5000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,14900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P81386103221'),
 'https://shopping-phinf.pstatic.net/main_8138610/81386103221.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P81386103221'),
 'https://shopping-phinf.pstatic.net/main_8138610/81386103221.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P81386103221'),
 'https://shopping-phinf.pstatic.net/main_8138610/81386103221.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P81386103221'),'더리얼 60정',18000,14900,60,'370000000001','10x10x5mm','비타민D3 5000IU',6,NULL),
((SELECT id FROM product WHERE code='P81386103221'),'더리얼 120정',32000,28900,120,'370000000002','10x10x5mm','비타민D3 5000IU',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='더리얼 60정' AND product_id=(SELECT id FROM product WHERE code='P81386103221')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='더리얼 120정' AND product_id=(SELECT id FROM product WHERE code='P81386103221')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P81386103221'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P81386103221'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='포뉴'),
 '포뉴 비타민D 1000 450mg x 60정, 1개',
 'P51929122769','포뉴 비타민D, 1000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 1000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1890,4.4,280,20000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929122769'),
 'https://shopping-phinf.pstatic.net/main_5192912/51929122769.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929122769'),
 'https://shopping-phinf.pstatic.net/main_5192912/51929122769.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929122769'),
 'https://shopping-phinf.pstatic.net/main_5192912/51929122769.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929122769'),'포뉴 60정',23000,20000,60,'380000000001','10x10x5mm','비타민D 1000IU',6,NULL),
((SELECT id FROM product WHERE code='P51929122769'),'포뉴 120정',42000,38900,120,'380000000002','10x10x5mm','비타민D 1000IU',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='포뉴 60정' AND product_id=(SELECT id FROM product WHERE code='P51929122769')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='포뉴 120정' AND product_id=(SELECT id FROM product WHERE code='P51929122769')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929122769'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929122769'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='바료랑'),
 '아기비타민D 유아 어린이 드롭 액상 비타민D3 400IU',
 'P89179871015','아기비타민D, 유아 어린이용 드롭',
 '하루 1-2방울을 섭취하세요.','콜레칼시페롤 400IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,19800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P89179871015'),
 'https://shopping-phinf.pstatic.net/main_8917987/89179871015.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P89179871015'),
 'https://shopping-phinf.pstatic.net/main_8917987/89179871015.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P89179871015'),'바료랑 30ml',22000,19800,30,'390000000001','드롭 1ml','아기비타민D 드롭',6,NULL),
((SELECT id FROM product WHERE code='P89179871015'),'바료랑 60ml',40000,36900,60,'390000000002','드롭 1ml','아기비타민D 드롭',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='바료랑 30ml' AND product_id=(SELECT id FROM product WHERE code='P89179871015')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='바료랑 60ml' AND product_id=(SELECT id FROM product WHERE code='P89179871015')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P89179871015'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P89179871015'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='ALLBARUN'),
 'ALLBARUN 올바른 비타민D 5000IU 300mg x 180정, 1개',
 'P51929216786','올바른 비타민D, 5000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 5000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-04-07 00:00:00',30,6,1890,4.4,280,11900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929216786'),
 'https://shopping-phinf.pstatic.net/main_5192921/51929216786.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929216786'),
 'https://shopping-phinf.pstatic.net/main_5192921/51929216786.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929216786'),
 'https://shopping-phinf.pstatic.net/main_5192921/51929216786.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929216786'),'ALLBARUN 180정',14000,11900,180,'400000000001','10x10x5mm','올바른 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929216786'),'ALLBARUN 90정',8000,7500,90,'400000000002','10x10x5mm','올바른 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='ALLBARUN 180정' AND product_id=(SELECT id FROM product WHERE code='P51929216786')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='ALLBARUN 90정' AND product_id=(SELECT id FROM product WHERE code='P51929216786')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929216786'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929216786'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터파이토'),
 '닥터파이토 유기농 비타민D3 300mg x 60정, 3개',
 'P54857997651','닥터파이토 유기농 비타민D3, 3개 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 300mg, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-05-19 00:00:00',30,6,1890,4.4,280,101900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P54857997651'),
 'https://shopping-phinf.pstatic.net/main_5485799/54857997651.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P54857997651'),
 'https://shopping-phinf.pstatic.net/main_5485799/54857997651.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P54857997651'),
 'https://shopping-phinf.pstatic.net/main_5485799/54857997651.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P54857997651'),'닥터파이토 3개 세트',120000,101900,180,'410000000001','10x10x5mm','유기농 비타민D3',6,NULL),
((SELECT id FROM product WHERE code='P54857997651'),'닥터파이토 1개',45000,39900,60,'410000000002','10x10x5mm','유기농 비타민D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터파이토 3개 세트' AND product_id=(SELECT id FROM product WHERE code='P54857997651')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터파이토 1개' AND product_id=(SELECT id FROM product WHERE code='P54857997651')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P54857997651'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P54857997651'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴트리모어'),
 '뉴트리모어 퓨어 비타민D 1000IU 130mg x 30캡슐, 1개',
 'P56571910806','뉴트리모어 퓨어 비타민D, 1000IU 고함량',
 '하루 1캡슐을 식후에 섭취하세요.','콜레칼시페롤 1000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-09-03 00:00:00',30,6,1890,4.4,280,14000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P56571910806'),
 'https://shopping-phinf.pstatic.net/main_5657191/56571910806.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P56571910806'),
 'https://shopping-phinf.pstatic.net/main_5657191/56571910806.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P56571910806'),
 'https://shopping-phinf.pstatic.net/main_5657191/56571910806.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P56571910806'),'뉴트리모어 30캡슐',16000,14000,30,'420000000001','10x10x5mm','퓨어 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P56571910806'),'뉴트리모어 60캡슐',30000,27900,60,'420000000002','10x10x5mm','퓨어 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='뉴트리모어 30캡슐' AND product_id=(SELECT id FROM product WHERE code='P56571910806')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='뉴트리모어 60캡슐' AND product_id=(SELECT id FROM product WHERE code='P56571910806')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P56571910806'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P56571910806'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 메가도스D 비타민D3 2000IU 880mg x 90정, 1개',
 'P51929303218','메가도스D 비타민D3, 2000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 2000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1890,4.4,280,9900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929303218'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929303218.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929303218'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929303218.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929303218'),
 'https://shopping-phinf.pstatic.net/main_5192930/51929303218.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929303218'),'고려은단 90정',12000,9900,90,'430000000001','10x10x5mm','메가도스D 비타민D3',6,NULL),
((SELECT id FROM product WHERE code='P51929303218'),'고려은단 180정',22000,19900,180,'430000000002','10x10x5mm','메가도스D 비타민D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 90정' AND product_id=(SELECT id FROM product WHERE code='P51929303218')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 180정' AND product_id=(SELECT id FROM product WHERE code='P51929303218')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929303218'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929303218'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 메가도스D 비타민D3 4000IU 200mg x 120정, 1개',
 'P51929363932','메가도스D 비타민D3, 4000IU 고함량',
 '하루 1정을 식후에 섭취하세요.','콜레칼시페롤 4000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1890,4.4,280,11500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929363932'),
 'https://shopping-phinf.pstatic.net/main_5192936/51929363932.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929363932'),
 'https://shopping-phinf.pstatic.net/main_5192936/51929363932.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929363932'),
 'https://shopping-phinf.pstatic.net/main_5192936/51929363932.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929363932'),'고려은단 120정',13000,11500,120,'440000000001','10x10x5mm','메가도스D 비타민D3',6,NULL),
((SELECT id FROM product WHERE code='P51929363932'),'고려은단 240정',24000,22900,240,'440000000002','10x10x5mm','메가도스D 비타민D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 120정' AND product_id=(SELECT id FROM product WHERE code='P51929363932')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 240정' AND product_id=(SELECT id FROM product WHERE code='P51929363932')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929363932'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929363932'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴네이처'),
 '뉴네이처 인생 비타민D3 5000IU 150mg x 180캡슐, 1개',
 'P51929470346','뉴네이처 인생 비타민D3, 5000IU 고함량',
 '하루 1캡슐을 식후에 섭취하세요.','콜레칼시페롤 5000IU, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-13 00:00:00',30,6,1890,4.4,280,15120);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929470346'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929470346.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929470346'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929470346.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929470346'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929470346.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929470346'),'뉴네이처 180캡슐',17000,15120,180,'450000000001','10x10x5mm','인생 비타민D3',6,NULL),
((SELECT id FROM product WHERE code='P51929470346'),'뉴네이처 90캡슐',9000,8500,90,'450000000002','10x10x5mm','인생 비타민D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='뉴네이처 180캡슐' AND product_id=(SELECT id FROM product WHERE code='P51929470346')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='뉴네이처 90캡슐' AND product_id=(SELECT id FROM product WHERE code='P51929470346')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929470346'),(SELECT id FROM category WHERE name='비타민 D'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929470346'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당건강'),
 '[1+1]종근당 아임비타 이뮨플러스 2박스 샷 올인원영양제 마시는 액상 비타민',
 'P86110690330_3','액상 비타민 샷, 1+1 이벤트로 2박스 제공, 면역력 강화',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 엽산, 판토텐산, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-05 00:00:00',24,6,1890,4.4,280,21500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86110690330_3'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86110690330_3'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86110690330_3'),
 'https://shopping-phinf.pstatic.net/main_8611069/86110690330.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86110690330_3'),'종근당 2박스 (1+1)',43000,21500,60,'700000000005','액상 20ml','액상 비타민 샷',6,NULL),
((SELECT id FROM product WHERE code='P86110690330_3'),'종근당 1박스',25000,22900,30,'700000000006','액상 20ml','액상 비타민 샷',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='종근당 2박스 (1+1)' AND product_id=(SELECT id FROM product WHERE code='P86110690330_3')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='종근당 1박스' AND product_id=(SELECT id FROM product WHERE code='P86110690330_3')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86110690330_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86110690330_3'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '오쏘몰 이뮨 멀티비타민 미네랄 (액상 20ml + 정제 919mg) x 30개입, 1개',
 'P51929317174_3','액상과 정제를 함께 제공하는 이뮨 멀티비타민, 면역력 강화',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-12 00:00:00',36,6,1570,4.3,187,80520);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929317174_3'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929317174_3'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929317174_3'),
 'https://shopping-phinf.pstatic.net/main_5192931/51929317174.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929317174_3'),'오쏘몰 30개입',85000,80520,30,'800000000005','액상+정제','이뮨 멀티비타민 미네랄',6,NULL),
((SELECT id FROM product WHERE code='P51929317174_3'),'오쏘몰 15개입',45000,42900,15,'800000000006','액상+정제','이뮨 멀티비타민 미네랄',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 30개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174_3')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 15개입' AND product_id=(SELECT id FROM product WHERE code='P51929317174_3')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929317174_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929317174_3'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='아임비타'),
 '아임비타 멀티비타민 이뮨샷 선물세트 (액상 20ml + 정제 700mg + 캡슐 500mg) x 20개입, 1개',
 'P56465476117_2','액상, 정제, 캡슐을 함께 제공하는 이뮨샷 선물세트',
 '하루 1개씩 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-15 00:00:00',24,6,1570,4.3,187,42300);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P56465476117_2'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P56465476117_2'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P56465476117_2'),
 'https://shopping-phinf.pstatic.net/main_5646547/56465476117.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P56465476117_2'),'아임비타 20개입',45000,42300,20,'100000000003','액상+정제+캡슐','이뮨샷 선물세트',6,NULL),
((SELECT id FROM product WHERE code='P56465476117_2'),'아임비타 10개입',25000,22900,10,'100000000004','액상+정제+캡슐','이뮨샷 선물세트',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='아임비타 20개입' AND product_id=(SELECT id FROM product WHERE code='P56465476117_2')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='아임비타 10개입' AND product_id=(SELECT id FROM product WHERE code='P56465476117_2')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P56465476117_2'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P56465476117_2'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 맨 종합 멀티 비타민 112정 코스트코 남성 50+ 대용량 영양제',
 'P86788593520_3','50세 이상 남성을 위한 종합 멀티비타민, 면역력 강화 및 활력 증진에 도움',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,1890,4.5,320,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86788593520_3'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86788593520_3'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86788593520_3'),
 'https://shopping-phinf.pstatic.net/main_8678859/86788593520.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86788593520_3'),'112정',45000,39990,112,'600000000017','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86788593520_3'),'56정',25000,22900,56,'600000000018','10x10x5mm','남성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='112정' AND product_id=(SELECT id FROM product WHERE code='P86788593520_3')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='56정' AND product_id=(SELECT id FROM product WHERE code='P86788593520_3')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86788593520_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86788593520_3'),1890,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 포 맨 더블업 종합 멀티 비타민 112정 코스트코 남성 대용량 영양제',
 'P88179641598_3','남성용 더블업 멀티비타민, 영양소 2배 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, 아연, 마그네슘, 칼슘, 철분, 셀레늄',
 '과다섭취 시 부작용이 있을 수 있습니다. 특정 질환이 있는 경우 의사와 상담 후 섭취하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-10 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179641598_3'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179641598_3'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179641598_3'),
 'https://shopping-phinf.pstatic.net/main_8817964/88179641598.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179641598_3'),'더블업 112정',45000,39990,112,'600000000019','10x10x5mm','남성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179641598_3'),'더블업 56정',25000,22900,56,'600000000020','10x10x5mm','남성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179641598_3')),
 203,'2025-01-26 09:15:00','2025-01-22 14:00:00'),
((SELECT id FROM product_variant WHERE variant_name='더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179641598_3')),
 127,'2025-01-26 09:15:00','2025-01-19 11:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179641598_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179641598_3'),2340,'2025-01-27 15:30:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='아임비타'),
 '종근당 아임비타 멀티비타민 이뮨플러스 14병 샷 마시는 액상 비타민 올인원영양제',
 'P88641950409_2','액상 비타민 샷, 올인원 영양제',
 '하루 1개씩 물에 타서 섭취하세요.','비타민 C, D, B군, 아연, 엽산, 판토텐산, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-10 00:00:00',24,6,1890,4.4,280,22900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88641950409_2'),
 'https://shopping-phinf.pstatic.net/main_8864195/88641950409.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88641950409_2'),
 'https://shopping-phinf.pstatic.net/main_8864195/88641950409.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88641950409_2'),'아임비타 14병',25000,22900,14,'110000000003','액상 20ml','액상 비타민 샷',6,NULL),
((SELECT id FROM product WHERE code='P88641950409_2'),'아임비타 7병',15000,13900,7,'110000000004','액상 20ml','액상 비타민 샷',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='아임비타 14병' AND product_id=(SELECT id FROM product WHERE code='P88641950409_2')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='아임비타 7병' AND product_id=(SELECT id FROM product WHERE code='P88641950409_2')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88641950409_2'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88641950409_2'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='오쏘몰'),
 '[유통기한 25년 11월 21일] 멀티비타민 오쏘몰 바이탈 F 30일분 + 쇼핑백',
 'P89432010130_2','멀티비타민 바이탈 F, 30일분 + 쇼핑백 제공',
 '하루 1정을 식후에 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-21 00:00:00',30,6,1570,4.3,187,60830);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P89432010130_2'),
 'https://shopping-phinf.pstatic.net/main_8943201/89432010130.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P89432010130_2'),
 'https://shopping-phinf.pstatic.net/main_8943201/89432010130.2.jpg','상세 이미지 1',1,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P89432010130_2'),'오쏘몰 바이탈 F 30일분',65000,60830,30,'120000000003','10x10x5mm','멀티비타민 바이탈 F',6,NULL),
((SELECT id FROM product WHERE code='P89432010130_2'),'오쏘몰 바이탈 F 15일분',35000,32900,15,'120000000004','10x10x5mm','멀티비타민 바이탈 F',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 바이탈 F 30일분' AND product_id=(SELECT id FROM product WHERE code='P89432010130_2')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='오쏘몰 바이탈 F 15일분' AND product_id=(SELECT id FROM product WHERE code='P89432010130_2')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P89432010130_2'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P89432010130_2'),1570,'2025-01-25 14:20:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 우먼 더블업 멀티비타민 112정 코스트코',
 'P88179999195_3','여성용 더블업 멀티비타민, 영양소 2배 강화',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-20 00:00:00',30,6,2340,4.6,420,37990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88179999195_3'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88179999195_3'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88179999195_3'),
 'https://shopping-phinf.pstatic.net/main_8817999/88179999195.4.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88179999195_3'),'센트룸 우먼 더블업 112정',42000,37990,112,'600000000021','10x10x5mm','여성용 더블업 멀티비타민',6,NULL),
((SELECT id FROM product WHERE code='P88179999195_3'),'센트룸 우먼 더블업 56정',25000,22900,56,'600000000022','10x10x5mm','여성용 더블업 멀티비타민',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 112정' AND product_id=(SELECT id FROM product WHERE code='P88179999195_3')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='센트룸 우먼 더블업 56정' AND product_id=(SELECT id FROM product WHERE code='P88179999195_3')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88179999195_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88179999195_3'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='센트룸'),
 '센트룸 실버 포 우먼 종합 멀티 비타민 영양제 112정 코스트코 비타민B12 대용량',
 'P86787803460_3','50세 이상 여성을 위한 종합 멀티비타민, 비타민B12 강화로 활력 증진',
 '하루 1정을 식후에 충분한 물과 함께 섭취하세요.','비타민 A, C, D, E, B군, B12, 엽산, 철분, 아연, 마그네슘, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다. 임신 중이거나 수유 중인 경우 의사와 상담하세요.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-08-15 00:00:00',30,6,2340,4.6,420,39990);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P86787803460_3'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P86787803460_3'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P86787803460_3'),
 'https://shopping-phinf.pstatic.net/main_8678780/86787803460.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P86787803460_3'),'우먼 112정',45000,39990,112,'600000000023','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL),
((SELECT id FROM product WHERE code='P86787803460_3'),'우먼 56정',25000,22900,56,'600000000024','10x10x5mm','여성용 멀티비타민 포뮬라',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='우먼 112정' AND product_id=(SELECT id FROM product WHERE code='P86787803460_3')),
 156,'2025-01-25 14:30:00','2025-01-20 10:00:00'),
((SELECT id FROM product_variant WHERE variant_name='우먼 56정' AND product_id=(SELECT id FROM product WHERE code='P86787803460_3')),
 89,'2025-01-25 14:30:00','2025-01-18 15:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P86787803460_3'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P86787803460_3'),2340,'2025-01-28 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='고려은단'),
 '고려은단 멀티비타민 올인원 1560mg x 60정, 3개',
 'P51929229300_2','멀티비타민 올인원, 3개 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','비타민 A, C, D, E, B군, 미네랄, 아연, 마그네슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-01 00:00:00',30,6,1570,4.3,187,59750);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929229300_2'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929229300_2'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P51929229300_2'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929229300.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929229300_2'),'고려은단 3개 세트',65000,59750,180,'130000000003','10x10x5mm','멀티비타민 올인원',6,NULL),
((SELECT id FROM product WHERE code='P51929229300_2'),'고려은단 1개',25000,22900,60,'130000000004','10x10x5mm','멀티비타민 올인원',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='고려은단 3개 세트' AND product_id=(SELECT id FROM product WHERE code='P51929229300_2')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='고려은단 1개' AND product_id=(SELECT id FROM product WHERE code='P51929229300_2')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929229300_2'),(SELECT id FROM category WHERE name='비타민 E'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929229300_2'),1570,'2025-01-25 14:20:00');


INSERT IGNORE INTO brand (name) VALUES ('JW중외제약');
INSERT IGNORE INTO brand (name) VALUES ('영롱');
INSERT IGNORE INTO brand (name) VALUES ('닥터스베스트');
INSERT IGNORE INTO brand (name) VALUES ('닥터린');
INSERT IGNORE INTO brand (name) VALUES ('NOW');
INSERT IGNORE INTO brand (name) VALUES ('비타플랜');
INSERT IGNORE INTO brand (name) VALUES ('GNC');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='JW중외제약'),
 '비오팜 JW중외제약 메나퀴논 MK7 비타민K2 180ug 영양제 500mg x 30정, 1개',
 'P56331375708','JW중외제약 메나퀴논 MK7 비타민K2, 180ug 고함량',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK7 180ug, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-08-19 00:00:00',30,6,1890,4.4,280,11800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P56331375708'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375708.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P56331375708'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375708.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P56331375708'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375708.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P56331375708'),'JW중외제약 30정',14000,11800,30,'460000000001','10x10x5mm','메나퀴논 MK7 비타민K2',6,NULL),
((SELECT id FROM product WHERE code='P56331375708'),'JW중외제약 60정',25000,22900,60,'460000000002','10x10x5mm','메나퀴논 MK7 비타민K2',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='JW중외제약 30정' AND product_id=(SELECT id FROM product WHERE code='P56331375708')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='JW중외제약 60정' AND product_id=(SELECT id FROM product WHERE code='P56331375708')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P56331375708'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P56331375708'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='영롱'),
 '3+1 영롱 세이프 비타민K2+칼슘 약사설계 [MK7 메나퀴논]120mcg 비타민케이2',
 'P88437782579','영롱 세이프 비타민K2+칼슘, 약사설계 MK7 메나퀴논',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK7 120mcg, 비타민K2, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,59700);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88437782579'),
 'https://shopping-phinf.pstatic.net/main_8843778/88437782579.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88437782579'),
 'https://shopping-phinf.pstatic.net/main_8843778/88437782579.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88437782579'),
 'https://shopping-phinf.pstatic.net/main_8843778/88437782579.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88437782579'),'영롱 4개 세트',70000,59700,120,'470000000001','10x10x5mm','세이프 비타민K2+칼슘',6,NULL),
((SELECT id FROM product WHERE code='P88437782579'),'영롱 1개',20000,18900,30,'470000000002','10x10x5mm','세이프 비타민K2+칼슘',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='영롱 4개 세트' AND product_id=(SELECT id FROM product WHERE code='P88437782579')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='영롱 1개' AND product_id=(SELECT id FROM product WHERE code='P88437782579')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88437782579'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88437782579'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터스베스트'),
 '닥터스베스트 내추럴 비타민 K2 MK-7 100mcg 베지 캡  60정  1개',
 'P49740164154','닥터스베스트 내추럴 비타민K2 MK-7, 100mcg 베지캡',
 '하루 1캡슐을 식후에 섭취하세요.','메나퀴논 MK-7 100mcg, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,16290);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P49740164154'),
 'https://shopping-phinf.pstatic.net/main_4974016/49740164154.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P49740164154'),
 'https://shopping-phinf.pstatic.net/main_4974016/49740164154.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P49740164154'),
 'https://shopping-phinf.pstatic.net/main_4974016/49740164154.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P49740164154'),'닥터스베스트 60정',19000,16290,60,'480000000001','10x10x5mm','내추럴 비타민K2 MK-7',6,NULL),
((SELECT id FROM product WHERE code='P49740164154'),'닥터스베스트 120정',35000,32900,120,'480000000002','10x10x5mm','내추럴 비타민K2 MK-7',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터스베스트 60정' AND product_id=(SELECT id FROM product WHERE code='P49740164154')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터스베스트 120정' AND product_id=(SELECT id FROM product WHERE code='P49740164154')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P49740164154'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P49740164154'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터린'),
 '닥터린 초임계 비타민K2 &amp; D3 프리미엄  30정  1개',
 'P55857652655','닥터린 초임계 비타민K2 & D3 프리미엄',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK-7, 비타민K2, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,12180);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P55857652655'),
 'https://shopping-phinf.pstatic.net/main_5585765/55857652655.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P55857652655'),
 'https://shopping-phinf.pstatic.net/main_5585765/55857652655.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P55857652655'),
 'https://shopping-phinf.pstatic.net/main_5585765/55857652655.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P55857652655'),'닥터린 30정',15000,12180,30,'490000000001','10x10x5mm','초임계 비타민K2 & D3',6,NULL),
((SELECT id FROM product WHERE code='P55857652655'),'닥터린 60정',28000,25900,60,'490000000002','10x10x5mm','초임계 비타민K2 & D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터린 30정' AND product_id=(SELECT id FROM product WHERE code='P55857652655')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터린 60정' AND product_id=(SELECT id FROM product WHERE code='P55857652655')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P55857652655'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P55857652655'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='영롱'),
 '[평점4.8] 영롱 세이프 비타민K2+칼슘 약사설계 [MK7 메나퀴논]120mcg 효능',
 'P88437738531','영롱 세이프 비타민K2+칼슘, 약사설계 MK7 메나퀴논',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK7 120mcg, 비타민K2, 칼슘',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,19900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88437738531'),
 'https://shopping-phinf.pstatic.net/main_8843773/88437738531.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88437738531'),
 'https://shopping-phinf.pstatic.net/main_8843773/88437738531.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P88437738531'),
 'https://shopping-phinf.pstatic.net/main_8843773/88437738531.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88437738531'),'영롱 30정',23000,19900,30,'500000000001','10x10x5mm','세이프 비타민K2+칼슘',6,NULL),
((SELECT id FROM product WHERE code='P88437738531'),'영롱 60정',42000,38900,60,'500000000002','10x10x5mm','세이프 비타민K2+칼슘',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='영롱 30정' AND product_id=(SELECT id FROM product WHERE code='P88437738531')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='영롱 60정' AND product_id=(SELECT id FROM product WHERE code='P88437738531')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88437738531'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88437738531'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='NOW'),
 '나우푸드 비타민 K-2 100mcg 베지 캡슐  100정  1개',
 'P49655566512','나우푸드 비타민K-2, 100mcg 베지캡슐',
 '하루 1캡슐을 식후에 섭취하세요.','메나퀴논 MK-7 100mcg, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,11910);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P49655566512'),
 'https://shopping-phinf.pstatic.net/main_4965556/49655566512.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P49655566512'),
 'https://shopping-phinf.pstatic.net/main_4965556/49655566512.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P49655566512'),
 'https://shopping-phinf.pstatic.net/main_4965556/49655566512.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P49655566512'),'나우푸드 100정',14000,11910,100,'510000000001','10x10x5mm','비타민K-2 베지캡슐',6,NULL),
((SELECT id FROM product WHERE code='P49655566512'),'나우푸드 200정',25000,22900,200,'510000000002','10x10x5mm','비타민K-2 베지캡슐',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='나우푸드 100정' AND product_id=(SELECT id FROM product WHERE code='P49655566512')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='나우푸드 200정' AND product_id=(SELECT id FROM product WHERE code='P49655566512')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P49655566512'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P49655566512'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터린'),
 '닥터린 초임계 비타민k2 300mg 30정 3박스  3개',
 'P53914690333','닥터린 초임계 비타민K2, 3박스 묶음 세트',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK-7, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,26220);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P53914690333'),
 'https://shopping-phinf.pstatic.net/main_5391469/53914690333.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P53914690333'),
 'https://shopping-phinf.pstatic.net/main_5391469/53914690333.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P53914690333'),
 'https://shopping-phinf.pstatic.net/main_5391469/53914690333.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P53914690333'),'닥터린 3박스 세트',30000,26220,90,'520000000001','10x10x5mm','초임계 비타민K2',6,NULL),
((SELECT id FROM product WHERE code='P53914690333'),'닥터린 1박스',12000,10900,30,'520000000002','10x10x5mm','초임계 비타민K2',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터린 3박스 세트' AND product_id=(SELECT id FROM product WHERE code='P53914690333')),
 78,'2025-01-23 13:45:00','2025-01-20 16:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터린 1박스' AND product_id=(SELECT id FROM product WHERE code='P53914690333')),
 45,'2025-01-23 13:45:00','2025-01-17 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P53914690333'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P53914690333'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터스베스트'),
 '닥터스베스트 내추럴 비타민 K2 MK-7 45mcg 베지 캡  180정  1개',
 'P49792688046','닥터스베스트 내추럴 비타민K2 MK-7, 45mcg 베지캡',
 '하루 1캡슐을 식후에 섭취하세요.','메나퀴논 MK-7 45mcg, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,30390);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P49792688046'),
 'https://shopping-phinf.pstatic.net/main_4979268/49792688046.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P49792688046'),
 'https://shopping-phinf.pstatic.net/main_4979268/49792688046.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P49792688046'),
 'https://shopping-phinf.pstatic.net/main_4979268/49792688046.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P49792688046'),'닥터스베스트 180정',35000,30390,180,'530000000001','10x10x5mm','내추럴 비타민K2 MK-7',6,NULL),
((SELECT id FROM product WHERE code='P49792688046'),'닥터스베스트 90정',20000,18900,90,'530000000002','10x10x5mm','내추럴 비타민K2 MK-7',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='닥터스베스트 180정' AND product_id=(SELECT id FROM product WHERE code='P49792688046')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='닥터스베스트 90정' AND product_id=(SELECT id FROM product WHERE code='P49792688046')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P49792688046'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P49792688046'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='비타플랜'),
 '비타민K2 영양제 비타민D3 메나퀴논 MK-7 K2D3 60정, 1개',
 'P89641910485','비타플랜 비타민K2 영양제, 비타민D3 메나퀴논 MK-7',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK-7, 비타민K2, 비타민D3',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-09-15 00:00:00',30,6,1890,4.4,280,9900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P89641910485'),
 'https://shopping-phinf.pstatic.net/main_8964191/89641910485.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P89641910485'),
 'https://shopping-phinf.pstatic.net/main_8964191/89641910485.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P89641910485'),
 'https://shopping-phinf.pstatic.net/main_8964191/89641910485.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P89641910485'),'비타플랜 60정',12000,9900,60,'540000000001','10x10x5mm','비타민K2 & D3',6,NULL),
((SELECT id FROM product WHERE code='P89641910485'),'비타플랜 120정',22000,19900,120,'540000000002','10x10x5mm','비타민K2 & D3',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='비타플랜 60정' AND product_id=(SELECT id FROM product WHERE code='P89641910485')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='비타플랜 120정' AND product_id=(SELECT id FROM product WHERE code='P89641910485')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P89641910485'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P89641910485'),1890,'2025-01-26 11:45:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='GNC'),
 '노바렉스 GNC 초임계 비타민 K-2 골드 300mg x 30정, 1개',
 'P56331375793','GNC 초임계 비타민K-2 골드, 300mg 고함량',
 '하루 1정을 식후에 섭취하세요.','메나퀴논 MK-7, 비타민K2',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-08-19 00:00:00',30,6,1890,4.4,280,22000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P56331375793'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375793.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P56331375793'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375793.2.jpg','상세 이미지 1',1,0),
((SELECT id FROM product WHERE code='P56331375793'),
 'https://shopping-phinf.pstatic.net/main_5633137/56331375793.3.jpg','상세 이미지 2',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P56331375793'),'GNC 30정',25000,22000,30,'550000000001','10x10x5mm','초임계 비타민K-2 골드',6,NULL),
((SELECT id FROM product WHERE code='P56331375793'),'GNC 60정',45000,42900,60,'550000000002','10x10x5mm','초임계 비타민K-2 골드',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='GNC 30정' AND product_id=(SELECT id FROM product WHERE code='P56331375793')),
 95,'2025-01-24 16:20:00','2025-01-21 09:00:00'),
((SELECT id FROM product_variant WHERE variant_name='GNC 60정' AND product_id=(SELECT id FROM product WHERE code='P56331375793')),
 67,'2025-01-24 16:20:00','2025-01-19 14:30:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P56331375793'),(SELECT id FROM category WHERE name='비타민 K'));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P56331375793'),1890,'2025-01-26 11:45:00');


INSERT IGNORE INTO brand (name) VALUES ('쁘띠앤');
INSERT IGNORE INTO brand (name) VALUES ('세비톨');
INSERT IGNORE INTO brand (name) VALUES ('GNM자연의품격');
INSERT IGNORE INTO brand (name) VALUES ('어바인랩 Irvinelab');
INSERT IGNORE INTO brand (name) VALUES ('뉴트원');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 뼈에좋은 칼슘 마그네슘 비타민D 아연 영양제 6개월 칼마디 임산부 마칼디',
 'P84112158990','뼈 건강을 위한 칼슘 마그네슘 비타민D 아연 영양제, 6개월분',
 '하루 2정을 식후에 섭취하세요.','칼슘 1000mg, 마그네슘 500mg, 비타민D 400IU, 아연 15mg',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-20 00:00:00',30,6,1890,4.3,280,28900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P84112158990'),
 'https://shopping-phinf.pstatic.net/main_8411215/84112158990.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P84112158990'),
 'https://shopping-phinf.pstatic.net/main_8411215/84112158990.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P84112158990'),
 'https://shopping-phinf.pstatic.net/main_8411215/84112158990.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P84112158990'),'6개월분',28900,25900,180,'600000000001','10x10x5mm','칼슘 마그네슘 비타민D 아연',6,NULL),
((SELECT id FROM product WHERE code='P84112158990'),'3개월분',18900,16900,90,'600000000002','10x10x5mm','칼슘 마그네슘 비타민D 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='6개월분' AND product_id=(SELECT id FROM product WHERE code='P84112158990')),
 120,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='3개월분' AND product_id=(SELECT id FROM product WHERE code='P84112158990')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P84112158990'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P84112158990'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='칼맥'),
 '종근당 칼슘 마그네슘 아연 비타민D 3개월 칼마디 골다공증 뼈 치아 임산부 어린이 마칼디',
 'P82323982535','칼슘 마그네슘 아연 비타민D 영양제, 3개월분',
 '하루 2정을 식후에 섭취하세요.','칼슘 800mg, 마그네슘 400mg, 아연 10mg, 비타민D 300IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-10-15 00:00:00',30,6,1890,4.2,280,14900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82323982535'),
 'https://shopping-phinf.pstatic.net/main_8232398/82323982535.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82323982535'),
 'https://shopping-phinf.pstatic.net/main_8232398/82323982535.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P82323982535'),
 'https://shopping-phinf.pstatic.net/main_8232398/82323982535.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82323982535'),'3개월분',14900,12900,90,'600000000003','10x10x5mm','칼슘 마그네슘 아연 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P82323982535'),'1개월분',8900,7900,30,'600000000004','10x10x5mm','칼슘 마그네슘 아연 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='3개월분' AND product_id=(SELECT id FROM product WHERE code='P82323982535')),
 150,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개월분' AND product_id=(SELECT id FROM product WHERE code='P82323982535')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82323982535'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82323982535'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='쁘띠앤'),
 '쁘띠앤 국민 리포좀 칼마디 칼슘 마그네슘 비타민D 영양제 임산부 수유부 청소년 뼈건강 칼슘마그네슘비타민D',
 'P83292255611','국민 리포좀 칼마디, 칼슘 마그네슘 비타민D 영양제',
 '하루 2정을 식후에 섭취하세요.','칼슘 1000mg, 마그네슘 500mg, 비타민D 400IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-01 00:00:00',30,6,1890,4.4,280,29000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83292255611'),
 'https://shopping-phinf.pstatic.net/main_8329225/83292255611.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83292255611'),
 'https://shopping-phinf.pstatic.net/main_8329225/83292255611.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P83292255611'),
 'https://shopping-phinf.pstatic.net/main_8329225/83292255611.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83292255611'),'6개월분',29000,26000,180,'600000000005','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P83292255611'),'3개월분',19000,17000,90,'600000000006','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='6개월분' AND product_id=(SELECT id FROM product WHERE code='P83292255611')),
 110,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='3개월분' AND product_id=(SELECT id FROM product WHERE code='P83292255611')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83292255611'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83292255611'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='세비톨'),
 '세비톨 칼슘 마그네슘 비타민D 비타민K2 임산부 수유부 칼마디 칼슘영양제 120정, 1개',
 'P84595393398','칼슘 마그네슘 비타민D 비타민K2 영양제, 120정',
 '하루 2정을 식후에 섭취하세요.','칼슘 1000mg, 마그네슘 500mg, 비타민D 400IU, 비타민K2 100mcg',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-10 00:00:00',30,6,1890,4.3,280,21500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P84595393398'),
 'https://shopping-phinf.pstatic.net/main_8459539/84595393398.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P84595393398'),
 'https://shopping-phinf.pstatic.net/main_8459539/84595393398.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P84595393398'),
 'https://shopping-phinf.pstatic.net/main_8459539/84595393398.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P84595393398'),'120정',21500,19500,120,'600000000007','10x10x5mm','칼슘 마그네슘 비타민D 비타민K2',6,NULL),
((SELECT id FROM product WHERE code='P84595393398'),'60정',15000,13000,60,'600000000008','10x10x5mm','칼슘 마그네슘 비타민D 비타민K2',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='120정' AND product_id=(SELECT id FROM product WHERE code='P84595393398')),
 130,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P84595393398')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P84595393398'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P84595393398'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 칼슘 앤 마그네슘 비타민D 아연 1000mg x 180정, 4개',
 'P51929594138','칼슘 앤 마그네슘 비타민D 아연 영양제, 1000mg x 180정',
 '하루 2정을 식후에 섭취하세요.','칼슘 1000mg, 마그네슘 500mg, 비타민D 400IU, 아연 15mg',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-15 00:00:00',30,6,1890,4.2,280,51770);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929594138'),
 'https://shopping-phinf.pstatic.net/main_5192959/51929594138.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929594138'),
 'https://shopping-phinf.pstatic.net/main_5192959/51929594138.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929594138'),
 'https://shopping-phinf.pstatic.net/main_5192959/51929594138.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929594138'),'4개',51770,46770,720,'600000000009','10x10x5mm','칼슘 마그네슘 비타민D 아연',6,NULL),
((SELECT id FROM product WHERE code='P51929594138'),'2개',35870,30870,360,'600000000010','10x10x5mm','칼슘 마그네슘 비타민D 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='4개' AND product_id=(SELECT id FROM product WHERE code='P51929594138')),
 50,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='2개' AND product_id=(SELECT id FROM product WHERE code='P51929594138')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929594138'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929594138'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='포뉴'),
 '포뉴 칼슘 마그네슘 비타민D 950mg x 120정, 1개',
 'P51929506356','칼슘 마그네슘 비타민D 영양제, 950mg x 120정',
 '하루 2정을 식후에 섭취하세요.','칼슘 950mg, 마그네슘 475mg, 비타민D 380IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-20 00:00:00',30,6,1890,4.1,280,33000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929506356'),
 'https://shopping-phinf.pstatic.net/main_5192950/51929506356.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929506356'),
 'https://shopping-phinf.pstatic.net/main_5192950/51929506356.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929506356'),
 'https://shopping-phinf.pstatic.net/main_5192950/51929506356.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929506356'),'120정',33000,30000,120,'600000000011','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929506356'),'60정',22000,19000,60,'600000000012','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='120정' AND product_id=(SELECT id FROM product WHERE code='P51929506356')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929506356')),
 70,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929506356'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929506356'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='GNM자연의품격'),
 'GNM자연의품격 칼슘 마그네슘 아연 비타민D 1350mg x 90정, 1개',
 'P51929613826','칼슘 마그네슘 아연 비타민D 영양제, 1350mg x 90정',
 '하루 2정을 식후에 섭취하세요.','칼슘 1350mg, 마그네슘 675mg, 아연 20mg, 비타민D 540IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-25 00:00:00',30,6,1890,4.0,280,8210);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929613826'),
 'https://shopping-phinf.pstatic.net/main_5192961/51929613826.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929613826'),
 'https://shopping-phinf.pstatic.net/main_5192961/51929613826.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929613826'),
 'https://shopping-phinf.pstatic.net/main_5192961/51929613826.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929613826'),'90정',8210,7210,90,'600000000013','10x10x5mm','칼슘 마그네슘 아연 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929613826'),'45정',6100,5100,45,'600000000014','10x10x5mm','칼슘 마그네슘 아연 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='90정' AND product_id=(SELECT id FROM product WHERE code='P51929613826')),
 120,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='45정' AND product_id=(SELECT id FROM product WHERE code='P51929613826')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929613826'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929613826'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터파이토'),
 '닥터파이토 칼슘 마그네슘 비타민D 800mg x 90정, 4개',
 'P51929224884','칼슘 마그네슘 비타민D 영양제, 800mg x 90정',
 '하루 2정을 식후에 섭취하세요.','칼슘 800mg, 마그네슘 400mg, 비타민D 320IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-30 00:00:00',30,6,1890,4.2,280,89000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929224884'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929224884.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929224884'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929224884.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929224884'),
 'https://shopping-phinf.pstatic.net/main_5192922/51929224884.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929224884'),'4개',89000,84000,360,'600000000015','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929224884'),'2개',55000,50000,180,'600000000016','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='4개' AND product_id=(SELECT id FROM product WHERE code='P51929224884')),
 40,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='2개' AND product_id=(SELECT id FROM product WHERE code='P51929224884')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929224884'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929224884'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='어바인랩 Irvinelab'),
 '칼마디 K2 어바인랩 WPE 칼슘 마그네슘 비타민 D K2 임산부 영양제 60캡슐, 1개',
 'P88253066008','칼마디 K2 어바인랩 WPE, 칼슘 마그네슘 비타민 D K2 영양제',
 '하루 2정을 식후에 섭취하세요.','칼슘 1000mg, 마그네슘 500mg, 비타민D 400IU, 비타민K2 100mcg',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-05 00:00:00',30,6,1890,4.3,280,19800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88253066008'),
 'https://shopping-phinf.pstatic.net/main_8825306/88253066008.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88253066008'),
 'https://shopping-phinf.pstatic.net/main_8825306/88253066008.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P88253066008'),
 'https://shopping-phinf.pstatic.net/main_8825306/88253066008.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88253066008'),'60캡슐',19800,17800,60,'600000000017','10x10x5mm','칼슘 마그네슘 비타민D 비타민K2',6,NULL),
((SELECT id FROM product WHERE code='P88253066008'),'30캡슐',12800,10800,30,'600000000018','10x10x5mm','칼슘 마그네슘 비타민D 비타민K2',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60캡슐' AND product_id=(SELECT id FROM product WHERE code='P88253066008')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30캡슐' AND product_id=(SELECT id FROM product WHERE code='P88253066008')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88253066008'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88253066008'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴트원'),
 '뉴트원 칼슘 마그네슘 비타민D 1200mg x 60정, 1개',
 'P51929177980','칼슘 마그네슘 비타민D 영양제, 1200mg x 60정',
 '하루 2정을 식후에 섭취하세요.','칼슘 1200mg, 마그네슘 600mg, 비타민D 480IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-10 00:00:00',30,6,1890,4.1,280,22210);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929177980'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929177980.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929177980'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929177980.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929177980'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929177980.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929177980'),'60정',22210,20210,60,'600000000019','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929177980'),'30정',15210,13210,30,'600000000020','10x10x5mm','칼슘 마그네슘 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929177980')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30정' AND product_id=(SELECT id FROM product WHERE code='P51929177980')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929177980'),(SELECT id FROM category WHERE name='칼슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929177980'),1890,'2025-09-29 12:00:00');


INSERT IGNORE INTO brand (name) VALUES ('바이오렉트라');
INSERT IGNORE INTO brand (name) VALUES ('일양약품');
INSERT IGNORE INTO brand (name) VALUES ('세노비스');
INSERT IGNORE INTO brand (name) VALUES ('뉴트리코어');
INSERT IGNORE INTO brand (name) VALUES ('비타생활건강');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='바이오렉트라'),
 '[공식] 헤어메스 바이오렉트라 마그네슘 독일 임산부 어린이 키즈',
 'P82448488373','독일 바이오렉트라 마그네슘, 임산부와 어린이를 위한 영양제',
 '하루 1정을 식후에 섭취하세요.','마그네슘 400mg, 비타민D, 아연',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-25 00:00:00',30,6,1890,4.3,280,22000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82448488373'),
 'https://shopping-phinf.pstatic.net/main_8244848/82448488373.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82448488373'),
 'https://shopping-phinf.pstatic.net/main_8244848/82448488373.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P82448488373'),
 'https://shopping-phinf.pstatic.net/main_8244848/82448488373.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82448488373'),'90정',22000,20000,90,'600000000021','10x10x5mm','마그네슘 비타민D 아연',6,NULL),
((SELECT id FROM product WHERE code='P82448488373'),'60정',18000,16000,60,'600000000022','10x10x5mm','마그네슘 비타민D 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='90정' AND product_id=(SELECT id FROM product WHERE code='P82448488373')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P82448488373')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82448488373'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82448488373'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='일양약품'),
 '일양약품 액티브 마그네슘 플러스 비타민D 800mg x 120정, 1개',
 'P51929172545','일양약품 액티브 마그네슘 플러스 비타민D, 800mg 고함량',
 '하루 2정을 식후에 섭취하세요.','마그네슘 800mg, 비타민D 400IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-05 00:00:00',30,6,1890,4.2,280,13610);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929172545'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929172545.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929172545'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929172545.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929172545'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929172545.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929172545'),'120정',13610,12610,120,'600000000023','10x10x5mm','마그네슘 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P51929172545'),'60정',10610,9610,60,'600000000024','10x10x5mm','마그네슘 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='120정' AND product_id=(SELECT id FROM product WHERE code='P51929172545')),
 120,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929172545')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929172545'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929172545'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='더리얼'),
 '더리얼 마그네슘 맥스 플러스 캐나다 산화제품 영양제 보충제',
 'P81925888613','더리얼 마그네슘 맥스 플러스, 캐나다산 고품질',
 '하루 1정을 식후에 섭취하세요.','마그네슘 500mg, 아연, 비타민B6',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-10 00:00:00',30,6,1890,4.4,280,26800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P81925888613'),
 'https://shopping-phinf.pstatic.net/main_8192588/81925888613.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P81925888613'),
 'https://shopping-phinf.pstatic.net/main_8192588/81925888613.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P81925888613'),
 'https://shopping-phinf.pstatic.net/main_8192588/81925888613.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P81925888613'),'90정',26800,24800,90,'600000000025','10x10x5mm','마그네슘 아연 비타민B6',6,NULL),
((SELECT id FROM product WHERE code='P81925888613'),'60정',21800,19800,60,'600000000026','10x10x5mm','마그네슘 아연 비타민B6',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='90정' AND product_id=(SELECT id FROM product WHERE code='P81925888613')),
 110,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P81925888613')),
 85,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P81925888613'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P81925888613'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='세노비스'),
 '세노비스 마그네슘 1000mg x 90정, 1개',
 'P51929514736','세노비스 마그네슘, 1000mg 고함량',
 '하루 1정을 식후에 섭취하세요.','마그네슘 1000mg, 비타민B6',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-15 00:00:00',30,6,1890,4.1,280,16690);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929514736'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929514736.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929514736'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929514736.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929514736'),
 'https://shopping-phinf.pstatic.net/main_5192951/51929514736.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929514736'),'90정',16690,15690,90,'600000000027','10x10x5mm','마그네슘 비타민B6',6,NULL),
((SELECT id FROM product WHERE code='P51929514736'),'60정',13690,12690,60,'600000000028','10x10x5mm','마그네슘 비타민B6',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='90정' AND product_id=(SELECT id FROM product WHERE code='P51929514736')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929514736')),
 75,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929514736'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929514736'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='GNM자연의품격'),
 'GNM자연의품격 마그네슘05 800mg x 120정, 1개',
 'P51929282682','GNM자연의품격 마그네슘05, 800mg 고함량',
 '하루 2정을 식후에 섭취하세요.','마그네슘 800mg, 아연, 비타민B6',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-20 00:00:00',30,6,1890,4.0,280,11600);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929282682'),
 'https://shopping-phinf.pstatic.net/main_5192928/51929282682.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929282682'),
 'https://shopping-phinf.pstatic.net/main_5192928/51929282682.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929282682'),
 'https://shopping-phinf.pstatic.net/main_5192928/51929282682.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929282682'),'120정',11600,10600,120,'600000000029','10x10x5mm','마그네슘 아연 비타민B6',6,NULL),
((SELECT id FROM product WHERE code='P51929282682'),'60정',8600,7600,60,'600000000030','10x10x5mm','마그네슘 아연 비타민B6',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='120정' AND product_id=(SELECT id FROM product WHERE code='P51929282682')),
 130,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929282682')),
 95,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929282682'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929282682'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='튼튼백서'),
 '튼튼백서 신경 튼튼 마그네슘 1200mg x 90정, 1개',
 'P51929175771','튼튼백서 신경 튼튼 마그네슘, 1200mg 고함량',
 '하루 1정을 식후에 섭취하세요.','마그네슘 1200mg, 비타민B6, 아연',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-25 00:00:00',30,6,1890,4.2,280,15900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929175771'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929175771.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929175771'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929175771.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929175771'),
 'https://shopping-phinf.pstatic.net/main_5192917/51929175771.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929175771'),'90정',15900,14900,90,'600000000031','10x10x5mm','마그네슘 비타민B6 아연',6,NULL),
((SELECT id FROM product WHERE code='P51929175771'),'60정',12900,11900,60,'600000000032','10x10x5mm','마그네슘 비타민B6 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='90정' AND product_id=(SELECT id FROM product WHERE code='P51929175771')),
 105,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929175771')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929175771'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929175771'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터스베스트'),
 '1+1 닥터스베스트 킬레이트 마그네슘 글리시네이트 100mg 120타블렛',
 'P88016571260','닥터스베스트 킬레이트 마그네슘 글리시네이트, 1+1 이벤트',
 '하루 2정을 식후에 섭취하세요.','마그네슘 글리시네이트 100mg, 킬레이트 형태',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-30 00:00:00',30,6,1890,4.5,280,32700);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88016571260'),
 'https://shopping-phinf.pstatic.net/main_8801657/88016571260.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88016571260'),
 'https://shopping-phinf.pstatic.net/main_8801657/88016571260.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P88016571260'),
 'https://shopping-phinf.pstatic.net/main_8801657/88016571260.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88016571260'),'1+1',32700,29700,240,'600000000033','10x10x5mm','마그네슘 글리시네이트',6,NULL),
((SELECT id FROM product WHERE code='P88016571260'),'1개',22700,19700,120,'600000000034','10x10x5mm','마그네슘 글리시네이트',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='1+1' AND product_id=(SELECT id FROM product WHERE code='P88016571260')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개' AND product_id=(SELECT id FROM product WHERE code='P88016571260')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88016571260'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88016571260'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴트리코어'),
 '뉴트리코어 마그네슘 1350mg x 60정, 1개',
 'P51929471939','뉴트리코어 마그네슘, 1350mg 고함량',
 '하루 1정을 식후에 섭취하세요.','마그네슘 1350mg, 비타민B6, 아연',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-05 00:00:00',30,6,1890,4.3,280,33740);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929471939'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929471939.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929471939'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929471939.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929471939'),
 'https://shopping-phinf.pstatic.net/main_5192947/51929471939.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929471939'),'60정',33740,31740,60,'600000000035','10x10x5mm','마그네슘 비타민B6 아연',6,NULL),
((SELECT id FROM product WHERE code='P51929471939'),'30정',23740,21740,30,'600000000036','10x10x5mm','마그네슘 비타민B6 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929471939')),
 85,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30정' AND product_id=(SELECT id FROM product WHERE code='P51929471939')),
 65,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929471939'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929471939'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='일양약품'),
 '일양약품 마그네슘 스피드액 글루콘산 마그네슘 액상 마그네슘 영양제 약국',
 'P88700999930','일양약품 마그네슘 스피드액, 글루콘산 마그네슘 액상',
 '하루 1포씩 물에 타서 섭취하세요.','글루콘산 마그네슘 500mg, 액상 형태',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-10 00:00:00',30,6,1890,4.1,280,13500);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P88700999930'),
 'https://shopping-phinf.pstatic.net/main_8870099/88700999930.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P88700999930'),
 'https://shopping-phinf.pstatic.net/main_8870099/88700999930.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P88700999930'),
 'https://shopping-phinf.pstatic.net/main_8870099/88700999930.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P88700999930'),'30포',13500,12500,30,'600000000037','액상','글루콘산 마그네슘',6,NULL),
((SELECT id FROM product WHERE code='P88700999930'),'15포',9500,8500,15,'600000000038','액상','글루콘산 마그네슘',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='30포' AND product_id=(SELECT id FROM product WHERE code='P88700999930')),
 70,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='15포' AND product_id=(SELECT id FROM product WHERE code='P88700999930')),
 50,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P88700999930'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P88700999930'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터스베스트'),
 '닥터스베스트 고흡수 마그네슘 100mg x 240정, 1개',
 'P53548645762','닥터스베스트 고흡수 마그네슘, 100mg 고흡수 형태',
 '하루 2정을 식후에 섭취하세요.','고흡수 마그네슘 100mg, 킬레이트 형태',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-15 00:00:00',30,6,1890,4.4,280,8950);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P53548645762'),
 'https://shopping-phinf.pstatic.net/main_5354864/53548645762.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P53548645762'),
 'https://shopping-phinf.pstatic.net/main_5354864/53548645762.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P53548645762'),
 'https://shopping-phinf.pstatic.net/main_5354864/53548645762.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P53548645762'),'240정',8950,7950,240,'600000000039','10x10x5mm','고흡수 마그네슘',6,NULL),
((SELECT id FROM product WHERE code='P53548645762'),'120정',6950,5950,120,'600000000040','10x10x5mm','고흡수 마그네슘',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='240정' AND product_id=(SELECT id FROM product WHERE code='P53548645762')),
 95,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='120정' AND product_id=(SELECT id FROM product WHERE code='P53548645762')),
 75,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P53548645762'),(SELECT id FROM category WHERE name='마그네슘' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P53548645762'),1890,'2025-09-29 12:00:00');


INSERT IGNORE INTO brand (name) VALUES ('헬퓨');
INSERT IGNORE INTO brand (name) VALUES ('더데이');
INSERT IGNORE INTO brand (name) VALUES ('DAYLIFE');
INSERT IGNORE INTO brand (name) VALUES ('뉴트키즈');
INSERT IGNORE INTO brand (name) VALUES ('스파톤');
INSERT IGNORE INTO brand (name) VALUES ('써플라이올코리아');
INSERT IGNORE INTO brand (name) VALUES ('닥터라인');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='에코 뉴트리션'),
 '에코뉴트리션 3개월분 철분제 여성 남성 임산부 비헴철 헤마 철분 영양제',
 'P83357292328','에코뉴트리션 철분제, 3개월분 비헴철 헤마 철분',
 '하루 1정을 식후에 섭취하세요.','비헴철 18mg, 헤마 철분, 엽산, 비타민C',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-11-30 00:00:00',30,6,1890,4.2,280,18800);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83357292328'),
 'https://shopping-phinf.pstatic.net/main_8335729/83357292328.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83357292328'),
 'https://shopping-phinf.pstatic.net/main_8335729/83357292328.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P83357292328'),
 'https://shopping-phinf.pstatic.net/main_8335729/83357292328.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83357292328'),'3개월분',18800,17800,90,'600000000041','10x10x5mm','비헴철 헤마 철분 엽산 비타민C',6,NULL),
((SELECT id FROM product WHERE code='P83357292328'),'1개월분',12800,11800,30,'600000000042','10x10x5mm','비헴철 헤마 철분 엽산 비타민C',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='3개월분' AND product_id=(SELECT id FROM product WHERE code='P83357292328')),
 100,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개월분' AND product_id=(SELECT id FROM product WHERE code='P83357292328')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83357292328'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83357292328'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='헬퓨'),
 '헬퓨 액상 철분제 10g x 30포, 3개',
 'P54071757285','헬퓨 액상 철분제, 10g x 30포 3개',
 '하루 1포씩 물에 타서 섭취하세요.','액상 철분 10g, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-05 00:00:00',30,6,1890,4.3,280,50000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P54071757285'),
 'https://shopping-phinf.pstatic.net/main_5407175/54071757285.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P54071757285'),
 'https://shopping-phinf.pstatic.net/main_5407175/54071757285.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P54071757285'),
 'https://shopping-phinf.pstatic.net/main_5407175/54071757285.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P54071757285'),'3개',50000,47000,90,'600000000043','액상','액상 철분 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P54071757285'),'1개',25000,22000,30,'600000000044','액상','액상 철분 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='3개' AND product_id=(SELECT id FROM product WHERE code='P54071757285')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개' AND product_id=(SELECT id FROM product WHERE code='P54071757285')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P54071757285'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P54071757285'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='더데이'),
 '돌아기철분 어린이 철분제 유아 초등학생 우리아이 유당없는 튼튼 철분 부족 비타민C 비헴철',
 'P83412328535','돌아기철분 어린이 철분제, 유당없는 비헴철',
 '하루 1정을 식후에 섭취하세요.','비헴철 7mg, 비타민C, 엽산, 아연',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-10 00:00:00',30,6,1890,4.4,280,25000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83412328535'),
 'https://shopping-phinf.pstatic.net/main_8341232/83412328535.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83412328535'),
 'https://shopping-phinf.pstatic.net/main_8341232/83412328535.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P83412328535'),
 'https://shopping-phinf.pstatic.net/main_8341232/83412328535.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83412328535'),'60정',25000,23000,60,'600000000045','10x10x5mm','비헴철 비타민C 엽산 아연',6,NULL),
((SELECT id FROM product WHERE code='P83412328535'),'30정',18000,16000,30,'600000000046','10x10x5mm','비헴철 비타민C 엽산 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P83412328535')),
 85,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30정' AND product_id=(SELECT id FROM product WHERE code='P83412328535')),
 65,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83412328535'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83412328535'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='DAYLIFE'),
 '데이라이프 아이 뉴트리션 베이비 키즈 철분 돌 아기 유아 어린이 초등학생 철분 철분제',
 'P83518772174','데이라이프 아이 뉴트리션 베이비 키즈 철분제',
 '하루 1정을 식후에 섭취하세요.','철분 8mg, 비타민C, 엽산, 아연',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-15 00:00:00',30,6,1890,4.1,280,18000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83518772174'),
 'https://shopping-phinf.pstatic.net/main_8351877/83518772174.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83518772174'),
 'https://shopping-phinf.pstatic.net/main_8351877/83518772174.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P83518772174'),
 'https://shopping-phinf.pstatic.net/main_8351877/83518772174.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83518772174'),'60정',18000,17000,60,'600000000047','10x10x5mm','철분 비타민C 엽산 아연',6,NULL),
((SELECT id FROM product WHERE code='P83518772174'),'30정',13000,12000,30,'600000000048','10x10x5mm','철분 비타민C 엽산 아연',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P83518772174')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30정' AND product_id=(SELECT id FROM product WHERE code='P83518772174')),
 70,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83518772174'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83518772174'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='뉴트키즈'),
 '뉴트키즈 철분 1300mg x 30포, 1개',
 'P51929202937','뉴트키즈 철분, 1300mg x 30포',
 '하루 1포씩 물에 타서 섭취하세요.','철분 1300mg, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-20 00:00:00',30,6,1890,4.0,280,13720);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929202937'),
 'https://shopping-phinf.pstatic.net/main_5192920/51929202937.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929202937'),
 'https://shopping-phinf.pstatic.net/main_5192920/51929202937.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929202937'),
 'https://shopping-phinf.pstatic.net/main_5192920/51929202937.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929202937'),'30포',13720,12720,30,'600000000049','포장','철분 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P51929202937'),'15포',9700,8700,15,'600000000050','포장','철분 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='30포' AND product_id=(SELECT id FROM product WHERE code='P51929202937')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='15포' AND product_id=(SELECT id FROM product WHERE code='P51929202937')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929202937'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929202937'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='스파톤'),
 '스파톤 철분제 오리지날 애플 액상 비헴철 비타민',
 'P83927240331','스파톤 철분제 오리지날 애플, 액상 비헴철',
 '하루 1포씩 물에 타서 섭취하세요.','비헴철 15mg, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-25 00:00:00',30,6,1890,4.2,280,24920);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P83927240331'),
 'https://shopping-phinf.pstatic.net/main_8392724/83927240331.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P83927240331'),
 'https://shopping-phinf.pstatic.net/main_8392724/83927240331.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P83927240331'),
 'https://shopping-phinf.pstatic.net/main_8392724/83927240331.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P83927240331'),'30포',24920,23920,30,'600000000051','액상','비헴철 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P83927240331'),'15포',17920,16920,15,'600000000052','액상','비헴철 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='30포' AND product_id=(SELECT id FROM product WHERE code='P83927240331')),
 70,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='15포' AND product_id=(SELECT id FROM product WHERE code='P83927240331')),
 50,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P83927240331'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P83927240331'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='종근당'),
 '종근당 철분제 철분 영양제 엽산 비타민D 2개월분 여성 남성 임산부 어린이',
 'P82306920633','종근당 철분제, 엽산 비타민D 포함 2개월분',
 '하루 1정을 식후에 섭취하세요.','철분 18mg, 엽산 400mcg, 비타민D 400IU',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2024-12-30 00:00:00',30,6,1890,4.3,280,11900);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82306920633'),
 'https://shopping-phinf.pstatic.net/main_8230692/82306920633.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82306920633'),
 'https://shopping-phinf.pstatic.net/main_8230692/82306920633.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P82306920633'),
 'https://shopping-phinf.pstatic.net/main_8230692/82306920633.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82306920633'),'2개월분',11900,10900,60,'600000000053','10x10x5mm','철분 엽산 비타민D',6,NULL),
((SELECT id FROM product WHERE code='P82306920633'),'1개월분',8900,7900,30,'600000000054','10x10x5mm','철분 엽산 비타민D',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='2개월분' AND product_id=(SELECT id FROM product WHERE code='P82306920633')),
 120,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개월분' AND product_id=(SELECT id FROM product WHERE code='P82306920633')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82306920633'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82306920633'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='포뉴'),
 '포뉴 NI 철분 카제로템 철분제 임산부 비헴철 영양제 성인 여성 남성 2개월분',
 'P82108890656','포뉴 NI 철분 카제로템, 임산부 비헴철 영양제',
 '하루 1정을 식후에 섭취하세요.','카제로템 철분 28mg, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-05 00:00:00',30,6,1890,4.4,280,28000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P82108890656'),
 'https://shopping-phinf.pstatic.net/main_8210889/82108890656.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P82108890656'),
 'https://shopping-phinf.pstatic.net/main_8210889/82108890656.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P82108890656'),
 'https://shopping-phinf.pstatic.net/main_8210889/82108890656.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P82108890656'),'2개월분',28000,26000,60,'600000000055','10x10x5mm','카제로템 철분 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P82108890656'),'1개월분',20000,18000,30,'600000000056','10x10x5mm','카제로템 철분 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='2개월분' AND product_id=(SELECT id FROM product WHERE code='P82108890656')),
 80,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='1개월분' AND product_id=(SELECT id FROM product WHERE code='P82108890656')),
 60,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P82108890656'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P82108890656'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='포뉴'),
 '포뉴 철분 600mg x 60정, 1개',
 'P51929354791','포뉴 철분, 600mg x 60정',
 '하루 1정을 식후에 섭취하세요.','철분 600mg, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-10 00:00:00',30,6,1890,4.1,280,28000);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929354791'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354791.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929354791'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354791.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929354791'),
 'https://shopping-phinf.pstatic.net/main_5192935/51929354791.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929354791'),'60정',28000,26000,60,'600000000057','10x10x5mm','철분 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P51929354791'),'30정',20000,18000,30,'600000000058','10x10x5mm','철분 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='60정' AND product_id=(SELECT id FROM product WHERE code='P51929354791')),
 90,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='30정' AND product_id=(SELECT id FROM product WHERE code='P51929354791')),
 70,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929354791'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929354791'),1890,'2025-09-29 12:00:00');


INSERT INTO product (brand_id,name,code,description,instruction,ingredients,cautions,disclaimer,
 sale_start_date,expiration_date,max_qty_per_order,sales,avg_rating,review_count,min_price)
VALUES ((SELECT id FROM brand WHERE name='닥터라인'),
 '닥터라인 헤모키즈 120ml, 1개',
 'P51929237803','닥터라인 헤모키즈, 120ml 액상 철분제',
 '하루 1포씩 물에 타서 섭취하세요.','액상 철분 120ml, 비타민C, 엽산',
 '과다섭취 시 부작용이 있을 수 있습니다.','본 제품은 질병의 예방 및 치료를 위한 의약품이 아닙니다.',
 '2025-01-15 00:00:00',30,6,1890,4.2,280,29260);
INSERT INTO product_img (product_id,image_url,alt_text,sort_idx,is_primary) VALUES
((SELECT id FROM product WHERE code='P51929237803'),
 'https://shopping-phinf.pstatic.net/main_5192923/51929237803.1.jpg','대표 이미지',0,1),
((SELECT id FROM product WHERE code='P51929237803'),
 'https://shopping-phinf.pstatic.net/main_5192923/51929237803.2.jpg','상품 상세',1,0),
((SELECT id FROM product WHERE code='P51929237803'),
 'https://shopping-phinf.pstatic.net/main_5192923/51929237803.3.jpg','성분 정보',2,0);
INSERT INTO product_variant (product_id,variant_name,list_price,sale_price,volume,upc_code,pill_size,
 nutrition_facts,max_qty_per_order,restock_eta) VALUES
((SELECT id FROM product WHERE code='P51929237803'),'120ml',29260,27260,120,'600000000059','액상','액상 철분 비타민C 엽산',6,NULL),
((SELECT id FROM product WHERE code='P51929237803'),'60ml',21260,19260,60,'600000000060','액상','액상 철분 비타민C 엽산',6,NULL);
INSERT INTO stock (product_variant_id,amount,updated_at,restocked_at) VALUES
((SELECT id FROM product_variant WHERE variant_name='120ml' AND product_id=(SELECT id FROM product WHERE code='P51929237803')),
 75,'2025-09-29 12:00:00','2025-08-20 12:00:00'),
((SELECT id FROM product_variant WHERE variant_name='60ml' AND product_id=(SELECT id FROM product WHERE code='P51929237803')),
 55,'2025-09-29 12:00:00','2025-08-20 12:00:00');
INSERT INTO product_category (product_id,category_id) VALUES
((SELECT id FROM product WHERE code='P51929237803'),(SELECT id FROM category WHERE name='철분' LIMIT 1));
INSERT INTO product_rank (product_id,recent_sales,updated_at) VALUES
((SELECT id FROM product WHERE code='P51929237803'),1890,'2025-09-29 12:00:00');



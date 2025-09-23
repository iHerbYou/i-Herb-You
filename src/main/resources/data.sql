-- =========================================================
-- CodeGroup Seed
-- =========================================================

INSERT INTO code_group (id, group_key, group_name, description, is_active, sort_order, created_at, updated_at)
VALUES
    (10,  'QNA_STATUS',             'QnA 상태',         'QnA 질문 상태 코드',        1, 10,  NOW(), NOW()),
    (20,  'DELIVERY_STATUS',        '배송 상태',        '배송 진행 단계 코드',       1, 20,  NOW(), NOW()),
    (30,  'ORDER_STATUS',           '주문 상태',        '주문 처리 단계 코드',       1, 30,  NOW(), NOW()),
    (40,  'PAYMENT_STATUS',         '결제 상태',        'PG 결제 상태',             1, 40,  NOW(), NOW()),
    (41,  'PAYMENT_METHOD',         '결제 수단',        '카드/계좌/간편결제 등',     1, 41,  NOW(), NOW()),
    (50,  'COUPON_STATUS',          '쿠폰 상태',        '활성/만료/삭제 등',        1, 50,  NOW(), NOW()),
    (51,  'DISCOUNT_TYPE',          '할인 종류',        '정액/정률',                1, 51,  NOW(), NOW()),
    (52,  'APPLICABLE_SCOPE',       '적용 범위',        '전체/상품/카테고리/브랜드', 1, 52,  NOW(), NOW()),
    (53,  'CHANNEL',                '채널',             'WEB/APP/ALL 등',           1, 53,  NOW(), NOW()),
    (60,  'REFUND_STATUS',          '환불 상태',        '요청/승인/완료 등',        1, 60,  NOW(), NOW()),
    (61,  'REFUND_REASON',          '환불 사유',        '옵션형 사유 코드',         1, 61,  NOW(), NOW()),
    (62,  'REFUND_DELIVERY_OPTION', '반품/수거 방식',    '직접 발송/택배 수거',      1, 62,  NOW(), NOW()),
    (70,  'USER_ROLE',              '회원 역할',        'user, admin_*',            1, 70,  NOW(), NOW()),
    (71,  'USER_STATUS',            '회원 상태',        '정상/정지/탈퇴/휴면',      1, 71,  NOW(), NOW()),
    (80,  'LOGIN_PROVIDER',         '로그인 제공자',    'LOCAL/GOOGLE/NAVER/KAKAO', 1, 80,  NOW(), NOW()),
    (90,  'TERMS_TYPE',             '약관 종류',        '서비스/개인정보/마케팅',   1, 90,  NOW(), NOW()),
    (100, 'REVIEW_REPORT_REASON',   '리뷰 신고 사유',   '스팸/욕설/부적절 등',      1, 100, NOW(), NOW()),
    (101, 'REPORT_STATUS',          '신고 처리 상태',   '접수/검토/조치/반려',      1, 101, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    group_name = VALUES(group_name),
    description = VALUES(description),
    is_active = VALUES(is_active),
    sort_order = VALUES(sort_order),
    updated_at = NOW();


-- =========================================================
-- Code Seed
-- =========================================================

-- 10: QNA_STATUS (101~)
INSERT INTO code (id, code_group_id, value, name, description, is_active, sort_order, valid_from, valid_to, created_at, updated_at)
VALUES
    (101, 10, 101, 'WAITING',  '답변 대기',   1, 1, NULL, NULL, NOW(), NOW()),
    (102, 10, 102, 'ANSWERED', '답변 완료',   1, 2, NULL, NULL, NOW(), NOW()),
    (103, 10, 103, 'HIDDEN',   '비공개/숨김', 1, 3, NULL, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    name=VALUES(name), description=VALUES(description),
    is_active=VALUES(is_active), sort_order=VALUES(sort_order), updated_at=NOW();

-- 20: DELIVERY_STATUS (201~)
INSERT INTO code VALUES
    (201, 20, 201, 'PREPARING',  '준비중',     '출고 준비',      1, 1, NULL, NULL, NOW(), NOW()),
    (202, 20, 202, 'SHIPPING',   '배송중',     '택배 이동중',     1, 2, NULL, NULL, NOW(), NOW()),
    (203, 20, 203, 'DELIVERED',  '배송 완료',  '수령 완료',       1, 3, NULL, NULL, NOW(), NOW()),
    (204, 20, 204, 'RETURNED',   '반송',       '반송/재발송',     1, 4, NULL, NULL, NOW(), NOW()),
    (205, 20, 205, 'DELAYED',    '지연',       '배송 지연',       1, 5, NULL, NULL, NOW(), NOW()),
    (206, 20, 206, 'CANCELED',   '취소',       '배송 취소',       1, 6, NULL, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    name=VALUES(name), description=VALUES(description),
    is_active=VALUES(is_active), sort_order=VALUES(sort_order), updated_at=NOW();

-- 30: ORDER_STATUS (301~)
INSERT INTO code VALUES
    (301, 30, 301, 'PENDING',          '주문 생성',        '결제 전 생성',      1, 1,  NULL, NULL, NOW(), NOW()),
    (302, 30, 302, 'PAID',             '결제 완료',        '결제 승인',        1, 2,  NULL, NULL, NOW(), NOW()),
    (303, 30, 303, 'PACKING',          '상품 준비',        '피킹/포장',        1, 3,  NULL, NULL, NOW(), NOW()),
    (304, 30, 304, 'SHIPPED',          '출고 완료',        '송장 할당/출고',   1, 4,  NULL, NULL, NOW(), NOW()),
    (305, 30, 305, 'COMPLETED',        '구매 확정',        '거래 완료',        1, 5,  NULL, NULL, NOW(), NOW()),
    (306, 30, 306, 'CANCELED',         '주문 취소',        '결제 전/후 취소',  1, 6,  NULL, NULL, NOW(), NOW()),
    (307, 30, 307, 'REFUND_REQUESTED', '환불 요청',        'CS 접수',          1, 7,  NULL, NULL, NOW(), NOW()),
    (308, 30, 308, 'REFUNDED',         '환불 완료',        '전액 환불 완료',   1, 8,  NULL, NULL, NOW(), NOW()),
    (309, 30, 309, 'PARTIAL_REFUNDED', '부분 환불 완료',   '일부 환불',        1, 9,  NULL, NULL, NOW(), NOW()),
    (310, 30, 310, 'FAILED',           '실패',             '결제/출고 실패',   1, 10, NULL, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    name=VALUES(name), description=VALUES(description),
    is_active=VALUES(is_active), sort_order=VALUES(sort_order), updated_at=NOW();

-- 40: PAYMENT_STATUS (401~)
INSERT INTO code VALUES
    (401, 40, 401, 'READY',            '결제 대기',      'PG 생성만',       1, 1, NULL, NULL, NOW(), NOW()),
    (402, 40, 402, 'AUTHORIZED',       '승인 완료',      '승인(매입 전)',   1, 2, NULL, NULL, NOW(), NOW()),
    (403, 40, 403, 'PAID',             '결제 완료',      '승인+매입',       1, 3, NULL, NULL, NOW(), NOW()),
    (404, 40, 404, 'CANCEL_REQUESTED', '취소 요청',      '취소 진행중',     1, 4, NULL, NULL, NOW(), NOW()),
    (405, 40, 405, 'CANCELED',         '결제 취소',      '승인취소/환불',   1, 5, NULL, NULL, NOW(), NOW()),
    (406, 40, 406, 'FAILED',           '결제 실패',      '승인 실패',       1, 6, NULL, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    name=VALUES(name), description=VALUES(description),
    is_active=VALUES(is_active), sort_order=VALUES(sort_order), updated_at=NOW();

-- 41: PAYMENT_METHOD (501~)
INSERT INTO code VALUES
    (501, 41, 501, 'CARD',             '신용/체크카드',   '일반 카드 결제',   1, 1, NULL, NULL, NOW(), NOW()),
    (502, 41, 502, 'VIRTUAL_ACCOUNT',  '가상계좌',       '무통장입금',       1, 2, NULL, NULL, NOW(), NOW()),
    (503, 41, 503, 'ACCOUNT_TRANSFER', '계좌이체',       '즉시 이체',       1, 3, NULL, NULL, NOW(), NOW()),
    (504, 41, 504, 'MOBILE',           '휴대폰결제',     '모바일 소액결제',  1, 4, NULL, NULL, NOW(), NOW()),

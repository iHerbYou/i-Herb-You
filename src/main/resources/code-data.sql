-- CodeGroup 테이블 데이터 삽입
INSERT INTO code_group (value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES
-- 10x: QnA
(10, 'QNA_STATUS', 'QnA 질문 상태 코드', 1, 10, NOW(), NOW()),
-- 20x: Delivery
(20, 'DELIVERY_STATUS', '배송 진행 단계 코드', 1, 20, NOW(), NOW()),
-- 30x: Order
(30, 'ORDER_STATUS', '주문 처리 단계 코드', 1, 30, NOW(), NOW()),
-- 40x: Payment
(40, 'PAYMENT_STATUS', 'PG 결제 상태', 1, 40, NOW(), NOW()),
(41, 'PAYMENT_METHOD', '카드/계좌/간편결제 등', 1, 41, NOW(), NOW()),
-- 50x: Coupon
(50, 'COUPON_STATUS', '활성/만료/삭제 등', 1, 50, NOW(), NOW()),
(51, 'DISCOUNT_TYPE', '정액/정률', 1, 51, NOW(), NOW()),
(52, 'APPLICABLE_SCOPE', '전체/상품/카테고리/브랜드', 1, 52, NOW(), NOW()),
(53, 'CHANNEL', 'WEB/APP/ALL 등', 1, 53, NOW(), NOW()),
-- 60x: Refund
(60, 'REFUND_STATUS', '요청/승인/완료 등', 1, 60, NOW(), NOW()),
(61, 'REFUND_REASON', '옵션형 사유 코드', 1, 61, NOW(), NOW()),
(62, 'REFUND_DELIVERY_OPTION', '직접 발송/택배 수거', 1, 62, NOW(), NOW()),
-- 70x: User
(70, 'USER_ROLE', 'user, admin_*', 1, 70, NOW(), NOW()),
(71, 'USER_STATUS', '정상/정지/탈퇴/휴면', 1, 71, NOW(), NOW()),
-- 80x: Login Provider
(80, 'LOGIN_PROVIDER', 'LOCAL/GOOGLE/NAVER/KAKAO…', 1, 80, NOW(), NOW()),
-- 90x: Terms/Policy
(90, 'TERMS_TYPE', '서비스/개인정보/마케팅/안내', 1, 90, NOW(), NOW()),
-- 100x: Report/Moderation
(100, 'REVIEW_REPORT_REASON', '스팸/욕설/부적절 등', 1, 100, NOW(), NOW()),
(101, 'REPORT_STATUS', '접수/검토/조치/반려', 1, 101, NOW(), NOW());


-- 여기서부터 code 테이블에 데이터 삽입입니다
-- 서브쿼리를 이용해서 code 테이블이 code_group_id 를 참조할 수 있도록 함 - 최승현

-- 10: QNA_STATUS (101~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 10), 101, 'WAITING', '대기', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 10), 102, 'ANSWERED', '답변 완료', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 10), 103, 'HIDDEN', '숨김/비공개', 1, 3, NOW(), NOW());

-- 20: DELIVERY_STATUS (201~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 20), 201, 'PREPARING', '출고 준비', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 20), 202, 'SHIPPING', '배송 중', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 20), 203, 'DELIVERED', '배송 완료', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 20), 204, 'RETURNED', '반송/재발송', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 20), 205, 'DELAYED', '배송 지연', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 20), 206, 'CANCELED', '배송 취소', 1, 6, NOW(), NOW());

-- 30: ORDER_STATUS (301~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 30), 301, 'PENDING', '주문 생성', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 302, 'PAID', '결제 완료', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 303, 'PACKING', '상품 준비', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 304, 'SHIPPED', '출고 완료', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 305, 'DELIVERED', '배송 완료', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 306, 'COMPLETED', '구매 확정', 1, 6, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 307, 'CANCELED', '주문 취소', 1, 7, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 308, 'REFUND_REQUESTED', '환불 요청', 1, 8, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 309, 'REFUNDED', '환불 완료', 1, 9, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 310, 'PARTIAL_REFUNDED', '부분 환불', 1, 10, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 30), 311, 'FAILED', '결제/출고 실패', 1, 11, NOW(), NOW());

-- 40: PAYMENT_STATUS (401~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 40), 401, 'READY', '결제 대기', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 40), 402, 'AUTHORIZED', '승인 완료', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 40), 403, 'PAID', '결제 완료', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 40), 404, 'CANCEL_REQUESTED', '취소 요청', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 40), 405, 'CANCELED', '결제 취소', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 40), 406, 'FAILED', '결제 실패', 1, 6, NOW(), NOW());

-- 41: PAYMENT_METHOD (411~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 41), 411, 'CARD', '신용/체크카드', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 412, 'VIRTUAL_ACCOUNT', '무통장입금', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 413, 'ACCOUNT_TRANSFER', '계좌이체', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 414, 'MOBILE', '휴대폰 결제', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 415, 'NAVER_PAY', '네이버페이', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 416, 'KAKAO_PAY', '카카오페이', 1, 6, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 417, 'TOSS_PAY', '토스페이', 1, 7, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 41), 418, 'PAYPAL', '페이팔', 1, 8, NOW(), NOW());

-- 50: COUPON_STATUS (501~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 50), 501, 'ACTIVE', '활성', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 50), 502, 'SCHEDULED', '시작 대기', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 50), 503, 'EXPIRED', '만료', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 50), 504, 'DELETED', '삭제', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 50), 505, 'PAUSED', '일시 중지', 1, 5, NOW(), NOW());

-- 51: DISCOUNT_TYPE (511~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 51), 511, 'AMOUNT', '정액', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 51), 512, 'PERCENT', '정률', 1, 2, NOW(), NOW());

-- 52: APPLICABLE_SCOPE (521~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 52), 521, 'ALL', '전체', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 52), 522, 'PRODUCT', '상품', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 52), 523, 'CATEGORY', '카테고리', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 52), 524, 'BRAND', '브랜드', 1, 4, NOW(), NOW());

-- 53: CHANNEL (531~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 53), 531, 'ALL', '전체', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 53), 532, 'WEB', '웹', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 53), 533, 'APP', '앱', 1, 3, NOW(), NOW());

-- 60: REFUND_STATUS (601~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 60), 601, 'REQUESTED', '요청', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 60), 602, 'APPROVED', '승인', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 60), 603, 'REJECTED', '반려', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 60), 604, 'PROCESSING', '처리 중', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 60), 605, 'COMPLETED', '완료', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 60), 606, 'FAILED', '실패', 1, 6, NOW(), NOW());

-- 61: REFUND_REASON (611~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 61), 611, 'CHANGE_OF_MIND', '단순 변심', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 61), 612, 'DEFECTIVE', '상품 불량', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 61), 613, 'WRONG_ITEM', '오배송', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 61), 614, 'DELIVERY_DELAY', '배송 지연', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 61), 615, 'OTHER', '기타', 1, 5, NOW(), NOW());

-- 62: REFUND_DELIVERY_OPTION (621~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 62), 621, 'SELF_SHIP', '고객 직접 반송', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 62), 622, 'CARRIER_PICKUP', '택배사 수거', 1, 2, NOW(), NOW());

-- 70: USER_ROLE (701~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 70), 701, 'USER', '일반회원', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 70), 702, 'ADMIN_BUSINESS', '관리자-영업', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 70), 703, 'ADMIN_DEVELOP', '관리자-개발', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 70), 704, 'ADMIN_MARKETING', '관리자-마케팅', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 70), 705, 'ADMIN_MASTER', '관리자-마스터', 1, 5, NOW(), NOW());

-- 71: USER_STATUS (711~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 71), 711, 'INACTIVE', '이메일 미인증', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 71), 712, 'ACTIVE', '정상', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 71), 713, 'SUSPENDED', '정지', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 71), 714, 'DORMANT', '휴면', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 71), 715, 'DELETED', '탈퇴', 1, 5, NOW(), NOW());

-- 80: LOGIN_PROVIDER (801~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 80), 801, 'LOCAL', '로컬', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 80), 802, 'GOOGLE', '구글', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 80), 803, 'NAVER', '네이버', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 80), 804, 'KAKAO', '카카오', 1, 4, NOW(), NOW());

-- 90: TERMS_TYPE (901~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 90), 901, 'SERVICE', '서비스 이용약관', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 90), 902, 'PRIVACY', '개인정보 처리방침', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 90), 903, 'MARKETING', '마케팅 수신 동의', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 90), 904, 'GUIDE', '이용 안내', 1, 4, NOW(), NOW());

-- 100: REVIEW_REPORT_REASON (1001~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 100), 1001, 'SPAM', '스팸/광고', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 100), 1002, 'ABUSE', '욕설/혐오', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 100), 1003, 'INAPPROPRIATE', '부적절한 내용', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 100), 1004, 'ADVERTISING', '홍보성 글', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 100), 1005, 'OFF_TOPIC', '주제와 무관', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 100), 1006, 'PERSONAL_INFO', '개인정보 노출', 1, 6, NOW(), NOW());

-- 101: REPORT_STATUS (1011~)
INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 101), 1011, 'PENDING', '접수', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 101), 1012, 'REVIEWED', '검토 완료', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 101), 1013, 'ACTION_TAKEN', '조치 완료', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 101), 1014, 'REJECTED', '반려', 1, 4, NOW(), NOW());
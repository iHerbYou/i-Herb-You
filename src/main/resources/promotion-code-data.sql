-- Promotion domain code group and codes
INSERT INTO code_group (value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES (110, 'POINT_EVENT_TYPE', '포인트 적립/사용 유형', 1, 110, NOW(), NOW());

INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES ((SELECT id FROM code_group WHERE value = 110), 1101, 'EARN_REVIEW_TEXT', '리뷰 텍스트 작성 적립', 1, 1, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 110), 1102, 'EARN_REVIEW_IMAGE', '리뷰 이미지 작성 추가 적립', 1, 2, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 110), 1103, 'EARN_ORDER_COMPLETION', '구매 확정 적립', 1, 3, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 110), 1104, 'USE_ORDER_PAYMENT', '주문 결제 시 포인트 사용', 1, 4, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 110), 1105, 'RESTORE_ORDER_CANCEL', '주문 취소 포인트 복구', 1, 5, NOW(), NOW()),
       ((SELECT id FROM code_group WHERE value = 110), 1106, 'EXPIRE', '포인트 만료', 1, 6, NOW(), NOW());

-- Promotion-specific seed data

-- 110: 포인트 적립/사용 유형 코드 그룹 및 코드 값
INSERT INTO code_group (value, display_name, description, is_active, sort_order, created_at, updated_at)
VALUES (110, 'POINT_EVENT_TYPE', '포인트 적립/사용 유형', 1, 110, NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name = VALUES(display_name);

INSERT INTO code (code_group_id, value, display_name, description, is_active, sort_order, created_at, updated_at)
SELECT cg.id, c.value, c.display_name, c.description, c.is_active, c.sort_order, NOW(), NOW()
FROM (
         SELECT 1101 AS value, 'EARN_REVIEW_TEXT' AS display_name, '리뷰 텍스트 작성 적립' AS description, 1 AS is_active, 1 AS sort_order
         UNION ALL
         SELECT 1102, 'EARN_REVIEW_IMAGE', '리뷰 이미지 작성 추가 적립', 1, 2
         UNION ALL
         SELECT 1103, 'EARN_ORDER_COMPLETION', '구매 확정 적립', 1, 3
         UNION ALL
         SELECT 1104, 'USE_ORDER_PAYMENT', '주문 결제 시 포인트 사용', 1, 4
         UNION ALL
         SELECT 1105, 'RESTORE_ORDER_CANCEL', '주문 취소 포인트 복구', 1, 5
         UNION ALL
         SELECT 1106, 'EXPIRE', '포인트 만료', 1, 6
     ) c
         JOIN code_group cg ON cg.value = 110
WHERE NOT EXISTS (SELECT 1 FROM code existing WHERE existing.code_group_id = cg.id AND existing.value = c.value);

-- 웰컴 쿠폰: 신규 가입 3,000원 할인(정액 할인, 활성 상태)
INSERT INTO coupon (name, code, description, discount_code_id, status_code_id, discount_value, max_discount_amount, min_order_amount,
                    per_user_limit, total_issuable, issued_count, stackable, combinable_with_others, is_first_purchase_only,
                    starts_at, coupon_ends_at, created_at, updated_at)
SELECT '웰컴 쿠폰', 'WELCOME-14D', '신규 가입 3,000원 할인',
       d.id AS discount_code_id,
       s.id AS status_code_id,
       3000, 3000, 0,
       1, 0, 0,
       0, 0, 1,
       NOW(), DATE_ADD(NOW(), INTERVAL 2 YEAR), NOW(), NOW()
FROM (SELECT id FROM code WHERE value = 511 LIMIT 1) d,
     (SELECT id FROM code WHERE value = 501 LIMIT 1) s
WHERE NOT EXISTS (SELECT 1 FROM coupon WHERE code = 'WELCOME-14D');

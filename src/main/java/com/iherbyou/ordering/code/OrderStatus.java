package com.iherbyou.ordering.code;

/**
 * 주문 상태 코드 값 래핑. Code 테이블(Order Status 그룹)과 매핑된다.
 */
public enum OrderStatus {
    PENDING(301),
    PAID(302),
    PACKING(303),
    SHIPPED(304),
    DELIVERED(305),
    COMPLETED(306),
    CANCELED(307),
    REFUND_REQUESTED(308),
    REFUNDED(309),
    PARTIALLY_REFUNDED(310),
    FAILED(311);

    public static final int GROUP_VALUE = 30;

    private final int codeValue;

    private static final java.util.Map<Integer, OrderStatus> BY_CODE =
            java.util.Arrays.stream(values())
                    .collect(java.util.stream.Collectors.toMap(OrderStatus::getCodeValue, it -> it));

    OrderStatus(int codeValue) {
        this.codeValue = codeValue;
    }

    public int getCodeValue() {
        return codeValue;
    }

    public static OrderStatus fromCodeValue(int codeValue) {
        OrderStatus status = BY_CODE.get(codeValue);
        if (status == null) {
            throw new IllegalArgumentException("Unknown order status code: " + codeValue);
        }
        return status;
    }
}

package com.iherbyou.ordering.entity;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.user.entity.UserAddress;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 배송 id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 주문 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code; // 배송 상태 코드 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private UserAddress userAddress; // 배송지 주소 (다수 배송이 같은 주소를 공유 가능)

    @Column(nullable = false, unique = true, length = 40)
    private String trackingNumber; // 송장번호

    @Column(length = 100, nullable = false)
    private String deliveryCompany; // 택배사

    @Column
    private String delMemo; // 배송 메모

    @Column
    private LocalDateTime delStartAt; // 배송 시작일

    @Column
    private LocalDateTime delCompleteAt; // 배송 완료일

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "recipient", column = @Column(name = "delivery_recipient", length = 20)),
            @AttributeOverride(name = "phone", column = @Column(name = "delivery_phone", length = 30)),
            @AttributeOverride(name = "zipcode", column = @Column(name = "delivery_zipcode", length = 10)),
            @AttributeOverride(name = "addressLine1", column = @Column(name = "delivery_address_line1", length = 200)),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "delivery_address_line2", length = 200))
    })
    private AddressSnapshot shippingAddress;

    public static Delivery create(Order order,
                                  Code status,
                                  UserAddress address,
                                  AddressSnapshot snapshot,
                                  String deliveryCompany,
                                  String trackingNumber,
                                  LocalDateTime startedAt) {
        Objects.requireNonNull(order, "order must not be null");
        Objects.requireNonNull(status, "status must not be null");
        if (deliveryCompany == null || deliveryCompany.isBlank()) {
            throw new IllegalArgumentException("deliveryCompany must not be blank");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }

        AddressSnapshot resolvedSnapshot = snapshot != null
                ? snapshot.copy()
                : AddressSnapshot.from(Objects.requireNonNull(address, "address must not be null when snapshot absent"));

        Delivery delivery = Delivery.builder()
                .order(order)
                .code(status)
                .userAddress(address)
                .deliveryCompany(deliveryCompany)
                .trackingNumber(trackingNumber)
                .delStartAt(startedAt)
                .shippingAddress(resolvedSnapshot)
                .build();

        order.attachDelivery(delivery);
        return delivery;
    }

    public void updateTracking(String deliveryCompany, String trackingNumber, LocalDateTime startedAt) {
        if (deliveryCompany == null || deliveryCompany.isBlank()) {
            throw new IllegalArgumentException("deliveryCompany must not be blank");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("trackingNumber must not be blank");
        }

        this.deliveryCompany = deliveryCompany;
        this.trackingNumber = trackingNumber;
        this.delStartAt = startedAt;
    }

    public void updateStatus(Code status, String memo, LocalDateTime completeAt, boolean clearComplete) {
        Objects.requireNonNull(status, "status must not be null");

        this.code = status;
        this.delMemo = memo;
        if (clearComplete) {
            this.delCompleteAt = null;
        } else {
            this.delCompleteAt = completeAt;
        }
    }

    void attachOrder(Order order) {
        this.order = order;
    }

    public void ensureShippingAddress(AddressSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        this.shippingAddress = snapshot.copy();
    }

    public void ensureUserAddress(UserAddress address) {
        if (address == null) {
            return;
        }
        this.userAddress = address;
    }
}

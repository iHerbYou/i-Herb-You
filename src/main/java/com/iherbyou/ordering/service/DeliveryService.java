package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.ordering.Delivery;
import com.iherbyou.ordering.Order;
import com.iherbyou.ordering.repository.DeliveryRepository;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.ordering.common.CodeFinder;
import com.iherbyou.ordering.dto.DeliveryRegisterRequest;
import com.iherbyou.user.entity.UserAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final CodeFinder codeFinder; // 추가

    public Delivery registerTracking(Long orderId, DeliveryRegisterRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        String deliveryCompany = request.getDeliveryCompany();
        String trackingNumber = request.getTrackingNumber();

        LocalDateTime now = LocalDateTime.now();

        // 기존 배송 있으면 업데이트, 없으면 생성
        Delivery delivery = deliveryRepository.findByOrder_Id(orderId).orElse(null);

        if (delivery == null) {
            // 1) 상태코드: DELIVERY_STATUS.READY (송장 등록과 동시에 배송 준비중으로 취급)
            Code ready = codeFinder.get("DELIVERY_STATUS", "READY");

            // 2) 필수값 세팅
            delivery = Delivery.builder()
                    .order(order)
                    .code(ready)
                    .deliveryCompany(deliveryCompany)
                    .trackingNumber(trackingNumber)
                    .delStartAt(now)
                    .build();

            // 3) 주소는 nullable=false면 반드시 세팅
            UserAddress defaultAddr = order.getUser().getAddresses().stream()
                    .filter(a -> a.isDefault())    // 기본 주소가 있으면
                    .findFirst()
                    .orElseGet(() -> order.getUser().getAddresses().isEmpty()
                            ? null
                            : order.getUser().getAddresses().get(0)); // 없으면 첫 번째

            if (defaultAddr == null) {
                // 주소가 필수(nullable=false)인데 못 구하면 예외로 막는 게 안전
                throw new IllegalStateException("no user address to attach for delivery");
            }
            delivery.setUserAddress(defaultAddr);
        } else {
            // 기존 건이면 필요한 필드만 갱신
            delivery.setDeliveryCompany(deliveryCompany);
            delivery.setTrackingNumber(trackingNumber);
            delivery.setDelStartAt(now);
        }

        order.setDelivery(delivery);

        return deliveryRepository.save(delivery);
    }

}
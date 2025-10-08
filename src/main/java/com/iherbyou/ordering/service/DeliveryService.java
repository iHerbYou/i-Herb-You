package com.iherbyou.ordering.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.ordering.code.OrderStatus;
import com.iherbyou.ordering.config.DeliveryProgressProperties;
import com.iherbyou.ordering.entity.AddressSnapshot;
import com.iherbyou.ordering.entity.Delivery;
import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.repository.DeliveryRepository;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.user.entity.UserAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeliveryService {

    private static final int DELIVERY_STATUS_PREPARING = 201;
    private static final int DELIVERY_STATUS_SHIPPING = 202;
    private static final int DELIVERY_STATUS_DELIVERED = 203;

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final CodeService codeService;
    private final OrderService orderService;
    private final TrackingNumberGenerator trackingNumberGenerator;
    private final TaskScheduler taskScheduler;
    private final DeliveryProgressProperties deliveryProgressProperties;
    private final PlatformTransactionManager transactionManager;

    public Delivery registerTracking(Long userId, Long orderId, String deliveryCompany, String trackingNumber) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required for delivery action");
        }
        if (deliveryCompany == null || deliveryCompany.isBlank()) {
            throw new IllegalArgumentException("deliveryCompany is required");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("trackingNumber is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        ensureOrderOwner(order, userId);
        ensureOrderAllowsTracking(order);

        LocalDateTime now = LocalDateTime.now();

        UserAddress address = resolveUserAddress(order);
        AddressSnapshot shippingSnapshot = ensureOrderShippingSnapshot(order, address);

        Delivery existing = deliveryRepository.findByOrder_Id(orderId).orElse(null);
        boolean newlyCreated = existing == null;

        Delivery delivery;
        if (existing == null) {
            Code ready = requireCode(20, DELIVERY_STATUS_PREPARING, "DELIVERY_STATUS:PREPARING");
            delivery = Delivery.create(order, ready, address, shippingSnapshot, deliveryCompany, trackingNumber, now);
        } else {
            delivery = existing;
            delivery.updateTracking(deliveryCompany, trackingNumber, now);
            delivery.ensureUserAddress(address);
            delivery.ensureShippingAddress(shippingSnapshot);
            order.attachDelivery(delivery);
        }

        Delivery saved = deliveryRepository.save(delivery);

        String correlationId = "DELIVERY_TRACKING:" + orderId + ":" + trackingNumber;
        String actor = "user:" + userId;
        orderService.updateStatus(orderId, OrderStatus.PACKING, "DELIVERY_TRACKING_REGISTERED", actor, correlationId);
        log.info("[DeliveryTrackingRegistered] orderId={} deliveryId={} company={} tracking={} actor={}",
                orderId, saved.getId(), deliveryCompany, trackingNumber, actor);

        if (newlyCreated) {
            scheduleDeliveryProgress(orderId, actor, now);
        }

        return saved;
    }

    public Delivery prepareAutomaticDelivery(Long orderId, String actor) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        ensureOrderAllowsTracking(order);

        String resolvedActor = resolveActor(actor);
        LocalDateTime now = LocalDateTime.now();

        TrackingNumberGenerator.GeneratedTracking generated = trackingNumberGenerator.generate();
        UserAddress address = resolveUserAddress(order);
        AddressSnapshot shippingSnapshot = ensureOrderShippingSnapshot(order, address);
        Delivery existing = deliveryRepository.findByOrder_Id(orderId).orElse(null);
        boolean newlyCreated = existing == null;

        Delivery delivery;
        if (existing == null) {
            Code ready = requireCode(20, DELIVERY_STATUS_PREPARING, "DELIVERY_STATUS:PREPARING");
            delivery = Delivery.create(order, ready, address, shippingSnapshot, generated.deliveryCompany(), generated.trackingNumber(), now);
        } else {
            delivery = existing;
            delivery.updateTracking(generated.deliveryCompany(), generated.trackingNumber(), now);
            delivery.ensureUserAddress(address);
            delivery.ensureShippingAddress(shippingSnapshot);
            order.attachDelivery(delivery);
        }

        Delivery saved = deliveryRepository.save(delivery);

        String correlationId = "DELIVERY_AUTO_PREPARED:" + orderId + ":" + saved.getTrackingNumber();
        orderService.updateStatus(orderId, OrderStatus.PACKING, "DELIVERY_AUTO_PREPARED", resolvedActor, correlationId);
        log.info("[DeliveryAutoPrepared] orderId={} deliveryId={} company={} tracking={} actor={}",
                orderId, saved.getId(), generated.deliveryCompany(), generated.trackingNumber(), resolvedActor);

        if (newlyCreated) {
            scheduleDeliveryProgress(orderId, resolvedActor, now);
        }

        return saved;
    }

    public Delivery changeStatus(Long orderId, Integer statusValue, String memo, LocalDateTime completeAt) {
        if (statusValue == null) {
            throw new IllegalArgumentException("status value is required");
        }

        Delivery delivery = deliveryRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new IllegalStateException("delivery not found for order: " + orderId));

        Code newStatus = requireCode(20, statusValue, "DELIVERY_STATUS:" + statusValue);

        LocalDateTime appliedCompleteAt = completeAt;
        if (appliedCompleteAt == null && statusValue == DELIVERY_STATUS_DELIVERED) {
            appliedCompleteAt = LocalDateTime.now();
        }

        boolean clearComplete = statusValue == DELIVERY_STATUS_PREPARING || statusValue == DELIVERY_STATUS_SHIPPING;
        delivery.updateStatus(newStatus, memo, appliedCompleteAt, clearComplete);

        Delivery saved = deliveryRepository.save(delivery);

        if (statusValue == DELIVERY_STATUS_PREPARING) {
            String correlationId = "DELIVERY_STATUS:" + orderId + ":" + DELIVERY_STATUS_PREPARING;
            orderService.updateStatus(orderId, OrderStatus.PACKING, "DELIVERY_STATUS_UPDATED", "system", correlationId);
        } else if (statusValue == DELIVERY_STATUS_SHIPPING) {
            String correlationId = "DELIVERY_STATUS:" + orderId + ":" + DELIVERY_STATUS_SHIPPING;
            orderService.updateStatus(orderId, OrderStatus.SHIPPED, "DELIVERY_STATUS_UPDATED", "system", correlationId);
        } else if (statusValue == DELIVERY_STATUS_DELIVERED) {
            String correlationId = "DELIVERY_STATUS:" + orderId + ":" + DELIVERY_STATUS_DELIVERED;
            orderService.updateStatus(orderId, OrderStatus.DELIVERED, "DELIVERY_STATUS_UPDATED", "system", correlationId);
            scheduleOrderCompletion(orderId, appliedCompleteAt);
        }

        log.info("[DeliveryStatusUpdated] orderId={} deliveryId={} statusValue={} actor=system", orderId, delivery.getId(), statusValue);
        return saved;
    }

    private void ensureOrderOwner(Order order, Long userId) {
        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied for order " + order.getId());
        }
    }

    private void ensureOrderAllowsTracking(Order order) {
        if (order.getOrderStatusCode() == null) {
            throw new IllegalStateException("order status is not set");
        }
        OrderStatus status = OrderStatus.fromCodeValue(order.getOrderStatusCode().getValue());
        if (status != OrderStatus.PAID && status != OrderStatus.PACKING) {
            throw new IllegalStateException("order status does not allow delivery registration: " + status);
        }
    }

    private Code requireCode(int groupValue, int codeValue, String context) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("code not found: " + context);
        }
        return code;
    }

    private UserAddress resolveUserAddress(Order order) {
        if (order.getUser() == null) {
            throw new IllegalStateException("order user is required for delivery address");
        }
        Optional<UserAddress> defaultAddress = order.getUser().getAddresses().stream()
                .filter(UserAddress::isDefault)
                .findFirst();
        if (defaultAddress.isPresent()) {
            return defaultAddress.get();
        }
        if (order.getUser().getAddresses().isEmpty()) {
            throw new IllegalStateException("no user address to attach for delivery");
        }
        return order.getUser().getAddresses().get(0);
    }

    private AddressSnapshot ensureOrderShippingSnapshot(Order order, UserAddress address) {
        AddressSnapshot snapshot = order.getShippingAddress();
        if (snapshot != null) {
            return snapshot;
        }
        if (address == null) {
            throw new IllegalStateException("no user address to attach for delivery");
        }
        AddressSnapshot created = AddressSnapshot.from(address);
        order.updateShippingAddress(created);
        return created;
    }

    private void scheduleDeliveryProgress(Long orderId, String actor, LocalDateTime baseTime) {
        Duration preparingToShipping = deliveryProgressProperties.getPreparingToShipping();
        Duration shippingToDelivered = deliveryProgressProperties.getShippingToDelivered();

        LocalDateTime shippingAt = addDuration(baseTime, preparingToShipping);
        scheduleStatusChange(orderId, DELIVERY_STATUS_SHIPPING, "AUTO_PROGRESS", shippingAt, null);

        LocalDateTime deliveredAtBase = shippingAt != null ? shippingAt : baseTime;
        LocalDateTime deliveredAt = addDuration(deliveredAtBase, shippingToDelivered);
        scheduleStatusChange(orderId, DELIVERY_STATUS_DELIVERED, "AUTO_PROGRESS", deliveredAt, deliveredAt);

        log.info("[DeliveryProgressScheduled] orderId={} actor={} shipAt={} deliveredAt={}",
                orderId, actor, shippingAt, deliveredAt);
    }

    private LocalDateTime addDuration(LocalDateTime base, Duration offset) {
        if (base == null || offset == null) {
            return base;
        }
        if (offset.isNegative()) {
            return base;
        }
        return offset.isZero() ? base : base.plus(offset);
    }

    private void scheduleOrderCompletion(Long orderId, LocalDateTime deliveredAt) {
        Duration completionWindow = deliveryProgressProperties.getCompletionWindow();
        LocalDateTime base = deliveredAt != null ? deliveredAt : LocalDateTime.now();
        LocalDateTime completionAt = addDuration(base, completionWindow);
        if (completionAt == null) {
            return;
        }

        Instant completionInstant = toFutureInstant(completionAt);
        Runnable task = () -> {
            try {
                TransactionTemplate template = new TransactionTemplate(transactionManager);
                template.execute(status -> {
                    orderService.updateStatus(
                            orderId,
                            OrderStatus.COMPLETED,
                            "ORDER_AUTO_COMPLETED",
                            "system",
                            "ORDER_AUTO_COMPLETED:" + orderId
                    );
                    return null;
                });
            } catch (Exception e) {
                log.warn("[OrderAutoCompletion][failed] orderId={} scheduledAt={}", orderId, completionAt, e);
            }
        };
        taskScheduler.schedule(task, Date.from(completionInstant));
        log.info("[OrderAutoCompletionScheduled] orderId={} completeAt={}", orderId, completionAt);
    }

    private void scheduleStatusChange(Long orderId, int statusValue, String memo, LocalDateTime when, LocalDateTime completeAt) {
        if (when == null) {
            return;
        }
        Instant instant = toFutureInstant(when);
        Runnable task = () -> {
            try {
                TransactionTemplate template = new TransactionTemplate(transactionManager);
                template.execute(status -> {
                    changeStatus(orderId, statusValue, memo, completeAt);
                    return null;
                });
            } catch (Exception e) {
                log.warn("[DeliveryProgressScheduled][failed] orderId={} statusValue={}", orderId, statusValue, e);
            }
        };
        taskScheduler.schedule(task, Date.from(instant));
    }

    private Instant toFutureInstant(LocalDateTime when) {
        Instant instant = when.atZone(ZoneId.systemDefault()).toInstant();
        Instant now = Instant.now();
        if (instant.isBefore(now)) {
            return now;
        }
        return instant;
    }

    private String resolveActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}

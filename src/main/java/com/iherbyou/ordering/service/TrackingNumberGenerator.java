package com.iherbyou.ordering.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TrackingNumberGenerator {

    private static final DateTimeFormatter TIMESTAMP_PATTERN = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private static final List<Carrier> CARRIERS = List.of(
            new Carrier("CJ Logistics", "CJ"),
            new Carrier("Lotte Global Logistics", "LGL"),
            new Carrier("Korea Post", "KRPOS"),
            new Carrier("Hanjin Express", "HJ"),
            new Carrier("Logen Express", "LGN"),
            new Carrier("CU Post", "CU"),
            new Carrier("GS Post", "GS"),
            new Carrier("Homepick", "HP")
    );

    public GeneratedTracking generate() {
        Carrier carrier = pickCarrier();
        return new GeneratedTracking(carrier.name(), buildTrackingNumber(carrier.prefix()));
    }

    private Carrier pickCarrier() {
        int index = ThreadLocalRandom.current().nextInt(CARRIERS.size());
        return CARRIERS.get(index);
    }

    private String buildTrackingNumber(String prefix) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_PATTERN);
        String randomDigits = randomDigits(5);
        return prefix + timestamp + randomDigits;
    }

    private String randomDigits(int length) {
        StringBuilder builder = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    private record Carrier(String name, String prefix) {
    }

    public static final class GeneratedTracking {
        private final String deliveryCompany;
        private final String trackingNumber;

        public GeneratedTracking(String deliveryCompany, String trackingNumber) {
            this.deliveryCompany = deliveryCompany;
            this.trackingNumber = trackingNumber;
        }

        public String deliveryCompany() {
            return deliveryCompany;
        }

        public String trackingNumber() {
            return trackingNumber;
        }
    }
}


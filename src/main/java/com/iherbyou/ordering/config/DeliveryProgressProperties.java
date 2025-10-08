package com.iherbyou.ordering.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "delivery.progress")
public class DeliveryProgressProperties {

    private Duration preparingToShipping = Duration.ofSeconds(30);
    private Duration shippingToDelivered = Duration.ofSeconds(30);
    private Duration completionWindow = Duration.ofMinutes(2);

}

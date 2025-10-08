package com.iherbyou.ordering.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "refund.policy")
public class RefundPolicyProperties {

    private Duration completionWindow = Duration.ofMinutes(2);
}


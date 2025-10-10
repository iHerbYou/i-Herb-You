package com.iherbyou.ordering.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "refund.policy")
public class RefundPolicyProperties {

    // 배송 완료 시점부터 환불 가능 기간
    private Duration completionWindow = Duration.ofMinutes(5);
}


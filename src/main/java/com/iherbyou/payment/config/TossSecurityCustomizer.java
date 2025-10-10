package com.iherbyou.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
public class TossSecurityCustomizer {

    @Bean
    public WebSecurityCustomizer tossPaymentWebSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/api/payments/confirm");
    }
}

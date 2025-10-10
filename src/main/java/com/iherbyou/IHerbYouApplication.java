package com.iherbyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan // @ConfigurationProperties beans (delivery/refund settings 등) 자동 등록
@EnableJpaAuditing // Spring Data JPA Auditing 활성화
@EnableScheduling // 배송 자동 진행 등 TaskScheduler 기반 예약 작업 활성화
public class IHerbYouApplication {

    public static void main(String[] args) {
        SpringApplication.run(IHerbYouApplication.class, args);
    }

}

package com.iherbyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Spring Data JPA Auditing 활성화
public class IHerbYouApplication {

    public static void main(String[] args) {
        SpringApplication.run(IHerbYouApplication.class, args);
    }

}

package com.iherbyou.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("iHerbYou API")
                        .description("아이허브유 백엔드 REST API 문서")
                        .version("v1"))
                .servers(List.of(
                        new Server().url("https://www.iherbyou.store").description("Production"),
                        new Server().url("http://localhost:8080").description("Local")
                ));
    }
}
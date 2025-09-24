package com.iherbyou.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGroupConfig {

    @Bean
    public GroupedOpenApi cartApi() {
        return GroupedOpenApi.builder()
                .group("Cart")
                .packagesToScan("com.iherbyou.cart.controller")
                .pathsToMatch("/api/cart/**", "/api/wishlist/**")
                .build();
    }

    @Bean
    public GroupedOpenApi catalogApi() {
        return GroupedOpenApi.builder()
                .group("Catalog")
                .packagesToScan("com.iherbyou.catalog.controller")
                .pathsToMatch("/api/products/**", "/api/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi communityApi() {
        return GroupedOpenApi.builder()
                .group("Community")
                .packagesToScan("com.iherbyou.community.controller")
                .pathsToMatch("/api/community/**", "/api/reviews/**", "/api/qna/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderingApi() {
        return GroupedOpenApi.builder()
                .group("Ordering")
                .packagesToScan("com.iherbyou.ordering.controller")
                .pathsToMatch("/api/orders/**", "/api/payments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User")
                .packagesToScan("com.iherbyou.user.controller")
                .pathsToMatch("/api/users/**", "/api/auth/**")
                .build();
    }
}
package com.iherbyou.promotion.policy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "promotion.policy")
public class PromotionPolicyProperties {

    private final WelcomeCoupon welcomeCoupon = new WelcomeCoupon();
    private final ReviewPoint reviewPoint = new ReviewPoint();
    private final OrderPoint orderPoint = new OrderPoint();
    private int couponMinOrderAmount = 50_000;
    private int pointMinOrderAmount = 50_000;

    @Getter
    @Setter
    public static class WelcomeCoupon {
        private boolean enabled = true;
        private String couponCode = "WELCOME-14D";
        private int validityDays = 14;
    }

    @Getter
    @Setter
    public static class ReviewPoint {
        private int textPoint = 500;
        private int imagePoint = 1000;
        private int validityDays = 365;
    }

    @Getter
    @Setter
    public static class OrderPoint {
        private int percent = 5;
        private int validityDays = 365;
        private boolean roundDown = true;
    }
}

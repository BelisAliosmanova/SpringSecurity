package com.springSecurity.springSecurity.config.rateLimiting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "resilience4j.rate-limiter")
@Getter
@Setter
public class RateLimiterConfigProperties {
    private Map<String, RateLimiterInstanceConfig> instances;

    @Getter
    @Setter
    public static class RateLimiterInstanceConfig {
        private int limitForPeriod;
        private String limitRefreshPeriod;
        private int timeoutDuration;
    }
}

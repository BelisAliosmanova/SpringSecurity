package com.springSecurity.springSecurity.config.rateLimiting;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class Resilience4jConfig {
    private final RateLimiterConfigProperties rateLimiterConfigProperties;

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterRegistry registry = RateLimiterRegistry.ofDefaults();

        Map<String, RateLimiterConfigProperties.RateLimiterInstanceConfig> instances =
                rateLimiterConfigProperties.getInstances();

        for (Map.Entry<String, RateLimiterConfigProperties.RateLimiterInstanceConfig> entry : instances.entrySet()) {
            String limiterName = entry.getKey();
            RateLimiterConfigProperties.RateLimiterInstanceConfig config = entry.getValue();

            RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                    .limitForPeriod(config.getLimitForPeriod())
                    .limitRefreshPeriod(Duration.parse("PT" + config.getLimitRefreshPeriod().toUpperCase()))
                    .timeoutDuration(Duration.ofMillis(config.getTimeoutDuration()))
                    .build();

            registry.rateLimiter(limiterName, rateLimiterConfig);
        }

        return registry;
    }
}
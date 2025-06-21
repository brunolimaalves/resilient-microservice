package br.com.poc.resilientmicroservice.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    private static final String ORDER = "order";
    private static final String PAYMENT = "payment";

    @Bean
    public CircuitBreaker paymentCircuitBreaker() {
        return CircuitBreaker.of(PAYMENT, CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(5)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build());
    }

    @Bean
    public CircuitBreaker orderCircuitBreaker() {
        return CircuitBreaker.of(ORDER, CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(5)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build());
    }

    @Bean
    public RateLimiter paymentRateLimiter() {
        return RateLimiter.of(PAYMENT, RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build());
    }

    @Bean
    public RateLimiter orderRateLimiter() {
        return RateLimiter.of(ORDER, RateLimiterConfig.custom()
                .limitForPeriod(10)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build());
    }

    @Bean
    public Bulkhead paymentBulkhead() {
        return Bulkhead.of(PAYMENT, BulkheadConfig.custom()
                .maxConcurrentCalls(2)
                .maxWaitDuration(Duration.ofMillis(0))
                .build());
    }

    @Bean
    public Bulkhead orderBulkhead() {
        return Bulkhead.of(ORDER, BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(0))
                .build());
    }

}
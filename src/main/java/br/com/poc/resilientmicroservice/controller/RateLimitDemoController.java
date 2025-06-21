package br.com.poc.resilientmicroservice.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/ratelimit")
@RequiredArgsConstructor
public class RateLimitDemoController {

    private final RateLimiter paymentRateLimiter;

    @GetMapping("/payment")
    public String processPayment() {
        Supplier<String> paymentLogic = () -> {
            simulateDelay(2000);
            if (Math.random() < 0.9) throw new RuntimeException("   Payment error!!  \n ");
            return "  [  Payment OK!  ]  \n ";
        };

        return Decorators.ofSupplier(paymentLogic)
            .withRateLimiter(paymentRateLimiter)
            .withFallback(e -> "  Payment processing failed: " + e.getClass().getSimpleName() + " \n ")
            .decorate()
            .get();
    }

    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
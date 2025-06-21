package br.com.poc.resilientmicroservice.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/circuitbreaker")
@RequiredArgsConstructor
public class CircuitBreakerDemoController {

    private final CircuitBreaker paymentCircuitBreaker;

    @GetMapping("/payment")
    public String processPayment() {
        Supplier<String> paymentLogic = () -> {
            simulateDelay(2000);
            if (Math.random() < 0.2) throw new RuntimeException("  Payment error!!  \n ");
            return " Payment processed successfully. \n ";
        };

        return Decorators.ofSupplier(paymentLogic)
            .withCircuitBreaker(paymentCircuitBreaker)
            .withFallback(e -> " Payment processing failed: " + e.getClass().getSimpleName()+ " \n ")
            .decorate()
            .get();
    }

    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
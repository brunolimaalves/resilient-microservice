package br.com.poc.resilientmicroservice.controller;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.decorators.Decorators;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bulkhead")
@RequiredArgsConstructor
public class BulkheadDemoController {

    private final Bulkhead paymentBulkhead;

    @GetMapping("/payment")
    public String processPayment() {
        return Decorators.ofSupplier(() -> {
            simulateDelay(5000); // simula travamento
            return "  [  Payment OK!  ]  \n ";
        })
        .withBulkhead(paymentBulkhead)
        .withFallback(e -> "  Payment service unavailable!!  \n" )
        .decorate().get();
    }

    private void simulateDelay(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {}
    }
}
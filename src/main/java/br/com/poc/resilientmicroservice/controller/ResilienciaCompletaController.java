package br.com.poc.resilientmicroservice.controller;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.decorators.Decorators;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/resiliencia")
@RequiredArgsConstructor
public class ResilienciaCompletaController {

    private final Bulkhead paymentBulkhead;
    private final RateLimiter paymentRateLimiter;
    private final CircuitBreaker paymentCircuitBreaker;

    @GetMapping("/payment")
    public String processarPagamentoComTodos() {
        Supplier<String> pagamentoReal = () -> {
            if (Math.random() < 0.2) {
                throw new RuntimeException(" Falha no processamento do pagamento! \n ");
            }
            simulateDelay(2000); // simula lentidão
            return " Pagamento processado com sucesso (Resiliência completa) \n";
        };

        return Decorators.ofSupplier(pagamentoReal)
            .withCircuitBreaker(paymentCircuitBreaker)
            .withRateLimiter(paymentRateLimiter)
            .withBulkhead(paymentBulkhead)
            .withFallback(e -> " Pagamento falhou: " + e.getClass().getSimpleName()+ "\n")
            .decorate()
            .get();
    }

    private void simulateDelay(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
    }
}
package br.com.poc.resilientmicroservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {

    @GetMapping("/api/payment")
    public String processarPagamento() {
        try {
            log.info("Processando pagamento");
            Thread.sleep(5000); // simula lentidão travando o serviço
        } catch (InterruptedException ignored) {}
        return "Payment processado com sucesso!.";
    }
}

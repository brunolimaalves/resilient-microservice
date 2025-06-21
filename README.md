# 🛡️ Resilience4j PoC - Spring Boot

Este projeto demonstra o uso dos principais padrões de **resiliência em microsserviços** utilizando a biblioteca **Resilience4j** com Spring Boot:

- ✅ Circuit Breaker
- ✅ Rate Limiter
- ✅ Bulkhead
- ✅ Fallbacks personalizados
- ✅ Composição de todos os padrões

---

## 🎯 Objetivo

Explorar cenários reais de instabilidade simulada (ex: lentidão, falha, sobrecarga) e como proteger serviços com mecanismos de resiliência desacoplados, mantendo a disponibilidade global do sistema.

---
## 💡 Sobre os padrões usados

### 🔁 Circuit Breaker
Evita chamadas repetidas para um serviço instável, abrindo o “circuito” temporariamente e permitindo que ele se recupere antes de novas tentativas.
```java
public CircuitBreaker paymentCircuitBreaker() {
        return CircuitBreaker.of(PAYMENT, CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(5)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build());
    }
```
| Parâmetro                         | Explicação                                                                                         |
| --------------------------------- | -------------------------------------------------------------------------------------------------- |
| `failureRateThreshold(50)`        | Se mais de 50% das chamadas falharem (exceções), o circuito **entra em estado aberto** (open).     |
| `slidingWindowSize(5)`            | O circuito analisa as **últimas 5 chamadas** para calcular a taxa de falha.                        |
| `waitDurationInOpenState(10s)`    | Após abrir o circuito, ele **espera 10 segundos** antes de tentar fechar parcialmente (half-open). |
| `CircuitBreaker.of(PAYMENT, ...)` | Cria o breaker nomeado `PAYMENT`, útil para logs e métricas separadas por serviço.                 |


### 🚦 Rate Limiter
Controla a taxa de requisições, limitando a quantidade de chamadas permitidas em um determinado intervalo de tempo. Isso protege o serviço de explosões de tráfego inesperadas.

```java
@Bean
public RateLimiter paymentRateLimiter() {
    return RateLimiter.of(PAYMENT, RateLimiterConfig.custom()
            .limitForPeriod(5)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(0))
            .build());
}
```

| Parâmetro                      | Explicação                                                                     |
| ------------------------------ | ------------------------------------------------------------------------------ |
| `limitForPeriod(5)`            | Permite **no máximo 5 chamadas por período**.                                  |
| `limitRefreshPeriod(1s)`       | O período é de **1 segundo** → ou seja, 5 chamadas por segundo.                |
| `timeoutDuration(0ms)`         | Se o limite for atingido, **a requisição falha imediatamente** (sem aguardar). |
| `RateLimiter.of(PAYMENT, ...)` | Aplica a política ao serviço `PAYMENT`.                                        |


### 🧱 Bulkhead
Isola a concorrência entre serviços usando compartimentos dedicados (como threads separadas). Isso garante que, se um serviço estiver sobrecarregado, os demais continuem funcionando normalmente.
```java
@Bean
public Bulkhead paymentBulkhead() {
    return Bulkhead.of(PAYMENT, BulkheadConfig.custom()
    .maxConcurrentCalls(2)
    .maxWaitDuration(Duration.ofMillis(0))
    .build());
}
```
| Parâmetro                   | Explicação                                                                         |
| --------------------------- | ---------------------------------------------------------------------------------- |
| `maxConcurrentCalls(2)`     | Permite **até 2 chamadas simultâneas** para o serviço. As demais serão rejeitadas. |
| `maxWaitDuration(0ms)`      | Se o número máximo for atingido, **a chamada falha na hora**.                      |
| `Bulkhead.of(PAYMENT, ...)` | Nomeia o compartimento de isolamento como `PAYMENT`.                               |


## 🚀 Como executar

### 1. Clonar o repositório

```bash
git clone git@github.com:brunolimaalves/resilient-microservice.git
cd resilient-microservice
```
### 2. Rodar o projeto
```bash
./mvnw spring-boot:run
```

### 3. Executar a simulação
```bash
./simular_resiliencia.sh
```


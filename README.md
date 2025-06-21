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

### 🚦 Rate Limiter
Controla a taxa de requisições, limitando a quantidade de chamadas permitidas em um determinado intervalo de tempo. Isso protege o serviço de explosões de tráfego inesperadas.

### 🧱 Bulkhead
Isola a concorrência entre serviços usando compartimentos dedicados (como threads separadas). Isso garante que, se um serviço estiver sobrecarregado, os demais continuem funcionando normalmente.


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


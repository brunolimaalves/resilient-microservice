#!/bin/bash

BASE_URL="http://localhost:8080/api"
REPETICOES=100
DELAY=0.2

### Cenário 1: Bulkhead isolado no serviço de pagamento
echo -e "\n=============================="
echo "🚧 TESTE: Bulkhead (Pagamento)"
echo "=============================="
for i in $(seq 1 $REPETICOES); do
  curl -s "$BASE_URL/bulkhead/payment" &
done
wait

### Cenário 2: Rate Limiter isolado no serviço de Pagamento
echo -e "\n=============================="
echo "🚦 TESTE: Rate Limiter (Pagamento)"
echo "=============================="
for i in $(seq 1 $REPETICOES); do
  curl -s "$BASE_URL/ratelimit/payment" &
  sleep $DELAY
done
wait

### Cenário 3: Circuit Breaker isolado no serviço de Pagamento
echo -e "\n=============================="
echo "💥 TEST: Circuit Breaker (Pagamento)"
echo "=============================="
for i in $(seq 1 $REPETICOES); do
  curl -s "$BASE_URL/circuitbreaker/payment" &
done
wait

### Cenário 4: Todos os padrões juntos (payment)
echo -e "\n=============================="
echo "🛡️ TESTE: Todos juntos (payment)"
echo "=============================="
for i in $(seq 1 $REPETICOES); do
  curl -s "$BASE_URL/resiliencia/payment" &
  sleep $DELAY
done
wait

### Cenário 5: Pedido funcionando isolado enquanto pagamento sofre
echo -e "\n=============================="
echo "🔁 TESTE: Pedido paralelo a pagamento (isolamento com Bulkhead)"
echo "=============================="
for i in $(seq 1 $REPETICOES); do
  curl -s -X GET "$BASE_URL/payment" > /dev/null &
  echo -n "[Order $i]: "
  curl -s -X GET "$BASE_URL/order"
  echo ""
done
wait

echo -e "\n✅ Finish."

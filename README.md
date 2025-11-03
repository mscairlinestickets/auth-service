# 🛡️ Estratégia RBAC para o Sistema de Reservas Aéreas 🔐

## 🎯 Objetivo

Este documento descreve a estratégia de Controle de Acesso Baseado em Funções (RBAC) utilizada nos microsserviços do ecossistema de reservas aéreas. O objetivo é garantir um controle de acesso seguro, escalável e simples, baseado em funções de usuário e permissões granulares (escopos).

---

## 🧱 Arquitetura de Microsserviços

| Serviço             | Responsabilidade principal                    | Requer autenticação |
|---------------------|-----------------------------------------------|----------------------|
| `auth-service`      | Emissão de tokens JWT                         | ✅ Sim               |
| `edge-service`      | Gateway + validação de tokens                 | ✅ Sim               |
| `booking-service`   | Listagem de voos e criação de reservas        | ❌ Não (acesso público) |
| `payment-service`   | Consumo de eventos via Kafka                  | ❌ Não (sem endpoints) |

---

## 👤 Funções e Escopos

### Função: `ROLE_AIRLINE`

Esta função é atribuída às companhias aéreas e concede acesso para gerenciar voos e visualizar reservas.

| Escopo (`scope`)        | Descrição da permissão                    |
|-------------------------|-------------------------------------------|
| `flight_create`         | Criar voos ou registros de aeronaves      |
| `flight_update`         | Atualizar informações de voos ou aeronaves|
| `flight_delete`         | Excluir voos ou registros de aeronaves    |
| `booking_read`          | Visualizar reservas associadas aos voos   |

---

## 🔐 Controle de Acesso por Endpoint

### `booking-service`

| Endpoint                          | Método | Requisito de acesso         |
|----------------------------------|--------|------------------------------|
| `/api/flights`                   | GET    | Público                      |
| `/api/bookings`                  | POST   | Público                      |
| `/api/flights`                   | POST   | `SCOPE_FLIGHT_CREATE`        |
| `/api/flights/{id}`              | PUT    | `SCOPE_FLIGHT_UPDATE`        |
| `/api/flights/{id}`              | DELETE | `SCOPE_FLIGHT_DELETE`        |

### `airline` (Gestão de Aeronaves)

| Endpoint                          | Método | Requisito de acesso         |
|----------------------------------|--------|------------------------------|
| `/api/airline`                   | POST   | `SCOPE_FLIGHT_CREATE`        |
| `/api/airline/{id}`              | PUT    | `SCOPE_FLIGHT_UPDATE`        |
| `/api/airline/{id}`              | DELETE | `SCOPE_FLIGHT_DELETE`        |

---

## 🧠 Princípios de Design

- **Função única e clara**: `ROLE_AIRLINE` cobre todas as permissões administrativas.
- **Escopos granulares**: permitem controle fino por ação e endpoint.
- **Acesso público**: visualização de voos e criação de reservas não exige autenticação.
- **Design escalável**: fácil adicionar novas funções ou escopos conforme o sistema evolui.

---

## 📌 Considerações Futuras

- Criar funções como `ROLE_FLEET_MANAGER` ou `ROLE_BOOKING_AGENT` para responsabilidades mais específicas.
- Adicionar escopos para o `payment-service` caso endpoints sejam introduzidos.
- Considerar expor um endpoint JWKS para distribuição dinâmica da chave pública.


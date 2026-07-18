# Delivery Management API

[![Build Status](https://github.com/MarceloJustin/delivery-management-api/actions/workflows/ci.yml/badge.svg)](https://github.com/MarceloJustin/delivery-management-api/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-green)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
![JUnit 5](https://img.shields.io/badge/JUnit-5-red)
![JaCoCo](https://img.shields.io/badge/Coverage-92%25-brightgreen)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-green)
![CI/CD](https://img.shields.io/badge/CI-GitHub_Actions-success)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-Render-46E3B7)](https://delivery-management-api-sgex.onrender.com/swagger-ui/index.html)

API REST para gerenciamento de pedidos de delivery desenvolvida com Java, Spring Boot, PostgreSQL, autenticação JWT, testes automatizados e documentação OpenAPI.

🔗 **API em produção:** [delivery-management-api-sgex.onrender.com](https://delivery-management-api-sgex.onrender.com/swagger-ui/index.html) (veja detalhes e limitações em [☁️ Deploy em Produção](#deploy-em-producao-render))

## 📑 Índice

* [📌 Sobre o Projeto](#sobre-o-projeto)
* [🚀 Tecnologias Utilizadas](#tecnologias-utilizadas)
* [🏗️ Arquitetura](#arquitetura)
* [🔐 Segurança e Autenticação](#seguranca-e-autenticacao)
* [📋 Funcionalidades](#funcionalidades)
* [📦 Regras de Negócio](#regras-de-negocio)
* [🔗 Principais Endpoints](#principais-endpoints)
* [📨 Exemplos de Requisição](#exemplos-de-requisicao)
* [⚠️ Tratamento de Erros](#tratamento-de-erros)
* [🧪 Testes](#testes)
* [📖 Documentação Swagger](#documentacao-swagger)
* [☁️ Deploy em Produção (Render)](#deploy-em-producao-render)
* [▶️ Como Executar o Projeto](#como-executar-o-projeto)
* [🐳 Executando com Docker (Recomendado)](#executando-com-docker-recomendado)
* [🔧 Variáveis de Ambiente](#variaveis-de-ambiente)
* [💻 Executando Localmente](#executando-localmente)
* [🔮 Melhorias Futuras](#melhorias-futuras)
* [👨‍💻 Autor](#autor)
* [📄 Licença](#licenca)

---

<a id="sobre-o-projeto"></a>
## 📌 Sobre o Projeto

A Delivery Management API é uma aplicação REST desenvolvida com Java e Spring Boot para gerenciamento de pedidos de delivery.

O sistema permite o cadastro de clientes, restaurantes e produtos, além da criação e acompanhamento de pedidos, simulando funcionalidades presentes em plataformas de entrega de alimentos.

O projeto foi desenvolvido com foco em boas práticas de arquitetura, segurança com autenticação JWT, tratamento de exceções, documentação da API e testes automatizados.

---

<a id="tecnologias-utilizadas"></a>
## 🚀 Tecnologias Utilizadas

* Java 21
* Spring Boot 4.0.6
* Spring Security
* JWT (JJWT 0.12.6)
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* OpenAPI / Swagger
* JUnit 5
* Mockito
* MockMvc
* JaCoCo
* GitHub Actions (CI/CD)
* Docker
* Docker Compose

---

<a id="arquitetura"></a>
## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller
│
Service
│
Repository
│
Database
```

### Camadas

**Controller**

* Recebe as requisições HTTP.
* Valida os dados de entrada.
* Retorna as respostas da API.

**Service**

* Contém as regras de negócio da aplicação.

**Repository**

* Responsável pela comunicação com o banco de dados através do Spring Data JPA.

**DTO**

* Responsável pela transferência de dados entre as camadas.
* Organizado em `dto/request` e `dto/response`, separando dados de entrada e saída da API.

**Mapper**

* Converte Entities em DTOs de resposta, centralizando essa lógica fora dos Services.

**Exception**

* Centraliza o tratamento de erros da aplicação.

**Security**

* Filtro JWT que intercepta todas as requisições.
* Valida o token e autentica o usuário no contexto de segurança.

### Estrutura do Projeto

<details open>
<summary>Ver estrutura completa de pastas e arquivos</summary>

```text
delivery-management-api/
├── src/
│   ├── main/
│   │   ├── java/com/delivery_management_api/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── RefreshTokenCleanupJob.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── HealthController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── RestaurantController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── CreateCustomerRequest.java
│   │   │   │   │   ├── CreateOrderItemRequest.java
│   │   │   │   │   ├── CreateOrderRequest.java
│   │   │   │   │   ├── CreateProductRequest.java
│   │   │   │   │   ├── CreateRestaurantRequest.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RefreshTokenRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── UpdateCustomerRequest.java
│   │   │   │   │   ├── UpdateOrderItemRequest.java
│   │   │   │   │   ├── UpdateOrderRequest.java
│   │   │   │   │   ├── UpdateOrderStatusRequest.java
│   │   │   │   │   ├── UpdateProductRequest.java
│   │   │   │   │   └── UpdateRestaurantRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── CustomerResponse.java
│   │   │   │       ├── ErrorResponse.java
│   │   │   │       ├── OrderItemResponse.java
│   │   │   │       ├── OrderResponse.java
│   │   │   │       ├── ProductResponse.java
│   │   │   │       ├── RestaurantResponse.java
│   │   │   │       └── ValidationErrorResponse.java
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── RefreshToken.java
│   │   │   │   ├── Restaurant.java
│   │   │   │   └── User.java
│   │   │   ├── enums/
│   │   │   │   ├── OrderStatus.java
│   │   │   │   └── Role.java
│   │   │   ├── exception/
│   │   │   │   ├── CustomerNotFoundException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidOrderStatusException.java
│   │   │   │   ├── InvalidRefreshTokenException.java
│   │   │   │   ├── OrderCancellationNotAllowedException.java
│   │   │   │   ├── OrderNotFoundException.java
│   │   │   │   ├── ProductNotFoundException.java
│   │   │   │   ├── RestaurantNotFoundException.java
│   │   │   │   └── UserAlreadyExistsException.java
│   │   │   ├── mapper/
│   │   │   │   ├── CustomerMapper.java
│   │   │   │   ├── OrderMapper.java
│   │   │   │   ├── ProductMapper.java
│   │   │   │   └── RestaurantMapper.java
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── RefreshTokenRepository.java
│   │   │   │   ├── RestaurantRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtService.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   └── service/
│   │   │       ├── AuthService.java
│   │   │       ├── CustomerService.java
│   │   │       ├── OrderService.java
│   │   │       ├── ProductService.java
│   │   │       ├── RefreshTokenService.java
│   │   │       └── RestaurantService.java
│   │   └── resources/
│   │       ├── application-render.properties
│   │       └── application.properties
│   └── test/
│       ├── java/com/delivery_management_api/
│       │   ├── integration/
│       │   │   ├── AuthControllerIntegrationTest.java
│       │   │   ├── CustomerAuthorizationIntegrationTest.java
│       │   │   ├── CustomerControllerIntegrationTest.java
│       │   │   ├── HealthControllerIntegrationTest.java
│       │   │   ├── OrderAuthorizationIntegrationTest.java
│       │   │   ├── OrderControllerIntegrationTest.java
│       │   │   ├── ProductControllerIntegrationTest.java
│       │   │   └── RestaurantControllerIntegrationTest.java
│       │   └── service/
│       │       ├── AuthServiceTest.java
│       │       ├── OrderServiceTest.java
│       │       ├── ProductServiceTest.java
│       │       └── RefreshTokenServiceTest.java
│       └── resources/
│           └── application-test.properties
├── postman/
│   └── delivery-management-api.postman_collection.json
├── Dockerfile
├── docker-compose.yml
├── render.yaml
├── pom.xml
└── README.md
```

</details>

---

<a id="seguranca-e-autenticacao"></a>
## 🔐 Segurança e Autenticação

A API utiliza autenticação stateless com **JWT (JSON Web Token)** via Spring Security.

### Como funciona

1. O usuário se registra em `POST /api/auth/register` e recebe um access token (JWT) e um refresh token
2. O usuário faz login em `POST /api/auth/login` e recebe um access token (JWT) e um refresh token
3. O access token deve ser enviado no header de todas as requisições protegidas:

```
Authorization: Bearer <token>
```

O access token expira em **24 horas** (configurável via `JWT_EXPIRATION`). Após a expiração, em vez de fazer login novamente, o cliente pode obter um novo access token através do refresh token.

### Refresh Token

Para evitar que o usuário precise fazer login toda vez que o access token expira, a API implementa o fluxo de **Refresh Token**:

1. O cliente envia o refresh token para `POST /api/auth/refresh`
2. A API valida o token (existe, não expirou, não foi revogado) e retorna um **novo access token e um novo refresh token**
3. O refresh token antigo é automaticamente revogado (**rotação de token**) — ele não pode ser reutilizado, mesmo que ainda não tenha expirado

O refresh token expira em **7 dias** (configurável via `REFRESH_TOKEN_EXPIRATION`) e é armazenado no banco de dados, o que permite revogação (diferente do access token, que é stateless). Uma tarefa agendada (`RefreshTokenCleanupJob`) remove diariamente do banco os tokens expirados ou já revogados.

### Roles

O sistema possui dois níveis de acesso:

| Role | Descrição |
| --- | --- |
| `ADMIN` | Acesso total à API |
| `CUSTOMER` | Acesso restrito aos próprios pedidos e ao próprio perfil de cliente |

<a id="autorizacao-por-endpoint"></a>
### Autorização por Endpoint

| Endpoint | Método | ADMIN | CUSTOMER | Público |
| --- | --- | :---: | :---: | :---: |
| `/api/auth/**` | POST | ✅ | ✅ | ✅ |
| `/api/health` | GET | ✅ | ✅ | ✅ |
| `/api/restaurants/**` | GET | ✅ | ✅ | ✅ |
| `/api/products/**` | GET | ✅ | ✅ | ✅ |
| `/api/restaurants/**` | POST / PUT / DELETE | ✅ | ❌ | ❌ |
| `/api/products/**` | POST / PUT / DELETE | ✅ | ❌ | ❌ |
| `/api/customers` | GET (listar todos) | ✅ | ❌ | ❌ |
| `/api/customers` | POST | ✅ | ❌ | ❌ |
| `/api/customers/{id}` | GET / PUT | ✅ (qualquer) | ⚠️ apenas o próprio perfil | ❌ |
| `/api/customers/{id}` | DELETE | ✅ | ❌ | ❌ |
| `/api/orders` | POST | ✅ (em nome de qualquer cliente) | ⚠️ sempre em nome próprio (`customerId` do corpo é ignorado) | ❌ |
| `/api/orders` | GET (listar) | ✅ (todos os pedidos) | ⚠️ apenas os próprios pedidos | ❌ |
| `/api/orders/{id}` | GET | ✅ (qualquer) | ⚠️ apenas o próprio pedido | ❌ |
| `/api/orders/status/{status}` | GET | ✅ (todos) | ⚠️ apenas os próprios | ❌ |
| `/api/orders/{id}/status` | PATCH | ✅ | ❌ | ❌ |
| `/api/orders/{id}/cancel` | PATCH | ✅ (qualquer) | ⚠️ apenas o próprio (e somente em `CREATED`/`CONFIRMED`) | ❌ |

⚠️ = **autorização por propriedade do recurso** (resource ownership): a role `CUSTOMER` dá acesso ao endpoint, mas o `Service` verifica se o recurso (pedido ou perfil) pertence ao usuário autenticado antes de liberar. Uma tentativa de acessar recurso de outro cliente retorna **403 Forbidden**.

Essa verificação depende do vínculo `Customer ↔ User`: todo `CUSTOMER` que se registra em `POST /api/auth/register` ganha automaticamente um perfil de `Customer` vinculado à sua conta.

### Usuário ADMIN padrão

Ao subir a aplicação pela primeira vez, um usuário ADMIN é criado automaticamente com as credenciais definidas no arquivo `.env`:

```env
ADMIN_EMAIL=admin@admin.com
ADMIN_PASSWORD=your_admin_password
```

Nas inicializações seguintes, se o usuário já existir, nenhuma ação é realizada.

---

<a id="funcionalidades"></a>
## 📋 Funcionalidades

### Autenticação

* Registrar usuário
* Fazer login e obter token JWT
* Renovar access token através de refresh token

### Clientes

* Criar cliente
* Listar clientes
* Buscar cliente por ID
* Atualizar cliente
* Remover cliente

### Restaurantes

* Criar restaurante
* Listar restaurantes
* Buscar restaurante por ID
* Buscar restaurantes por nome
* Atualizar restaurante
* Remover restaurante

### Produtos

* Criar produto
* Listar produtos
* Buscar produto por ID
* Atualizar produto
* Remover produto
* Buscar produtos por restaurante

### Pedidos

* Criar pedido
* Listar pedidos
* Buscar pedido por ID
* Buscar pedidos por status
* Atualizar status do pedido
* Cancelar pedido

---

<a id="regras-de-negocio"></a>
## 📦 Regras de Negócio

### Fluxo de Status do Pedido

Os pedidos seguem um fluxo controlado de estados:

```text
CREATED
↓
CONFIRMED
↓
PREPARING
↓
OUT_FOR_DELIVERY
↓
DELIVERED
```

Também existe o status:

```text
CANCELLED
```
utilizado quando um pedido é cancelado em um estágio permitido.

### Transições de Status

A aplicação valida todas as mudanças de status através de regras de negócio implementadas na camada de serviço.

Transições permitidas:

```text
CREATED → CONFIRMED

CONFIRMED → PREPARING

PREPARING → OUT_FOR_DELIVERY

OUT_FOR_DELIVERY → DELIVERED
```

Transições inválidas são bloqueadas e retornam erro de negócio.

Exemplos de transições não permitidas:

```text
CREATED → DELIVERED

CREATED → OUT_FOR_DELIVERY

CONFIRMED → DELIVERED

DELIVERED → CREATED
```

### Cancelamento de Pedidos

Um pedido pode ser cancelado apenas enquanto estiver nos status:

```text
CREATED
CONFIRMED
```

Pedidos nos status:

```text
PREPARING
OUT_FOR_DELIVERY
DELIVERED
```

não podem ser cancelados.

Tentativas de cancelamento inválidas retornam erro de negócio.

### Validações

* Nome do cliente é obrigatório.
* E-mail deve possuir formato válido.
* Taxa de entrega não pode ser negativa.
* Pedidos devem possuir pelo menos um item.
* Produtos devem estar associados a um restaurante válido.
* Transições inválidas de status são bloqueadas.

---

<a id="principais-endpoints"></a>
## 🔗 Principais Endpoints

### Autenticação

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | /api/auth/register | Registrar novo usuário |
| POST | /api/auth/login | Fazer login e obter token JWT |
| POST | /api/auth/refresh | Renovar access token através de refresh token |

### Clientes

| Método | Endpoint            |
| ------ | ------------------- |
| POST   | /api/customers      |
| GET    | /api/customers      |
| GET    | /api/customers/{id} |
| PUT    | /api/customers/{id} |
| DELETE | /api/customers/{id} |

### Restaurantes

| Método | Endpoint                       |
| ------ | ------------------------------ |
| POST   | /api/restaurants               |
| GET    | /api/restaurants               |
| GET    | /api/restaurants/{id}          |
| GET    | /api/restaurants/search/{name} |
| PUT    | /api/restaurants/{id}          |
| DELETE | /api/restaurants/{id}          |

### Produtos

| Método | Endpoint                                |
| ------ | --------------------------------------- |
| POST   | /api/products                           |
| GET    | /api/products                           |
| GET    | /api/products/{id}                      |
| GET    | /api/products/restaurant/{restaurantId} |
| GET    | /api/products/price/{price}             |
| GET    | /api/products/price-range               |
| PUT    | /api/products/{id}                      |
| DELETE | /api/products/{id}                      |

### Pedidos

| Método | Endpoint                    |
| ------ | --------------------------- |
| POST   | /api/orders                 |
| GET    | /api/orders                 |
| GET    | /api/orders/{id}            |
| GET    | /api/orders/status/{status} |
| PATCH  | /api/orders/{id}/status     |
| PATCH  | /api/orders/{id}/cancel     |

---

<a id="exemplos-de-requisicao"></a>
## 📨 Exemplos de Requisição

### Registrar Usuário

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "type": "Bearer",
  "refreshToken": "b6f1c9e2-3a4d-4e2b-9c1a-5f6e7d8c9b0a",
  "name": "João Silva",
  "email": "joao@email.com",
  "role": "CUSTOMER"
}
```

### Fazer Login

```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "type": "Bearer",
  "refreshToken": "b6f1c9e2-3a4d-4e2b-9c1a-5f6e7d8c9b0a",
  "name": "João Silva",
  "email": "joao@email.com",
  "role": "CUSTOMER"
}
```

### Renovar Access Token

```json
{
  "refreshToken": "b6f1c9e2-3a4d-4e2b-9c1a-5f6e7d8c9b0a"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "type": "Bearer",
  "refreshToken": "c7a2d0f3-4b5e-4f3c-8d2b-6a7f8e9d0c1b",
  "name": "João Silva",
  "email": "joao@email.com",
  "role": "CUSTOMER"
}
```

> O refresh token retornado é **novo** — o token antigo é revogado automaticamente (rotação) e não pode ser reutilizado.

### Criar Cliente

```json
{
  "name": "Marcelo",
  "email": "marcelo@email.com"
}
```

### Criar Restaurante

```json
{
  "name": "Burger King",
  "category": "Fast Food",
  "deliveryFee": 9.90
}
```

### Criar Produto

```json
{
  "name": "Whopper",
  "price": 39.90,
  "restaurantId": 1
}
```

### Criar Pedido

```json
{
  "customerId": 1,
  "restaurantId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### Atualizar Status do Pedido

```json
{
  "status": "CONFIRMED"
}
```

---

<a id="tratamento-de-erros"></a>
## ⚠️ Tratamento de Erros

A API utiliza tratamento global de exceções através de um GlobalExceptionHandler.

| Status | Descrição |
| --- | --- |
| 400 | Dados inválidos na requisição |
| 401 | Não autenticado — token ausente ou inválido |
| 403 | Sem permissão — role insuficiente |
| 404 | Recurso não encontrado |
| 409 | Conflito — e-mail já cadastrado ou transição de status inválida |

Exemplo de resposta:

```json
{
  "timestamp": "2026-06-12T18:00:00",
  "status": 404,
  "error": "Customer not found"
}
```

---

<a id="testes"></a>
## 🧪 Testes

### Cobertura de Testes

O projeto utiliza JaCoCo para análise de cobertura de código.

Resultados atuais:

- Cobertura total: 92%
- Cobertura de branches da camada Service: 100%
- Fluxo completo de status de pedidos testado
- Testes de exceções e regras de negócio implementados
- Testes de autenticação e autorização JWT implementados

A cobertura inclui:

- Testes unitários
- Testes de integração
- Validação de transições de status
- Tratamento de exceções
- Regras de cancelamento de pedidos

### Testes Unitários

* ProductServiceTest
* OrderServiceTest
* AuthServiceTest
* RefreshTokenServiceTest

### Testes de Integração

* HealthControllerIntegrationTest
* CustomerControllerIntegrationTest
* RestaurantControllerIntegrationTest
* ProductControllerIntegrationTest
* OrderControllerIntegrationTest
* AuthControllerIntegrationTest

Os testes utilizam:

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc
* Spring Security Test

Executar todos os testes:

```bash
.\mvnw clean test
```

---

### Integração Contínua (CI)

O projeto utiliza GitHub Actions para execução automática dos testes a cada push ou pull request na branch principal.

A pipeline realiza:

* Checkout do código
* Configuração do Java 21
* Execução dos testes automatizados
* Validação da integridade da aplicação através da execução dos testes automatizados e geração dos relatórios de cobertura.

Status atual:

[![Build Status](https://github.com/MarceloJustin/delivery-management-api/actions/workflows/ci.yml/badge.svg)](https://github.com/MarceloJustin/delivery-management-api/actions/workflows/ci.yml)

<a id="documentacao-swagger"></a>
## 📖 Documentação Swagger

A documentação interativa da API está disponível após iniciar a aplicação:

```text
http://localhost:8080/swagger-ui/index.html
```

<a id="como-autenticar-no-swagger"></a>
### Como autenticar no Swagger

1. Inicie a aplicação
2. Acesse o Swagger UI
3. Chame `POST /api/auth/login` com as credenciais do ADMIN
4. Copie o valor do campo `token` da resposta
5. Clique no botão **Authorize** (cadeado) no topo da página
6. Cole o token no formato: `Bearer <token>`
7. Confirme clicando em **Authorize**

A partir desse momento todos os endpoints protegidos estarão disponíveis.

### Visão Geral da API

![Visão Geral do Swagger](images/swagger-overview.png)

### Exemplo de Endpoint

Abaixo é possível visualizar a documentação do endpoint de atualização de status de pedidos.

![Documentação de Pedidos](images/swagger-orders.png)

### Collection do Postman

Além do Swagger, a API também pode ser testada via [Postman](https://www.postman.com/), usando a collection disponível em [`postman/delivery-management-api.postman_collection.json`](postman/delivery-management-api.postman_collection.json):

1. No Postman, clique em **Import** e selecione o arquivo acima.
2. A collection já vem com a variável `baseUrl` apontando para a API em produção (Render). Para testar localmente, edite essa variável para `http://localhost:8080`.
3. Chame `POST /api/auth/login` com as credenciais do ADMIN e copie o `token` da resposta.
4. Salve o token na variável de collection `bearerToken` — os endpoints protegidos já usam essa variável automaticamente (autenticação Bearer configurada por request).

---

<a id="deploy-em-producao-render"></a>
## ☁️ Deploy em Produção (Render)

A API está publicada em produção no [Render](https://render.com), usando o Blueprint declarado em [`render.yaml`](render.yaml): um Web Service (Docker, a partir do mesmo `Dockerfile` usado localmente) e um banco PostgreSQL gerenciado, provisionados juntos e conectados automaticamente via variáveis de ambiente.

* **API / Swagger:** [delivery-management-api-sgex.onrender.com/swagger-ui/index.html](https://delivery-management-api-sgex.onrender.com/swagger-ui/index.html)
* **Health check:** [delivery-management-api-sgex.onrender.com/api/health](https://delivery-management-api-sgex.onrender.com/api/health)

> Acessar a raiz do domínio (`/`) diretamente retorna **401**, propositalmente — não existe nenhum endpoint mapeado em `/`, e a regra padrão de segurança (`anyRequest().authenticated()`) exige autenticação para qualquer rota não listada explicitamente como pública. Use sempre um caminho da API (`/api/...`) ou o Swagger.

### Como testar agora

> ⏱️ **No plano free do Render, a primeira requisição após ~15 minutos de inatividade pode levar de 30 a 60 segundos para responder (cold start).** Se a página parecer travada no primeiro acesso, é isso — não é um erro. Detalhes em [Limitações do plano Free do Render](#limitacoes-do-plano-free-do-render).

1. Abra o [Swagger](https://delivery-management-api-sgex.onrender.com/swagger-ui/index.html) ou importe a [collection do Postman](postman/delivery-management-api.postman_collection.json).
2. Sem precisar de login, já dá pra ver o catálogo público: `GET /api/restaurants` e `GET /api/products`.
3. Para testar endpoints protegidos (pedidos, clientes), registre-se como `CUSTOMER` em `POST /api/auth/register` — é público, qualquer pessoa pode criar uma conta de teste.
4. Copie o `token` retornado e siga [Como autenticar no Swagger](#como-autenticar-no-swagger) para autorizar as próximas requisições (o mesmo token serve para a variável `bearerToken` da collection do Postman).

### O que fica público em produção

Como a aplicação está exposta na internet, é importante deixar claro **o que qualquer pessoa consegue acessar sem token** e o que continua protegido — as mesmas regras da seção [Autorização por Endpoint](#autorizacao-por-endpoint) valem em produção, sem exceção:

* **Público, sem token:** `/api/health`, `/swagger-ui/**`, `/api/auth/register`, `/api/auth/login` (é assim que alguém vira usuário) e a leitura (`GET`) de `/api/restaurants` e `/api/products` — um catálogo público, como em qualquer app de delivery real.
* **Nunca público:** `/api/customers` e `/api/orders`. Diferente do que se poderia supor, o cadastro de clientes é o recurso **mais** protegido, não o mais aberto — listar/criar clientes exige role `ADMIN`, e buscar/editar um cliente específico exige ser o próprio `ADMIN` ou o `CUSTOMER` dono daquele perfil.

Qualquer pessoa pode se registrar como `CUSTOMER` (endpoint público, por desenho), mas a autorização por propriedade de recurso (v1.5) garante que cada `CUSTOMER` só enxerga e altera o próprio perfil e os próprios pedidos — nunca dados de outro cliente. As credenciais do usuário `ADMIN` padrão não ficam no repositório: são definidas manualmente no dashboard do Render (`ADMIN_EMAIL` / `ADMIN_PASSWORD` com `sync: false` no `render.yaml`).

<a id="limitacoes-do-plano-free-do-render"></a>
### Limitações do plano Free do Render

* O Web Service **dorme após ~15 minutos de inatividade**. A primeira requisição depois disso pode levar de 30 a 60 segundos para responder (cold start) — se parecer que a API "não está funcionando", tente novamente após esse tempo antes de investigar outra causa.
* O banco PostgreSQL gerenciado no plano free **expira automaticamente 30 dias após a criação**.

---

<a id="como-executar-o-projeto"></a>
## ▶️ Como Executar o Projeto

### 1. Clonar o projeto

```bash
git clone https://github.com/MarceloJustin/delivery-management-api.git

cd delivery-management-api
```

<a id="executando-com-docker-recomendado"></a>
## 🐳 Executando com Docker (Recomendado)

O projeto possui suporte completo a Docker e Docker Compose, permitindo executar a API e o banco PostgreSQL com apenas um comando.

### Serviços

* delivery-api (Spring Boot)
* postgres-db (PostgreSQL 17)

### Subir a aplicação

Antes de iniciar os containers, certifique-se de que o arquivo `.env` foi criado e configurado.

```bash
docker compose up --build
```

### Executar em segundo plano

```bash
docker compose up -d
```

### Parar os containers

```bash
docker compose down
```

A API ficará disponível em:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

> **Observação**
>
> O projeto utiliza variáveis de ambiente para evitar que credenciais sejam armazenadas no repositório.
> O arquivo `.env` é ignorado pelo Git e o arquivo `.env.example` serve como modelo de configuração.


<a id="variaveis-de-ambiente"></a>
## 🔧 Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para configurar banco de dados, JWT e o usuário administrador padrão.

### 1. Crie um arquivo `.env`

Linux / macOS

```bash
cp .env.example .env
```

Windows

```text
Copie o arquivo .env.example e renomeie para .env.
```

### 2. Configure as variáveis

```env
# PostgreSQL
POSTGRES_DB=delivery_management_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password

# Spring Boot
DB_URL=jdbc:postgresql://postgres:5432/delivery_management_db
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key_must_be_at_least_32_characters_long
JWT_EXPIRATION=86400000

# Refresh Token
REFRESH_TOKEN_EXPIRATION=604800000
REFRESH_TOKEN_CLEANUP_CRON=0 0 3 * * *

# Admin padrão (criado automaticamente na primeira inicialização)
ADMIN_NAME=Admin
ADMIN_EMAIL=admin@admin.com
ADMIN_PASSWORD=your_admin_password
```

### Descrição das variáveis

| Variável | Descrição | Obrigatória |
| --- | --- | :---: |
| `POSTGRES_DB` | Nome do banco de dados | ✅ |
| `POSTGRES_USER` | Usuário do PostgreSQL | ✅ |
| `POSTGRES_PASSWORD` | Senha do PostgreSQL | ✅ |
| `DB_URL` | URL de conexão JDBC | ✅ |
| `DB_USERNAME` | Usuário da aplicação | ✅ |
| `DB_PASSWORD` | Senha da aplicação | ✅ |
| `JWT_SECRET` | Chave secreta para assinar os tokens JWT | ✅ |
| `JWT_EXPIRATION` | Tempo de expiração do token em ms (padrão: 86400000 = 24h) | ❌ |
| `REFRESH_TOKEN_EXPIRATION` | Tempo de expiração do refresh token em ms (padrão: 604800000 = 7 dias) | ❌ |
| `REFRESH_TOKEN_CLEANUP_CRON` | Expressão cron da limpeza de refresh tokens expirados/revogados (padrão: `0 0 3 * * *`, todo dia às 3h) | ❌ |
| `ADMIN_NAME` | Nome do usuário administrador padrão | ❌ |
| `ADMIN_EMAIL` | E-mail do administrador padrão | ❌ |
| `ADMIN_PASSWORD` | Senha do administrador padrão | ✅ |

> O arquivo `.env` não é versionado e deve permanecer apenas no ambiente local.

<a id="executando-localmente"></a>
## 💻 Executando Localmente

### Pré-requisitos

* Java 21
* Maven
* PostgreSQL

### 1. Criar o banco de dados

```sql
CREATE DATABASE delivery_management_db;
```

### 2. Configure o arquivo `.env`

Copie o arquivo `.env.example` para `.env` e configure as credenciais do PostgreSQL de acordo com o seu ambiente.

### 3. Executar a aplicação

```bash
# Linux / macOS
./mvnw spring-boot:run
```

```bash
# Windows
mvnw.cmd spring-boot:run
```

---

<a id="melhorias-futuras"></a>
## 🔮 Melhorias Futuras

* Versionamento de banco com Flyway
* Versionamento da API
* Fluxo de pagamento dos pedidos

---

<a id="autor"></a>
## 👨‍💻 Autor

Desenvolvido por **Marcelo da Silva Justin** como projeto de estudo e prática de desenvolvimento backend utilizando Java e Spring Boot.

GitHub: [MarceloJustin](https://github.com/MarceloJustin)

LinkedIn: [marcelojustin](https://linkedin.com/in/marcelojustin)

---

<a id="licenca"></a>
## 📄 Licença

Este projeto está licenciado sob a licença MIT.

Consulte o arquivo [LICENSE](LICENSE) para mais informações.
# 🚀 Via Appia --- Incident Management API

API de gerenciamento de incidentes desenvolvida como parte de um desafio
técnico.\
Implementada em **Java 17 + Spring Boot 3**, com autenticação **JWT**,
cache **Caffeine**, documentação **OpenAPI/Swagger**, banco
**PostgreSQL**, e totalmente **dockerizada**.

------------------------------------------------------------------------

## 📚 Índice

1.  Descrição
2.  Tecnologias
3.  Arquitetura do Projeto
4.  Execução com Docker (recomendado)
5.  Execução local (sem Docker)
6.  Credenciais e fluxo de login
7.  Variáveis de ambiente
8.  Banco de Dados
9.  Documentação OpenAPI
10. Principais Endpoints
11. Reset do banco
12. Troubleshooting

------------------------------------------------------------------------

## 📝 Descrição

O sistema permite:

-   Criar, consultar, editar e remover **incidentes**
-   Alterar status de incidentes (PATCH)
-   Listar e criar **comentários** relacionados a incidentes
-   Autenticação JWT com roles `READ` e `WRITE`
-   Paginação, filtros e busca textual
-   Cache com Caffeine para otimização de leitura

------------------------------------------------------------------------

## 🛠 Tecnologias

### 🔹 Backend

-   Java 17\
-   Spring Boot 3\
-   Spring Web\
-   Spring Data JPA\
-   Spring Security (JWT)\
-   Spring Validation\
-   Spring Cache (Caffeine)\
-   Springdoc OpenAPI\
-   Flyway (migrations)

### 🔹 Frontend

- Angular 17
- Angular Material
- Standalone Components
- JWT Interceptor
- Guards para rotas protegidas
- Docker + NGINX para build/produção

### 🔹 Infra

-   PostgreSQL 16\
-   Docker & Docker Compose\
-   Maven 3+

------------------------------------------------------------------------

## 🏗 Arquitetura do Projeto 
### 📌 Backend — Spring Boot (Java)

    src/main/java/com/appia/incidents
    │
    ├── config/           # CORS, security, OpenAPI, cache
    ├── controller/       # Endpoints REST
    ├── dto/              # Request/response DTOs
    ├── entity/           # Entidades JPA
    ├── exception/        # Handler global de exceções
    ├── mapper/           # Conversão DTO <-> Entity
    ├── repository/       # Repositórios JPA
    ├── security/         # JWT, filtros, roles
    ├── service/          # Regras de negócios + cache
    └── spec/             # Specifications (filtros dinâmicos)
### 🎨 Frontend — Angular
``` bash
frontend/
│
├── src/
│   ├── app/
│   │   ├── auth/                     # Login, guarda de rotas, interceptor JWT
│   │   │   ├── login/
│   │   │   ├── auth.guard.ts
│   │   │   └── auth.interceptor.ts
│   │   │
│   │   ├── core/                     # Infra base
│   │   │   ├── api-client.service.ts # Cliente HTTP genérico
│   │   │   ├── auth.service.ts       # Controle de token
│   │   │   └── models/               # Models: Incident, Comment, Page<T>, etc.
│   │   │
│   │   ├── incidents/                # Funcionalidades de incidentes
│   │   │   ├── incident-list/        # Lista + filtros + paginação
│   │   │   ├── incident-detail/      # Visualização + comentários
│   │   │   └── incident-form/        # Criar e editar (formulário único)
│   │   │
│   │   ├── services/                 # Serviços específicos de domínio
│   │   │   ├── incident.service.ts
│   │   │   └── comment.service.ts
│   │   │
│   │   ├── shared/                   # Utilitários reaproveitáveis
│   │   │   ├── pipes/
│   │   │   │   └── app-date.pipe.ts  # Formatação consistente de datas
│   │   │   └── utils/
│   │   │       ├── normalize-incident.ts  # Normalização de formulários (DRY)
│   │   │       └── query-utils.ts         # Builder de query params (DRY)
│   │   │
│   │   └── app.routes.ts             # Configuração de rotas
│   │
│   ├── environments/                 # apiBaseUrl e configs por ambiente
│   └── main.ts                       # Bootstrap da aplicação Angular
│
├── Dockerfile                        # Build de produção (Nginx)
└── angular.json                      # Configuração do workspace Angular

```

------------------------------------------------------------------------

## 🐳 Execução com Docker (recomendado)

### 1️⃣ Clonar o repositório

``` bash
git clone https://github.com/NotMarcos/via-appia-incidents
cd via-appia-incidents
```

### 2️⃣ Subir com docker-compose

``` bash
docker-compose up --build
```

Isso irá subir:

| Serviço       | Porta |
|---------------|-------|
| API Backend   | 8080  |
| Frontend Angular | 4200 |
| PostgreSQL    | 5432  |

### 3️⃣ Acessar a API e o Frontend

-   Swagger UI:\
    👉 http://localhost:8080/swagger-ui/index.html\
-   OpenAPI JSON:\
    👉 http://localhost:8080/v3/api-docs

- Aplicação Web (Angular):  
  👉 http://localhost:4200

### 4️⃣ Parar containers

``` bash
docker-compose down
```

------------------------------------------------------------------------

## ▶ Execução local (sem Docker)

### **Pré-requisitos**


- Java 17
- Maven 3+
- PostgreSQL rodando localmente
- Node 18+
- Angular CLI instalado globalmente (`npm install -g @angular/cli`)

### 1️⃣ Configurar variáveis no `application.properties` ou ambiente

(seção abaixo)

### 2️⃣ Rodar o Backend

``` bash
mvn spring-boot:run
```
### 2️⃣ Rodar o Frontend

``` bash
cd incidents-frontend
npm install
npm start
```



------------------------------------------------------------------------

## 🔐 Credenciais e fluxo de login

### **Usuários padrão (criadas via Flyway e utilizadas também no frontend)**

| Usuário | Senha    | Papel |
| ------- | -------- | ----- |
| admin   | admin123 | WRITE |
| viewer  | admin123 | READ  |

------------------------------------------------------------------------

### 1️⃣ Login

``` http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Resposta:

``` json
{
  "token": "JWT_TOKEN_AQUI"
}
```

### 2️⃣ Usar o token

``` http
Authorization: Bearer JWT_TOKEN_AQUI
```

------------------------------------------------------------------------

## ⚙ Variáveis de ambiente

    DB_HOST=postgres
    DB_PORT=5432
    DB_NAME=incidents_db
    DB_USER=incidents_user
    DB_PASS=incidents_pass

    JWT_SECRET=MEU_SEGREDO_123
    JWT_EXPIRATION=86400000

------------------------------------------------------------------------

## 🗄 Banco de Dados

A aplicação cria tudo automaticamente via Flyway:
- Tabelas
- Índices
- Inserts iniciais (usuários admin/viewer)

## Configuração (Docker)

| Campo | Valor          |
|-------|----------------|
| Host  | localhost      |
| Porta | 5432           |
| Banco | incidents_db   |
| User  | incidents_user |
| Pass  | incidents_pass |

------------------------------------------------------------------------

## 📘 Documentação OpenAPI

Após subir os containers:

-   Swagger UI: http://localhost:8080/swagger-ui/index.html\
-   OpenAPI JSON: http://localhost:8080/v3/api-docs

------------------------------------------------------------------------

## 🔧 Principais Endpoints

### 🔐 Autenticação
| Método | Endpoint     | Descrição |
|--------|--------------|-----------|
| POST   | /auth/login  | Gera JWT  |

### 🟦 Incidentes
| Método | Endpoint                 | Descrição          |
|--------|---------------------------|---------------------|
| GET    | /incidents               | Listar com filtros |
| POST   | /incidents               | Criar              |
| GET    | /incidents/{id}          | Buscar por ID      |
| PUT    | /incidents/{id}          | Atualizar          |
| PATCH  | /incidents/{id}/status   | Alterar status     |
| DELETE | /incidents/{id}          | Excluir            |

### 🟨 Comentários
| Método | Endpoint                         | Descrição         |
|--------|-----------------------------------|--------------------|
| GET    | /incidents/{id}/comments          | Listar comentários |
| POST   | /incidents/{id}/comments          | Criar comentário   |

------------------------------------------------------------------------

## 🔄 Reset do Banco

Para reset completo:
``` bash
docker-compose down -v
```

------------------------------------------------------------------------

## ❗ Troubleshooting

### 🔸 1. API não sobe (porta em uso)
```bash
sudo lsof -i :8080
kill -9 <PID>
```
### 🔸 2. Postgres não sobe
```bash
sudo lsof -i :5432
```
### 🔸 3. Swagger mostra 401
Gerar novo token JWT em /auth/login.
### 🔸4. Erro de CORS no frontend

O backend tem CORS liberado em CorsConfig.
Se necessário, adicione a origin do frontend.
------------------------------------------------------------------------
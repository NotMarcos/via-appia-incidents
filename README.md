# Via Appia Test

## Descrição

Projeto de teste para gerenciamento de incidentes, desenvolvido em **Java 17 + Spring Boot 3**, com banco **PostgreSQL**, autenticação JWT e documentação **OpenAPI/Swagger**.

---

## Tecnologias

- Java 17
- Spring Boot 3
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Spring Validation
    - Spring Cache
    - Springdoc OpenAPI
- PostgreSQL 16
- Flyway (para migrations)
- Docker & Docker Compose
- JWT para autenticação
- BCrypt para senha (opcional)

---

## Requisitos Técnicos

- Java 17+
- Spring Boot 3+
- Banco PostgreSQL
- Endpoints protegidos via JWT
- Paginação com Pageable/Page<T>
- Padronização de erros: 400/401/403/404/409/422/500
- OpenAPI/Swagger disponível em:
    - `/swagger-ui/index.html`
    - `/v3/api-docs`

---

## Executando o Projeto

### 1. Clonar repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd via-appia-test
```

### 2. Suba os containers:

```bash
docker-compose up --build
```

Isso irá:

- Subir o banco PostgreSQL (incidents_db)
- Subir o backend Spring Boot (incidents_api)
- Verifique se o backend está rodando:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- JSON OpenAPI: http://localhost:8080/v3/api-docs

### 🔑 Credenciais de teste

| Usuário | Senha     | Papel |
|---------|-----------|-------|
| admin   | admin123  | WRITE |
| viewer  | admin123  | READ  |

### 🛠 Fluxo de autenticação
1. Faça o ligin para obter o token JWT:
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```
Resposta:
```bash
{
  "token": "<JWT_TOKEN_AQUI>"
}
```
2. Use o token para acessar endpoints autenticados:
```bash
GET /incidents
Authorization: Bearer <JWT_TOKEN_AQUI>
```
3. Para criar um incidente (usuário com papel WRITE):
```bash
POST /incidents
Authorization: Bearer <JWT_TOKEN_AQUI>
Content-Type: application/json

{
  "titulo": "Incidente teste",
  "descricao": "Descrição do incidente",
  "prioridade": "ALTA",
  "status": "OPEN",
  "responsavelEmail": "admin@app.com",
  "tags": "teste"
}
```
### 📦Banco de dados
- Host: localhost (quando rodando com Docker)
- Porta: 5432
- Banco: incidents_db
- Usuário: incidents_user
- Senha: incidents_pass

O banco é populado automaticamente pelo Flyway no primeiro build.

### 🧩 Endpoints principais
| Método | Endpoint              | Descrição                   |
|--------|----------------------|-----------------------------|
| POST   | /auth/login           | Login e obtenção do JWT     |
| GET    | /incidents            | Listar incidentes           |
| POST   | /incidents            | Criar novo incidente        |
| PUT    | /incidents/{id}       | Atualizar incidente         |
| DELETE | /incidents/{id}       | Remover incidente           |

### ⚠️ Observações

- O token JWT expira conforme configuração em application.properties (jwt.expiration-ms)
- Usuários com papel READ não podem criar ou atualizar incidentes
- Erros padronizados: 400, 401, 403, 404, 409, 422, 500
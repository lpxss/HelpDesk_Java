# Help Desk API

API REST desenvolvida em Java com Spring Boot para gerenciamento de tickets de suporte técnico (Help Desk).

## 📋 Índice

- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração](#configuração)
- [Endpoints](#endpoints)
- [Autenticação](#autenticação)
- [Regras de Negócio](#regras-de-negócio)
- [Exemplos de Uso](#exemplos-de-uso)

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Web** - Para criação de APIs REST
- **Spring Data JPA** - Para acesso a dados
- **Spring Security** - Para segurança e autenticação
- **JWT (JSON Web Token)** - Para autenticação stateless
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **Maven** - Gerenciador de dependências
- **Bean Validation** - Para validação de dados

## ✨ Funcionalidades

### Perfis de Usuário

- **USUARIO**: Pode criar e visualizar apenas seus próprios tickets
- **SUPORTE**: Pode visualizar e atualizar tickets atribuídos a ele
- **ADMIN**: Pode visualizar todos os tickets e gerenciar o sistema

### Tickets (Chamados)

- Criar tickets de suporte
- Listar tickets (conforme perfil)
- Visualizar detalhes de um ticket
- Atualizar status dos tickets
- Adicionar comentários aos tickets

### Autenticação

- Registro de novos usuários
- Login com JWT
- Proteção de endpoints por autenticação

## 📁 Estrutura do Projeto

```
src/main/java/com/helpdesk/
├── controller/          # Controllers REST
│   ├── AuthController.java
│   ├── TicketController.java
│   └── CommentController.java
├── service/            # Lógica de negócio
│   ├── AuthService.java
│   ├── UserService.java
│   ├── TicketService.java
│   └── CommentService.java
├── repository/         # Repositories JPA
│   ├── UserRepository.java
│   ├── TicketRepository.java
│   └── CommentRepository.java
├── entity/            # Entidades JPA
│   ├── User.java
│   ├── Ticket.java
│   ├── Comment.java
│   └── enums/
│       ├── Perfil.java
│       ├── Status.java
│       ├── Prioridade.java
│       └── Categoria.java
├── dto/               # Data Transfer Objects
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── TicketRequest.java
│   │   ├── StatusUpdateRequest.java
│   │   └── CommentRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── UserResponse.java
│       ├── TicketResponse.java
│       └── CommentResponse.java
├── security/          # Configuração de segurança
│   ├── SecurityConfig.java
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── SecurityUtil.java
├── exception/         # Tratamento de exceções
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   └── UnauthorizedException.java
└── HelpDeskApplication.java
```

## ⚙️ Configuração

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

### Executando a Aplicação

1. Clone o repositório
2. Navegue até o diretório do projeto
3. Execute o comando:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Banco de Dados H2

O banco de dados H2 está configurado para execução em memória. Para acessar o console do H2:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:helpdeskdb`
- Username: `sa`
- Password: (vazio)

## 📡 Endpoints

### Autenticação

#### POST /auth/register
Registra um novo usuário no sistema.

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "senha": "senha123456",
  "nome": "João Silva"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "usuario": {
    "id": 1,
    "email": "usuario@example.com",
    "nome": "João Silva",
    "perfil": "USUARIO",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

#### POST /auth/login
Autentica um usuário e retorna um token JWT.

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "senha": "senha123456"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "usuario": {
    "id": 1,
    "email": "usuario@example.com",
    "nome": "João Silva",
    "perfil": "USUARIO",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### Tickets

#### POST /tickets
Cria um novo ticket (requer autenticação).

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "titulo": "Problema com impressora",
  "descricao": "A impressora não está imprimindo corretamente",
  "categoria": "HARDWARE",
  "prioridade": "ALTA"
}
```

**Categorias:** HARDWARE, SOFTWARE, REDE, OUTROS
**Prioridades:** BAIXA, MEDIA, ALTA

**Response (201 Created):**
```json
{
  "id": 1,
  "titulo": "Problema com impressora",
  "descricao": "A impressora não está imprimindo corretamente",
  "categoria": "HARDWARE",
  "prioridade": "ALTA",
  "status": "ABERTO",
  "usuario": {
    "id": 1,
    "email": "usuario@example.com",
    "nome": "João Silva",
    "perfil": "USUARIO"
  },
  "atendente": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### GET /tickets
Lista todos os tickets (conforme perfil do usuário).

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Problema com impressora",
    "descricao": "A impressora não está imprimindo corretamente",
    "categoria": "HARDWARE",
    "prioridade": "ALTA",
    "status": "ABERTO",
    "usuario": {...},
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### GET /tickets/{id}
Busca um ticket por ID (requer autenticação e permissão).

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Problema com impressora",
  "descricao": "A impressora não está imprimindo corretamente",
  "categoria": "HARDWARE",
  "prioridade": "ALTA",
  "status": "ABERTO",
  "usuario": {...},
  "atendente": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### PUT /tickets/{id}/status
Atualiza o status de um ticket (apenas SUPORTE ou ADMIN).

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "status": "EM_ATENDIMENTO"
}
```

**Status disponíveis:** ABERTO, EM_ATENDIMENTO, RESOLVIDO, FECHADO

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Problema com impressora",
  "descricao": "A impressora não está imprimindo corretamente",
  "categoria": "HARDWARE",
  "prioridade": "ALTA",
  "status": "EM_ATENDIMENTO",
  "usuario": {...},
  "atendente": {...},
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:30:00"
}
```

### Comentários

#### POST /tickets/{ticketId}/comments
Adiciona um comentário a um ticket (requer autenticação).

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "texto": "Verificando o problema..."
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "texto": "Verificando o problema...",
  "usuario": {
    "id": 2,
    "email": "suporte@example.com",
    "nome": "Maria Santos",
    "perfil": "SUPORTE"
  },
  "ticketId": 1,
  "createdAt": "2024-01-01T11:00:00"
}
```

#### GET /tickets/{ticketId}/comments
Lista todos os comentários de um ticket (requer autenticação).

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "texto": "Verificando o problema...",
    "usuario": {...},
    "ticketId": 1,
    "createdAt": "2024-01-01T11:00:00"
  }
]
```

## 🔐 Autenticação

A API utiliza autenticação via JWT (JSON Web Token). Para acessar endpoints protegidos, é necessário:

1. Fazer login ou registro para obter um token
2. Incluir o token no header `Authorization` no formato: `Bearer {token}`
3. O token expira em 24 horas (configurável em `application.properties`)

### Exemplo de Uso do Token

```bash
curl -X GET http://localhost:8080/tickets \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## 📜 Regras de Negócio

### Usuários

- Email deve ser único
- Senha deve ter no mínimo 8 caracteres
- Usuário comum só pode visualizar seus próprios tickets
- SUPORTE pode visualizar e atualizar tickets atribuídos a ele
- ADMIN pode visualizar todos os tickets

### Tickets

- Campos obrigatórios: título, descrição, categoria, prioridade
- Ticket sempre inicia com status `ABERTO`
- Usuário não pode fechar um ticket sem passar por `RESOLVIDO`
- Apenas SUPORTE ou ADMIN pode mudar status
- Ticket fechado não pode ser reaberto
- Ticket não pode ser fechado sem estar `RESOLVIDO` primeiro

### Comentários

- Usuário e suporte podem adicionar comentários
- Comentário não pode ser vazio
- Comentários ficam vinculados a um ticket

## 📝 Exemplos de Uso

### 1. Registrar um novo usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123456",
    "nome": "João Silva"
  }'
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123456"
  }'
```

### 3. Criar um ticket

```bash
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "titulo": "Problema com conexão",
    "descricao": "Não consigo me conectar à rede WiFi",
    "categoria": "REDE",
    "prioridade": "MEDIA"
  }'
```

### 4. Listar tickets

```bash
curl -X GET http://localhost:8080/tickets \
  -H "Authorization: Bearer {token}"
```

### 5. Atualizar status do ticket

```bash
curl -X PUT http://localhost:8080/tickets/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "status": "RESOLVIDO"
  }'
```

### 6. Adicionar comentário

```bash
curl -X POST http://localhost:8080/tickets/1/comments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "texto": "Problema resolvido. Teste novamente."
  }'
```

## 🧪 Testes

Para executar os testes (quando implementados):

```bash
mvn test
```

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - Para decodificar e verificar tokens JWT

## 👨‍💻 Desenvolvimento

### Estrutura de Camadas

- **Controller**: Recebe requisições HTTP e retorna respostas
- **Service**: Contém a lógica de negócio
- **Repository**: Interface para acesso aos dados
- **Entity**: Representação das tabelas do banco de dados
- **DTO**: Objetos de transferência de dados

### Validações

Todas as validações são feitas usando Bean Validation (`@Valid`, `@NotBlank`, `@NotNull`, etc.) e erros são tratados de forma centralizada pelo `GlobalExceptionHandler`.

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

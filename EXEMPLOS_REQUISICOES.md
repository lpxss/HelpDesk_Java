# Exemplos de Requisições HTTP

Este arquivo contém exemplos práticos de requisições HTTP para testar a API.

## Autenticação

### 1. Registrar um novo usuário

```bash
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "joao@example.com",
  "senha": "senha123456",
  "nome": "João Silva"
}
```

### 2. Login

```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "senha": "senha123456"
}
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "usuario": {
    "id": 1,
    "email": "joao@example.com",
    "nome": "João Silva",
    "perfil": "USUARIO",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

## Tickets

### 3. Criar um ticket

**Nota:** Substitua `{token}` pelo token recebido no login.

```bash
POST http://localhost:8080/tickets
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Problema com impressora",
  "descricao": "A impressora não está imprimindo corretamente",
  "categoria": "HARDWARE",
  "prioridade": "ALTA"
}
```

**Valores válidos para categoria:**
- HARDWARE
- SOFTWARE
- REDE
- OUTROS

**Valores válidos para prioridade:**
- BAIXA
- MEDIA
- ALTA

### 4. Listar todos os tickets

```bash
GET http://localhost:8080/tickets
Authorization: Bearer {token}
```

### 5. Buscar um ticket por ID

```bash
GET http://localhost:8080/tickets/1
Authorization: Bearer {token}
```

### 6. Atualizar status do ticket

**Nota:** Apenas usuários com perfil SUPORTE ou ADMIN podem atualizar status.

```bash
PUT http://localhost:8080/tickets/1/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "EM_ATENDIMENTO"
}
```

**Valores válidos para status:**
- ABERTO
- EM_ATENDIMENTO
- RESOLVIDO
- FECHADO

## Comentários

### 7. Adicionar um comentário a um ticket

```bash
POST http://localhost:8080/tickets/1/comments
Authorization: Bearer {token}
Content-Type: application/json

{
  "texto": "Verificando o problema..."
}
```

### 8. Listar comentários de um ticket

```bash
GET http://localhost:8080/tickets/1/comments
Authorization: Bearer {token}
```

## Fluxo Completo de Exemplo

### Passo 1: Registrar um usuário
```bash
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "usuario@test.com",
  "senha": "senha123456",
  "nome": "Usuário Teste"
}
```

### Passo 2: Criar um ticket
```bash
POST http://localhost:8080/tickets
Authorization: Bearer {token_do_passo_1}
Content-Type: application/json

{
  "titulo": "Problema de conexão",
  "descricao": "Não consigo me conectar à rede",
  "categoria": "REDE",
  "prioridade": "MEDIA"
}
```

### Passo 3: Adicionar um comentário
```bash
POST http://localhost:8080/tickets/1/comments
Authorization: Bearer {token_do_passo_1}
Content-Type: application/json

{
  "texto": "Testando a conexão..."
}
```

### Passo 4: Listar comentários
```bash
GET http://localhost:8080/tickets/1/comments
Authorization: Bearer {token_do_passo_1}
```

## Testando com cURL

### Registrar usuário
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@example.com","senha":"senha123456","nome":"Teste"}'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@example.com","senha":"senha123456"}'
```

### Criar ticket
```bash
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{"titulo":"Teste","descricao":"Descrição do teste","categoria":"SOFTWARE","prioridade":"BAIXA"}'
```

### Listar tickets
```bash
curl -X GET http://localhost:8080/tickets \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

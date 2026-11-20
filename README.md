# 🚨 StormAid API

O **StormAid** é uma plataforma inteligente de gestão de emergências, zeladoria comunitária e resposta a catástrofes. A API integra cidadãos, dependentes e socorristas/reguladores institucionais através de uma arquitetura robusta em Spring Boot com segurança JWT, persistência em Oracle Database, mensageria assíncrona com RabbitMQ e inteligência artificial com Spring AI (RAG).

---

# 🚀 Swagger UI & Consoles

Acesse a documentação interativa e realize testes diretamente pelo navegador:

```bash
https://stormaid-java-gs.onrender.com/swagger-ui/index.html
```
```bash
http://localhost:8080/swagger-ui/index.html
```
```bash
http://localhost:8080
```
---

# 🔗 Link YouTube (Pitch)

```bash
https://youtu.be/ae4XAPM-IjA
```
---

## 👥 Integrantes do Grupo

<table>
  <tr>
    <td width="130">
      <img src="https://github.com/moisesBarsoti.png" width="120" style="border-radius: 50%;"/>
    </td>
    <td>
      <b>Moisés Barsoti Andrade de Oliveira</b><br/>
      <b>RM:</b> 565049 &nbsp;&nbsp;|&nbsp;&nbsp;<b>Turma:</b> 2TDSPO - FIAP <br/>
    </td>
  </tr>

  <tr>
    <td width="130">
      <img src="https://github.com/sSofia-s.png" width="120" style="border-radius: 50%;"/>
    </td>
    <td>
      <b>Sofia Siqueira Fontes</b><br/>
      <b>RM:</b> 563829 &nbsp;&nbsp;|&nbsp;&nbsp;<b>Turma:</b> 2TDSPG - FIAP <br/>
    </td>
  </tr>

  <tr>
    <td width="130">
      <img src="https://github.com/manuelalacerda.png" width="120" style="border-radius: 50%;"/>
    </td>
    <td>
      <b>Manuela de Lacerda Soares</b><br/>
      <b>RM:</b> 564887 &nbsp;&nbsp;|&nbsp;&nbsp;<b>Turma:</b> 2TDSPG - FIAP <br/>
    </td>
  </tr>
</table>

---

## ⚙️ Tecnologias e Configurações
### Tecnologias Utilizadas
- **Java 17 / 21** & **Spring Boot 3.2.5**
- **Spring AI (Integração com LLM e RAG para análise de incidentes)**
- **Spring Security** com **JWT (JSON Web Token)**
- **Oracle Database** (Ambiente Principal) & **H2 Database** (Ambiente de Testes)
- **Flyway Migration** (Gerenciamento de Schemas SQL)
- **RabbitMQ** (Mensageria para criação e processamento assíncrono de incidentes)
- **JUnit 5 / MockMvc** (Testes de Integração)

---

## 🤖 Declaração de Uso de Inteligência Artificial (Spring AI & RAG)

- **Ferramentas Utilizadas**: Spring AI integrado com modelos OpenAI (LLM e Embeddings) e recuperação de contexto vetorial (RAG).
- **Fluxo Real de Spring AI / RAG**: Quando uma ocorrência é criada, o evento é publicado no RabbitMQ (`incident.ai.queue`). O consumidor aciona o `RagAnalysisService`, que consulta a base de conhecimento do sistema para recuperar contexto relevante e gera uma análise automatizada de triagem e risco persistida na ocorrência.
- **Transcrição de Áudio (Simulada vs. Real)**: O sistema suporta rotinas assíncronas para processamento de áudios de emergência utilizando atualmente uma **transcrição simulada** (baseada em metadados/chaves de teste pré-definidos para demonstração académica), diferindo de uma **transcrição real** via APIs de Speech-to-Text (como Whisper) em tempo real.
- **Limitações**: A análise depende da disponibilidade e quota da API externa da OpenAI e do contexto previamente indexado na base de conhecimento; em caso de falha na API externa, o sistema aplica fallback sem bloquear o registo da ocorrência.

---

## 📊 Arquitetura e Modelo de Dados (Diagrama ER)

Abaixo está representado o diagrama de entidades e relacionamentos (ER) da base de dados do StormAid, mapeando utilizadores, perfis, dependentes, ocorrências e análises de IA:

```mermaid
erDiagram
USER ||--o{ USER_ROLE : has
ROLE ||--o{ USER_ROLE : grants
USER ||--o| CITIZEN_PROFILE : has_citizen_profile
USER ||--o| RESPONDER_PROFILE : has_responder_profile
USER ||--o{ DEPENDENT : manages
USER ||--o{ INCIDENT_REPORT : submits
DEPENDENT o|--o{ INCIDENT_REPORT : may_be_subject
INCIDENT_REPORT ||--o{ INCIDENT_AI_ANALYSIS : receives
USER ||--o{ COMMUNITY_RISK_REPORT : submits
USER o|--o{ RESPONDER_PROFILE : authorizes
```
---

## 🔐 Roles e Perfis de Acesso (Os Quatro Perfis)

O sistema utiliza quatro perfis (roles) de autorização distintos:

1. `CITIZEN`: Cidadão comum com permissão para gerenciar seu próprio perfil, cadastrar dependentes, registrar ocorrências de incidentes e relatos de riscos comunitários.
2. `RESPONDER`: Socorrista institucional responsável pelo atendimento de emergências no local. O **cadastro institucional** do socorrista é submetido no sistema e permanece pendente de validação até ser analisado e aprovado por um `REGULATOR` através do fluxo interno de credenciamento.
3. `REGULATOR`: Agente regulador encarregado da triagem, verificação de credenciais institucionais, aprovação/autorização de socorristas (`RESPONDER`) e gestão estratégica de equipes.
4. `ADMIN`: Administrador global do sistema, com permissão para gestão avançada de utilizadores, controlo de acessos e auditoria administrativa.

---

## 🛠️ Configuração de Banco de Dados (Oracle & Flyway) e JWT

### 1. Migrações de Banco de Dados (Flyway)
As migrações SQL são executadas automaticamente na inicialização da aplicação em ambiente de produção/desenvolvimento Oracle:
- `V1__create_stormaid_schema.sql`: Criação das tabelas base do esquema do StormAid.
- `V2__seed_roles.sql`: Carga inicial dos perfis (roles) do sistema.
- `V3__create_responder_profile.sql`: Criação da estrutura de perfil de socorrista (RESPONDER).
- `V4__alter_role_constraint.sql`: Atualização de restrições (constraints) de perfis.
- `V5__create_password_reset_table.sql`: Criação da tabela de códigos de recuperação de senha.
- `V6__add_attempts_to_password_reset.sql`: Adição do controle de tentativas na tabela de recuperação de senha.
- `V7__add_cpf_and_birth_date_to_user.sql`: Adição dos campos de CPF e data de nascimento na tabela de usuários.
- `V8__add_accessibility_fields_to_citizen_profile.sql`: Adição de campos de acessibilidade no perfil do cidadão.
- `V9__add_accessibility_fields_to_dependent.sql`: Adição de campos de acessibilidade nos dependentes.
- `V10__add_token_version_to_user.sql`: Adição da coluna de versionamento de token (TOKEN_VERSION) no usuário.

### 2. Variáveis de Ambiente
Configure as seguintes variáveis no seu ambiente ou no `application.properties`:

```properties
# Base de Dados Oracle
ORACLE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
ORACLE_USER=seu_usuario_rm
ORACLE_PASSWORD=sua_senha_oracle

# Mensageria RabbitMQ
RABBITMQ_HOST=127.0.0.1
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=stormaid
RABBITMQ_PASSWORD=sua_senha_rabbitmq
RABBITMQ_VIRTUAL_HOST=/

# Servidor & Segurança JWT
PORT=8080
JWT_SECRET=sua_chave_jwt_super_segura_aqui

# Inteligência Artificial (Spring AI / OpenAI)
API_KEY=sua_chave_api_openai_aqui

# E-mail (SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@exemplo.com
MAIL_PASSWORD=sua_senha_ou_app_password
```
---

## 📬 Mensageria Assíncrona (RabbitMQ)

A API do StormAid publica eventos assíncronos críticos:
- Exchange: stormaid.incidents
- Fila de Incidentes: incident.created.v1
- Fila de IA (RAG): incident.ai.queue
- Atualização de Status (via .NET): consumo idempotente com controlo de versão de evento (IncidentStatusChangedConsumer).
---

## 🧪 Execução de Testes Automatizados

Os testes unitários utilizam JUnit 5 + Mockito, e os testes de integração utilizam MockMvc com banco isolado H2 em memória (src/test/resources/application.properties).

### Suíte de Testes Coberta

- AuthControllerTest: Autenticação, registo e bloqueio de acesso sem token JWT (403 Forbidden).
- CitizenRegistrationServiceTest: Validação de regras de cadastro completo do cidadão.
- CitizenProfileServiceTest: Consulta de perfil padrão, atualização de endereço, vulnerabilidade (MobilityStatus) e contacto de emergência.
- DependentServiceTest: Criação de dependente com herança de endereço do responsável, validação de parentesco (RelationshipType), isolamento entre responsáveis (403) e remoção lógica (soft delete).
- PasswordRecoveryServiceTest: Envio de código com rate limit, invalidação de códigos anteriores, verificação de código com geração de tempToken, bloqueio por tentativas falhas e redefinição de senha com incremento de tokenVersion.
- IncidentReportServiceTest, IncidentReportControllerTest & IncidentReportControllerRecoveryTest: Regras de negócio de ocorrências, validações e recuperação.
- IncidentEventPublisherTest & IncidentStatusChangedConsumerTest: Publicação pós-commit no RabbitMQ, idempotência e rejeição de payloads inválidos.
- AddressServiceTest & AppVersionServiceTest: Consulta de CEP e metadados de versão/build.

### Comandos Maven:

**Executar todos os testes (Windows PowerShell):**
```powershell
.\mvnw clean test
```

**Executar todos os testes (Linux / macOS):**
```bash
./mvnw clean test
```

**Gerar pacote executável (.jar):**
```bash
.\mvnw -DskipTests clean package
```

---

## 💻 Como Executar a Aplicação

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```
---

### Execução via Docker e Docker Compose

1. **Subir os serviços de infraestrutura (RabbitMQ e Oracle):**
   ```bash
   docker-compose up -d
   ```

2 **Rodar build Docker:**
   ```bash
   docker build -t stormaid-api .
   ```
---

## 🔑 Contas de Teste Pré-Configuradas

Para facilitar a avaliação, a aplicação inicializa automaticamente contas de teste para cada perfil de acesso do sistema:

| Perfil | E-mail de Acesso | Senha | Descrição |
| :--- | :--- | :--- | :--- |
| **CITIZEN** | `cidadao@stormaid.example` | `SenhaForte!2026` | Cidadão comum para abertura de incidentes e dependentes |
| **RESPONDER** | `carlos.socorrista@stormaid.example` | `SenhaForte!2026` | Socorrista institucional de campo |
| **REGULATOR** | `regulator@stormaid.example` | `SenhaForte!2026` | Agente regulador e gestor de credenciamento |
| **ADMIN** | `admin@stormaid.example` | `SenhaForte!2026` | Administrador global do sistema |

---
## 📌 Endpoints da API

### 🔐 1. Autenticação (`/api/v1/auth`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/v1/auth/register` | Registo básico de novos utilizadores com role `CITIZEN` | Público |
| `POST` | `/api/v1/auth/register-complete` | Cadastro completo do cidadão (dados pessoais, endereço, vulnerabilidade e contacto de emergência)| Público |
| `POST` | `/api/v1/auth/login` | Realiza o login na aplicação e retorna o token JWT | Público|
| `GET` | `/api/v1/auth/me` | Retorna os dados do utilizador autenticado| Autenticado|


#### Exemplo de Payload JSON — POST /api/v1/auth/register-complete
```JSON
{
"fullName": "Maria Silva",
"email": "maria.silva@stormaid.example",
"cpf": "12345678901",
"birthDate": "1990-05-15",
"password": "SenhaForte!2026",
"phoneNumber": "11999999999",
"address": {
"postalCode": "01310-100",
"street": "Avenida Paulista",
"number": "1000",
"complement": "Apto 42",
"neighborhood": "Bela Vista",
"city": "São Paulo",
"state": "SP"
},
"vulnerability": {
"mobilityStatus": "ASSISTED",
"hasVisualImpairment": false,
"hasHearingImpairment": false,
"equipmentDescription": "Bengala de apoio",
"accessibilityNotes": "Necessita de apoio na evacuação"
},
"emergencyContact": {
"name": "Ana Paula",
"phoneNumber": "11988887777",
"relationship": "Irmã"
}
}
```
---

### 👤 2. Utilizadores (`/api/v1/users`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/users/me` | Consulta os dados do utilizador autenticado | Autenticado |
| `PUT` | `/api/v1/users/me` | Atualiza os dados cadastrais básicos do utilizador autenticado | Autenticado |
| `DELETE` | `/api/v1/users/me` | Remove ou desativa o utilizador autenticado | Autenticado |
| `GET` | `/api/v1/internal/users` | Consulta interna de utilizadores | Interno / Conforme configuração|

---

### 🪪 3. Perfil do Cidadão (`/api/v1/citizens`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/citizens/me/profile` | Retorna os dados do perfil do cidadão autenticado | `CITIZEN` |
| `PUT` | `/api/v1/citizens/me/profile` | Atualiza endereço, condições de vulnerabilidade (AUTONOMOUS, ASSISTED, WHEELCHAIR_USER, BEDRIDDEN, UNKNOWN) e contacto de emergência | `CITIZEN`|

---

### 👨‍👩‍👧 4. Dependentes (`/api/v1/dependents`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/dependents` | Lista os dependentes associados ao cidadão autenticado| `CITIZEN`|
| `POST` | `/api/v1/dependents` | Cadastra um novo dependente para o cidadão| `CITIZEN`|
| `GET` | `/api/v1/dependents/{id}` | Busca os detalhes de um dependente por ID | `CITIZEN`|
| `PUT` | `/api/v1/dependents/{id}` | Atualiza os dados de um dependente| `CITIZEN`|
| `DELETE` | `/api/v1/dependents/{id}` | Remove o registo de um dependente| `CITIZEN`|

---

### 🚨 5. Ocorrências de Incidentes (`/api/v1/incidents`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/incidents` | Lista as ocorrências registadas| Autenticado / Conforme perfil|
| `POST` | `/api/v1/incidents` | Regista nova ocorrência (dispara evento `incident.created.v1`)| Autenticado / Conforme perfil|
| `GET` | `/api/v1/incidents/{id}` | Consulta detalhes de uma ocorrência específica| `CITIZEN`, `RESPONDER`, `REGULATOR`|

---

### 🛡️ 6. Relatos de Risco Comunitário (`/api/v1/community-risks`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/community-risks` | Lista os relatos de risco de zeladoria urbana| `CITIZEN`, `REGULATOR`|
| `POST` | `/api/v1/community-risks` | Cadastra um novo relato de risco comunitário| `CITIZEN`|
| `GET` | `/api/v1/community-risks/{id}` | Detalhes de um relato de risco específico| `CITIZEN`, `REGULATOR`|
| `PUT` | `/api/v1/community-risks/{id}` | Atualiza as informações de um relato de risco| `CITIZEN`|
| `DELETE` | `/api/v1/community-risks/{id}` | Remove um relato de risco cadastrado| `CITIZEN`, `REGULATOR`|

---

### 📍 7. Endereços / CEP (`/api/v1/addresses`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/addresses/cep/{cep}` | Consulta endereço por CEP| Autenticado / Conforme configuração |

---

### 🌦️8. Meteorologia (`/api/v1/weather`)

| Método | Endpoint | Descrição | Acesso    |
|--------|----------|-----------|-----------|
| `GET` | `/api/v1/weather` | Consulta condições meteorológicas atuais e previsão por coordenadas/localidade | Autenticado / Público |

---

### 🔑 9. Recuperação de Senha (`/api/v1/password-recovery`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/v1/password-recovery/request` | Solicita recuperação de senha e envia código por e-mail (SMTP) | Público|
| `POST` | `/api/v1/password-recovery/verify` | Verifica o código de recuperação de senha| Público|
| `POST` | `/api/v1/password-recovery/reset` | Redefine a senha do utilizador| Público |

---

### 🚑 10. Socorristas e Credenciamento Interno (`/api/v1/responders` e `/api/v1/internal`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/responders/me/profile` | Retorna o perfil do socorrista autenticado| `RESPONDER`|
| `GET` | `/api/v1/internal` | Consulta interna de socorristas | `REGULATOR` / Interno|
| `PATCH` | `/api/v1/internal/{id}/authorization` | Atualiza status de verificação/credenciamento| `REGULATOR`|
| `GET` | `/api/v1/internal/{userId}/eligibility` | Consulta elegibilidade institucional do socorrista | `REGULATOR`, `ADMIN`|

---

### 🔗 11. Integração Interna — Incidentes (`.NET`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/internal/incidents/{id}/regulation-view` | Obtém visão de regulação do incidente para integração com .NET | Interno / Conforme configuração |

---

### ℹ️ 12. Versão e Build da Aplicação (`/api/v1/version / Actuator`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|----------|--------|
| `GET`  | `/api/v1/version` | Retorna informações de versão da aplicação, ambiente e hash do commit Git| `Público / Conforme configuração` |

---

### ⚙️ 13. Administração do Sistema (`/api/v1/admin`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/v1/admin/regulators` | Cadastra ou promove reguladores no sistema| `ADMIN` |

---

## 📸 Evidências e Testes da Aplicação 

Esta secção documenta as evidências de execução, testes funcionais, validação de regras de negócio e a robustez de segurança implementada na API do StormAid.

### 1. Documentação Interativa (Swagger UI)
Abaixo está a interface geral do Swagger UI a expor todos os controladores da API (autenticação, incidentes, dependentes, perfis e relatórios comunitários):
<p align="center">
  <img src="docs/images/swagger-ui.png" width="300" alt="Swagger UI - Visão Geral">
</p>

### 2. Testes de Regras de Negócio e Validação de Exceções (`400 Bad Request`)
O endpoint de registo de incidentes valida rigorosamente os dados enviados no payload (como a existência de dependentes e parâmetros obrigatórios), retornando um erro limpo de Bad Request quando há inconsistências de negócio, antes de prosseguir com o fluxo de mensageria e IA:
<p align="center">
  <img src="docs/images/bad-request-test.png" width="300" alt="Teste com Erro de Negócio">
</p>

### 3. Testes de Sucesso e Persistência do Incidente (`200 OK`)
Após a validação correta dos parâmetros e do envio com o token de autenticação, o endpoint `POST /api/v1/incidents` processa com sucesso o pedido, persistindo o incidente na base de dados e acionando o ecossistema assíncrono:
<p align="center">
  <img src="docs/images/success-incident-test.png" width="300" alt="Teste com Sucesso">
</p>

### 4. Suíte de Testes Automatizados
* **Ferramenta**: JUnit 5, Mockito e MockMvc (Testes unitários e de integração com H2 em memória).
* **Status da Execução**: `BUILD SUCCESS` (Garante que a inicialização do contexto Spring Boot, regras de perfil/dependentes/recuperação de senha, mensageria e o bloqueio de acessos não autorizados por JWT funcionam perfeitamente).
<p align="center">
  <img src="docs/images/build-success.png" width="300" alt="BUILD SUCCESS">
</p>


# 🚨 StormAid API

O StormAid é uma plataforma inteligente de gestão de emergências, zeladoria comunitária e resposta a catástrofes, integrando cidadãos, dependentes e socorristas institucionais através de uma arquitetura robusta e mensageria assíncrona.

---

# 🚀 Swagger UI & Consoles

Acesse a documentação interativa e realize testes diretamente pelo navegador:

```bash
LINK RENDER
```
```bash
http://localhost:8080/swagger-ui/index.html
```
```bash
http://localhost:8080
```
---

# 🔗 Link Youtube

```bash
https://www.youtube.com/watch?v=OFF-OnubFsc
```
---

# ⚙️ Como Configurar e Executar com Oracle / Flyway

### 1. Pré-requisitos
* Java JDK 21 instalada.
* Acesso a uma instância do **Oracle Database** e servidor **RabbitMQ** ativo.

### 2. Configuração de Variáveis de Ambiente
Configure as seguintes variáveis no seu ambiente ou no arquivo de propriedades:
* `ORACLE_URL`: `jdbc:oracle:thin:@//host:port/service`
* `ORACLE_USER`: Seu usuário do banco
* `ORACLE_PASSWORD`: Sua senha do banco
* `JWT_SECRET`: Chave secreta para os tokens
* `SPRING_RABBITMQ_HOST`: Endereço do servidor RabbitMQ
* `SPRING_RABBITMQ_PORT`: Porta do RabbitMQ (ex: 5672)

*(Nota: O projeto não possui um carregador específico de arquivo `.env`; o Spring lê as variáveis de ambiente diretamente).*

### 3. Executando as Migrações
O projeto utiliza o **Flyway** para gerenciar e versionar o banco de dados Oracle de forma automatizada ao iniciar a aplicação (scripts V1, V2 e V3).

---

# 💻 Como Iniciar a Aplicação
Mac/Linux
```bash
chmod +x mvnw
./mvnw spring-boot:run
```
Windows
```bash
.\mvnw.cmd spring-boot:run
```
---

🧪 Executando os Testes Automatizados
Mac/Linux
```bash
./mvnw clean test
```
Windows
```bash
.\mvnw.cmd clean test
```
---

# 🏗️ Divisão da Arquitetura
    Frontend (React Native / Web)
    ├── Java Spring Boot -> Autenticação, Gestão Operacional, Incidentes e RabbitMQ
    └── .NET -> Integração Comercial e Serviços de Apoio
* JWT Cross-Platform: O token JWT gerado pelo backend Java é totalmente compatível e validado pelos microsserviços associados.
---

# 🔐 Roles e Perfis do Sistema
    * CITIZEN (Cidadão)
    * RESPONDER (Socorrista Institucional)
    * ADMIN (Administrador do Sistema)
---

# 📅 Cronograma

| Atividade | Responsável | Data | Status |
|--------|----------|------------|------------|
| Implementação inicial da entidade User e AuthController | Manuelalacerda | 10/11 | Concluído |
| Configuração do Swagger/OpenAPI e Segurança JWT | Manuelalacerda | 10/11 | Concluído |
| Criação do módulo de Gestão de Dependentes (`DependentController`) | Manuelalacerda | 10/11 | Concluído |
| Implementação do Perfil do Cidadão (`CitizenProfile`) | Manuelalacerda | 10/11 | Concluído |
| Desenvolvimento do Registo de Incidentes (`IncidentReport`) | Manuelalacerda | 10/11 | Concluído |
| Implementação da Zeladoria Comunitária (`CommunityRiskReport`) | Manuelalacerda | 10/11 | Concluído |
| Criação dos Perfis de Socorristas e Autorização Institucional | Manuelalacerda | 10/11 | Concluído |
| Configuração da Mensageria Assíncrona com RabbitMQ (`RabbitMQConfig`) | Manuelalacerda | 10/11 | Concluído |
| Organização do Versionamento em Commits Separados por Camadas | Manuelalacerda | 10/11 | Concluído |

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

# 🔐 1. Auth Controller (`/auth`)

| Método | Endpoint | Descrição/Perfil de Acesso                         |
|--------|----------|----------------------------------------------------|
| POST | `/auth/login` | Realiza o login na aplicação e retorna o token JWT |
| POST | `/auth/register` | Registo público de novos utilizadores              |

---
LOGIN
```bash
{
  "email": "cidadao@stormaid.com",
  "password": "SenhaSegura123!"
}
```
---

# 👤 2. Citizen Profile Controller (`/api/v1`)

| Método | Endpoint | Descrição/Perfil de Acesso                      |
|--------|----------|-------------------------------------------------|
| GET | `/api/v1/citizens/me/profile` | Retorna o perfil detalhado do cidadão autenticado |
| PUT | `/api/v1/citizens/me/profile` | Atualiza o perfil do cidadão autenticado        |

---

# 👨‍👩‍👧 3. Dependent Controller (`/api/v1`)

| Método | Endpoint | Descrição/Perfil de Acesso                   |
|--------|----------|----------------------------------------------|
| GET | `/api/v1/dependents` | Lista os dependentes associados ao cidadão   |
| POST | `/api/v1/dependents` | Adiciona um novo dependente ao cidadão       |
| GET | `/api/v1/dependents/{id}` | Busca um dependente específico pelo ID       |
| PUT | `/api/v1/dependents/{id}` | Atualiza os dados de um dependente           |
| DELETE | `/api/v1/dependents/{id}` | Remove um dependente do registo              |

---

# 🚨 4. Incident Report Controller (`/api/v1`)

| Método | Endpoint | Descrição/Perfil de Acesso                    |
|--------|----------|-----------------------------------------------|
| GET | `/api/v1/incidents` | Lista todas as ocorrências do cidadão         |
| POST | `/api/v1/incidents` | Regista uma nova ocorrência (publica no RabbitMQ) |
| GET | `/api/v1/incidents/{id}` | Consulta detalhes de uma ocorrência específica |

---

# 🛡️ 5. Community Risk Report Controller (`/api/v1`)

| Método | Endpoint | Descrição/Perfil de Acesso                     |
|--------|----------|------------------------------------------------|
| GET | `/api/v1/community-risks` | Lista os relatos de risco comunitário          |
| POST | `/api/v1/community-risks` | Cria um novo relato de zeladoria comunitária   |
| GET | `/api/v1/community-risks/{id}` | Detalhes de um relato de risco específico      |
| PUT | `/api/v1/community-risks/{id}` | Atualiza um relato de risco comunitário        |
| DELETE | `/api/v1/community-risks/{id}` | Remove um relato de risco                      |

---

# 🚑 6. Responder Profile Controller (`/api/v1`)

| Método | Endpoint | Descrição/Perfil de Acesso                      |
|--------|----------|-------------------------------------------------|
| GET | `/api/v1/responders/me/profile` | Retorna o perfil do socorrista autenticado      |
| GET | `/api/v1/internal/responders` | Lista todos os socorristas (Acesso Restrito/Admin) |
| GET | `/api/v1/internal/responders/{id}` | Busca socorrista por ID (Acesso Restrito/Admin)  |
| PATCH | `/api/v1/internal/responders/{id}/authorization` | Atualiza o status de verificação/credenciamento |

---

### 🗄 Modelo e Arquitetura

A arquitetura do StormAid foi planeada para garantir resiliência e alta performance em cenários críticos de emergência, estruturando-se em camadas bem definidas (`config`, `controller`, `dto`, `entity`, `messaging`, `repository`, `service`).
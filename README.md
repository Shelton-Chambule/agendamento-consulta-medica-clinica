# Medical Appointment Scheduling System

[Read in Portuguese below / Leia em português mais abaixo](#sistema-de-agendamento-de-consultas-médicas)

REST API for managing medical appointment scheduling at a clinic, secured with **Spring Security and JWT**. Built as a learning project to consolidate concepts of layered architecture, JPA/Hibernate, authentication/authorization, and REST API best practices with Spring Boot.

## Technologies

* Java 21
* Spring Boot 4.1.0
* Spring Web — REST API
* Spring Security — stateless authentication and role-based authorization
* JWT (`com.auth0:java-jwt`) — token generation and validation (HMAC256)
* BCrypt — password hashing
* Spring Data JPA / Hibernate
* Jakarta Persistence (JPA) and Jakarta Bean Validation (`@Valid`)
* PostgreSQL — relational database
* Maven — dependency management and build
* Lombok — boilerplate reduction
* StarUML — class diagram modeling for the domain, done before implementation

## Architecture

The project follows a layered architecture, with DTOs forming the boundary between JPA entities and the API, avoiding direct entity exposure and JSON serialization loops caused by bidirectional relationships. Security sits in front of the controllers as a filter chain:

```
Request → SecurityFilter (JWT) → Controller → Service → Repository → Entity
```

Main package structure (`com.clinica.agendamento_consulta_medica`):

* `configuration` — security setup: `ConfigurationSecurity`, `SecurityFilter`, `SecurityExceptionHandler`
* `controller` — REST controllers (including `AccountAuthenticationController` for login and admin registration)
* `service` — business rules, plus `AccountService` (`UserDetailsService`) and `TokenService` (JWT)
* `repository` — Spring Data JPA interfaces
* `entity` — Account, Doctor, Patient, Consultation, Specialty, MedicalSchedule, Prescription, HistoryPatient
* `entity.enums` — `StatusConsultation` (WAITING, CONFIRMED, SCHEDULED, CARRIED_OUT, CANCELED) and `AccountRole` (ADMIN, DOCTOR, PATIENT)
* `dto` — request/response DTOs per entity
* `exception` — domain exceptions (`ResourceNotFoundException`, `DataBaseException`, `ScheduleConflictException`, `UniqueLoginException`, `AccessDeniedException`, `ProcessConsultation`, `ValidateStatusConsultation`), global handling via `@ControllerAdvice`, and the standardized error response (`StandardError`)

## Security

### Authentication (JWT)

1. The user sends `login` and `password` to `POST /authentications/login`.
2. `AuthenticationManager` validates the credentials against `AccountService`, which loads the account by login, and the stored BCrypt hash.
3. `TokenService` issues a signed JWT (issuer `clinica.agendamento`, subject = login, **expires in 2 hours**).
4. On every later request, the client sends the header `Authorization: Bearer <token>`.
5. `SecurityFilter` (a `OncePerRequestFilter` registered before `UsernamePasswordAuthenticationFilter`) extracts and validates the token, loads the account and places it in the `SecurityContext`. Invalid, expired or missing tokens leave the request unauthenticated.

The API is **stateless** (`SessionCreationPolicy.STATELESS`) and CSRF protection is disabled, since authentication relies on the bearer token instead of session cookies.

### Authorization (roles)

There are three roles, stored in the `Account` entity:

| Role | Description |
|------|-------------|
| `ADMIN` | Manages doctors, specialties, schedules and has read access to all records |
| `DOCTOR` | Manages their own profile, processes their consultations and issues prescriptions |
| `PATIENT` | Self-registers, books and manages their own consultations |

Authorization works in **two layers**:

1. **Route level** — `ConfigurationSecurity` defines which roles can call each endpoint. Any route not explicitly listed is denied (`anyRequest().denyAll()`).
2. **Resource level** — the services verify ownership. For example, a patient can only read, update or cancel their own consultations, and a doctor can only process consultations assigned to them. `ADMIN` bypasses ownership checks.

### Security error responses

`SecurityExceptionHandler` returns the same standardized JSON used by the rest of the API:

* `401 Unauthorized` — missing, invalid or expired token
* `403 Forbidden` — authenticated, but without permission

### Endpoints and access

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/authentications/login` | Public |
| POST | `/authentications/register/admin` | ADMIN |
| POST | `/patients/save` | Public (patient self-registration) |
| GET | `/patients/getAllPatient` | ADMIN |
| GET / PUT / DELETE | `/patients/{patientId}` | ADMIN, or the PATIENT who owns it |
| POST | `/doctors/create` | ADMIN |
| GET | `/doctors/getAll` | ADMIN |
| GET / PUT / DELETE | `/doctors/{doctorId}` | ADMIN, or the DOCTOR who owns it |
| POST / PUT / DELETE | `/specialtys/save`, `/specialtys/{specialtyId}` | ADMIN |
| GET | `/specialtys/getAll`, `/specialtys/{specialtyId}` | Any authenticated user |
| POST / PUT / DELETE | `/medicalschedules/save`, `/medicalschedules/{id}` | ADMIN |
| GET | `/medicalschedules/findAll`, `/medicalschedules/{id}` | Any authenticated user |
| POST | `/consultations/save` | PATIENT |
| GET | `/consultations/getAll` | ADMIN |
| GET | `/consultations/{consultationId}` | ADMIN, or the patient/doctor of the consultation |
| PUT / DELETE | `/consultations/{consultationId}` | ADMIN, or the PATIENT who owns it |
| POST | `/consultations/{consultationId}/process` | ADMIN, or the DOCTOR of the consultation |
| POST | `/prescriptions/create` | ADMIN, DOCTOR (of the consultation) |
| GET | `/prescriptions/getAll` | ADMIN |
| GET | `/prescriptions/{id}` | ADMIN, or the doctor/patient of the consultation |
| PUT / DELETE | `/prescriptions/{id}` | ADMIN, DOCTOR (of the consultation) |
| GET | `/historyConsultation/**` | ADMIN (read-only) |

The patient history is **read-only through the API**: it is created and updated automatically by the consultation flow, and every write operation on `/historyConsultation/**` is denied.

## Implemented features

* JWT authentication with BCrypt password hashing and role-based access control (ADMIN, DOCTOR, PATIENT)
* Unique login validation across all account types, and password re-encoding on profile updates
* Public patient self-registration; doctor and admin accounts can only be created by an administrator
* Full CRUD for: Doctors, Patients, Specialties, Medical schedules, Consultations and Prescriptions; read-only access to Patient history
* Schedule conflict validation per doctor when booking or rescheduling a consultation (`hasScheduleConflict`), throwing `ScheduleConflictException` on overlap
* Consultation lifecycle rules:
   * A new consultation starts as `WAITING` and automatically creates its history record
   * Only `WAITING` consultations can be changed or cancelled
   * Processing a consultation moves it from `WAITING` to `CARRIED_OUT` and updates the history
* Prescriptions can only be issued for consultations already `CARRIED_OUT`
* Schedule validation: start time must be before end time, and consultation duration must be greater than zero
* Deletion protected against integrity violations (`DataBaseException` when a record is still referenced)
* JPA relationships modeled between entities:
   * Account ↔ Doctor / Patient (one-to-one)
   * Doctor ↔ Specialty (many-to-many)
   * Doctor ↔ Consultation and Patient ↔ Consultation (one-to-many)
   * Consultation ↔ HistoryPatient (one-to-one)
   * Prescription → Consultation (many-to-one)
* Global, centralized exception handling via `@ControllerAdvice`, returning standardized responses with timestamp, HTTP status, error type, message, and request path

## Future improvements

Two features were intentionally left out of this version, to be developed later:

* Referral of consultations/patients between doctors
* Reception module (receptionist profile) for the clinic's administrative management

## How to run

1. Clone the repository
2. Configure the PostgreSQL connection and the JWT secret in `application.properties` (or `application.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinica
spring.datasource.username=your_user
spring.datasource.password=your_password

# Secret used to sign the JWT tokens — never commit a real value
api.agendamento.clinico=${JWT_SECRET}
```

3. Define the `JWT_SECRET` environment variable, then run with Maven:

```
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

> **First administrator:** since `POST /authentications/register/admin` requires an authenticated ADMIN, the first administrator account must be inserted directly in the database (or by a data seeder). BCrypt-hash the password before inserting it.

### Quick test

```bash
# 1. Register a patient (public)
curl -X POST http://localhost:8080/patients/save \
  -H "Content-Type: application/json" \
  -d '{"login":"maria","password":"123456","name":"Maria","phone":"840000000","dataNascimento":"1995-04-10"}'

# 2. Login and get the token
curl -X POST http://localhost:8080/authentications/login \
  -H "Content-Type: application/json" \
  -d '{"login":"maria","password":"123456"}'

# 3. Call a protected endpoint
curl http://localhost:8080/specialtys/getAll \
  -H "Authorization: Bearer <token>"
```

## Author

Project developed by Shelton Chambule, Computer Programming student, as a learning exercise in backend development with Java and Spring Boot.

---

# Sistema de Agendamento de Consultas Médicas

[Read in English above](#medical-appointment-scheduling-system)

API REST para gestão de agendamento de consultas numa clínica médica, protegida com **Spring Security e JWT**. Desenvolvida como projeto de aprendizagem para consolidar conceitos de arquitetura em camadas, JPA/Hibernate, autenticação/autorização e boas práticas de construção de APIs REST com Spring Boot.

## Tecnologias

* Java 21
* Spring Boot 4.1.0
* Spring Web — construção da API REST
* Spring Security — autenticação stateless e autorização por perfis
* JWT (`com.auth0:java-jwt`) — geração e validação de tokens (HMAC256)
* BCrypt — encriptação de palavras-passe
* Spring Data JPA / Hibernate
* Jakarta Persistence (JPA) e Jakarta Bean Validation (`@Valid`)
* PostgreSQL — base de dados relacional
* Maven — gestão de dependências e build
* Lombok — redução de boilerplate
* StarUML — modelação do diagrama de classes do domínio, antes da implementação

## Arquitetura

O projeto segue uma arquitetura em camadas, com DTOs a fazer a fronteira entre as entidades JPA e a API, evitando exposição direta das entidades e loops de serialização JSON causados pelas relações bidirecionais. A segurança fica à frente dos controllers, como uma cadeia de filtros:

```
Pedido → SecurityFilter (JWT) → Controller → Service → Repository → Entity
```

Estrutura de pacotes principal (`com.clinica.agendamento_consulta_medica`):

* `configuration` — configuração de segurança: `ConfigurationSecurity`, `SecurityFilter`, `SecurityExceptionHandler`
* `controller` — controllers REST (incluindo `AccountAuthenticationController` para login e registo de administradores)
* `service` — regras de negócio, mais `AccountService` (`UserDetailsService`) e `TokenService` (JWT)
* `repository` — interfaces Spring Data JPA
* `entity` — Account, Doctor, Patient, Consultation, Specialty, MedicalSchedule, Prescription, HistoryPatient
* `entity.enums` — `StatusConsultation` (WAITING, CONFIRMED, SCHEDULED, CARRIED_OUT, CANCELED) e `AccountRole` (ADMIN, DOCTOR, PATIENT)
* `dto` — DTOs de pedido/resposta por entidade
* `exception` — exceções de domínio (`ResourceNotFoundException`, `DataBaseException`, `ScheduleConflictException`, `UniqueLoginException`, `AccessDeniedException`, `ProcessConsultation`, `ValidateStatusConsultation`), tratamento global via `@ControllerAdvice` e resposta padronizada de erro (`StandardError`)

## Segurança

### Autenticação (JWT)

1. O utilizador envia `login` e `password` para `POST /authentications/login`.
2. O `AuthenticationManager` valida as credenciais com o `AccountService`, que carrega a conta pelo login, e o hash BCrypt guardado.
3. O `TokenService` emite um JWT assinado (issuer `clinica.agendamento`, subject = login, **expira em 2 horas**).
4. Em todos os pedidos seguintes, o cliente envia o header `Authorization: Bearer <token>`.
5. O `SecurityFilter` (um `OncePerRequestFilter` registado antes do `UsernamePasswordAuthenticationFilter`) extrai e valida o token, carrega a conta e coloca-a no `SecurityContext`. Tokens inválidos, expirados ou ausentes deixam o pedido sem autenticação.

A API é **stateless** (`SessionCreationPolicy.STATELESS`) e a proteção CSRF está desativada, uma vez que a autenticação depende do token Bearer e não de cookies de sessão.

### Autorização (perfis)

Existem três perfis, guardados na entidade `Account`:

| Perfil | Descrição |
|--------|-----------|
| `ADMIN` | Gere médicos, especialidades e horários, e tem acesso de leitura a todos os registos |
| `DOCTOR` | Gere o seu próprio perfil, processa as suas consultas e emite receitas |
| `PATIENT` | Regista-se a si próprio, agenda e gere as suas consultas |

A autorização funciona em **duas camadas**:

1. **Nível da rota** — a `ConfigurationSecurity` define que perfis podem chamar cada endpoint. Qualquer rota não listada explicitamente é negada (`anyRequest().denyAll()`).
2. **Nível do recurso** — os services verificam a propriedade. Por exemplo, um paciente só pode consultar, alterar ou cancelar as suas próprias consultas, e um médico só pode processar consultas que lhe pertencem. O `ADMIN` ignora as verificações de propriedade.

### Respostas de erro de segurança

O `SecurityExceptionHandler` devolve o mesmo JSON padronizado usado no resto da API:

* `401 Unauthorized` — token ausente, inválido ou expirado
* `403 Forbidden` — autenticado, mas sem permissão

### Endpoints e acessos

| Método | Endpoint | Acesso |
|--------|----------|--------|
| POST | `/authentications/login` | Público |
| POST | `/authentications/register/admin` | ADMIN |
| POST | `/patients/save` | Público (auto-registo do paciente) |
| GET | `/patients/getAllPatient` | ADMIN |
| GET / PUT / DELETE | `/patients/{patientId}` | ADMIN, ou o PATIENT dono do registo |
| POST | `/doctors/create` | ADMIN |
| GET | `/doctors/getAll` | ADMIN |
| GET / PUT / DELETE | `/doctors/{doctorId}` | ADMIN, ou o DOCTOR dono do registo |
| POST / PUT / DELETE | `/specialtys/save`, `/specialtys/{specialtyId}` | ADMIN |
| GET | `/specialtys/getAll`, `/specialtys/{specialtyId}` | Qualquer utilizador autenticado |
| POST / PUT / DELETE | `/medicalschedules/save`, `/medicalschedules/{id}` | ADMIN |
| GET | `/medicalschedules/findAll`, `/medicalschedules/{id}` | Qualquer utilizador autenticado |
| POST | `/consultations/save` | PATIENT |
| GET | `/consultations/getAll` | ADMIN |
| GET | `/consultations/{consultationId}` | ADMIN, ou o paciente/médico da consulta |
| PUT / DELETE | `/consultations/{consultationId}` | ADMIN, ou o PATIENT dono da consulta |
| POST | `/consultations/{consultationId}/process` | ADMIN, ou o DOCTOR da consulta |
| POST | `/prescriptions/create` | ADMIN, DOCTOR (da consulta) |
| GET | `/prescriptions/getAll` | ADMIN |
| GET | `/prescriptions/{id}` | ADMIN, ou o médico/paciente da consulta |
| PUT / DELETE | `/prescriptions/{id}` | ADMIN, DOCTOR (da consulta) |
| GET | `/historyConsultation/**` | ADMIN (apenas leitura) |

O histórico do paciente é **apenas de leitura via API**: é criado e atualizado automaticamente pelo fluxo de consultas, e qualquer operação de escrita em `/historyConsultation/**` é negada.

## Funcionalidades implementadas

* Autenticação JWT com encriptação BCrypt das palavras-passe e controlo de acesso por perfis (ADMIN, DOCTOR, PATIENT)
* Validação de login único em todos os tipos de conta, e nova encriptação da palavra-passe nas atualizações de perfil
* Auto-registo público de pacientes; contas de médico e de administrador só podem ser criadas por um administrador
* CRUD completo para: Médicos, Pacientes, Especialidades, Horários médicos, Consultas e Receitas; acesso apenas de leitura ao Histórico de paciente
* Validação de conflito de horários por médico ao agendar ou reagendar uma consulta (`hasScheduleConflict`), lançando `ScheduleConflictException` em caso de sobreposição
* Regras do ciclo de vida da consulta:
   * Uma nova consulta começa em `WAITING` e cria automaticamente o seu registo de histórico
   * Só consultas em `WAITING` podem ser alteradas ou canceladas
   * Processar uma consulta move-a de `WAITING` para `CARRIED_OUT` e atualiza o histórico
* Receitas só podem ser emitidas para consultas já `CARRIED_OUT`
* Validações: a hora de início do horário deve ser anterior à hora de fim, e a duração da consulta deve ser maior que zero
* Eliminação protegida contra violações de integridade (`DataBaseException` quando o registo ainda é referenciado)
* Relações JPA modeladas entre entidades:
   * Account ↔ Doctor / Patient (um-para-um)
   * Doctor ↔ Specialty (muitos-para-muitos)
   * Doctor ↔ Consultation e Patient ↔ Consultation (um-para-muitos)
   * Consultation ↔ HistoryPatient (um-para-um)
   * Prescription → Consultation (muitos-para-um)
* Tratamento global e centralizado de exceções via `@ControllerAdvice`, devolvendo respostas padronizadas com timestamp, status HTTP, tipo de erro, mensagem e path do pedido

## Melhorias futuras

Duas funcionalidades ficaram propositadamente de fora desta versão, para serem desenvolvidas mais tarde:

* Encaminhamento de consultas/pacientes entre médicos
* Módulo de receção (perfil de rececionista) para gestão administrativa da clínica

## Como executar

1. Clonar o repositório
2. Configurar a ligação ao PostgreSQL e o segredo do JWT em `application.properties` (ou `application.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinica
spring.datasource.username=seu_utilizador
spring.datasource.password=sua_password

# Segredo usado para assinar os tokens JWT — nunca fazer commit de um valor real
api.agendamento.clinico=${JWT_SECRET}
```

3. Definir a variável de ambiente `JWT_SECRET` e executar com Maven:

```
mvn spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

> **Primeiro administrador:** como `POST /authentications/register/admin` exige um ADMIN autenticado, a primeira conta de administrador tem de ser inserida diretamente na base de dados (ou por um seeder). A palavra-passe deve ser encriptada com BCrypt antes de ser inserida.

### Teste rápido

```bash
# 1. Registar um paciente (público)
curl -X POST http://localhost:8080/patients/save \
  -H "Content-Type: application/json" \
  -d '{"login":"maria","password":"123456","name":"Maria","phone":"840000000","dataNascimento":"1995-04-10"}'

# 2. Fazer login e obter o token
curl -X POST http://localhost:8080/authentications/login \
  -H "Content-Type: application/json" \
  -d '{"login":"maria","password":"123456"}'

# 3. Chamar um endpoint protegido
curl http://localhost:8080/specialtys/getAll \
  -H "Authorization: Bearer <token>"
```

## Autor

Projeto desenvolvido por Shelton Chambule, estudante de Programação Informática, como exercício de aprendizagem em desenvolvimento backend com Java e Spring Boot.

# Medical Appointment Scheduling System

*[Read in Portuguese below / Leia em português mais abaixo](#sistema-de-agendamento-de-consultas-médicas)*

REST API for managing medical appointment scheduling at a clinic, built as a learning project to consolidate concepts of layered architecture, JPA/Hibernate, and REST API best practices with Spring Boot.

## Technologies

- **Java 21**
- **Spring Boot 4.1.0**
- **Spring Data JPA / Hibernate**
- **Spring Web** — REST API
- **PostgreSQL** — relational database
- **Maven** — dependency management and build
- **Lombok** — boilerplate reduction (`@Getter`, `@Setter`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`)
- **Jakarta Persistence (JPA)**
- **StarUML** — class diagram modeling for the domain, done before implementation

## Architecture

The project follows a layered architecture, with DTOs forming the boundary between JPA entities and the API, avoiding direct entity exposure and JSON serialization loops caused by bidirectional relationships:

```
Resource (Controller) → Service → Repository → Entity
```

Main package structure:

- `entities` — Doctor, Patient, Consultation, Specialty, MedicalSchedule, Prescription, PrescriptionItem, Medications, HistoryPatient, Administrator
- `entities.enums` — StatusConsultation (WAITING, CONFIRMED, SCHEDULED, CARRIED_OUT, CANCELED)
- `entities.pk` — composite keys (e.g. `PrescriptionPk`, via `@Embeddable`)
- `dto` — DTOs per entity
- `repository` — Spring Data JPA interfaces
- `service` — business rules
- `service.exception` — domain exceptions (`ResourceNotFoundException`, `DataBaseException`, `ScheduleConflictException`)
- `resource.exception` — global exception handling (`@ControllerAdvice`) and standardized error response (`StandardError`)

## Implemented features

- Full CRUD for: Doctors, Patients, Specialties, Medical schedules, Consultations, Prescriptions, Prescription items, Medications, and Patient history
- Schedule conflict validation per doctor when booking a consultation (`hasScheduleConflict`), throwing `ScheduleConflictException` on overlap
- JPA relationships modeled between entities:
  - Doctor ↔ Specialty (many-to-many)
  - Doctor ↔ Consultation (one-to-many)
  - Prescription ↔ PrescriptionItem (composite-key relationship)
  - Consultation ↔ HistoryPatient (one-to-one)
- Global, centralized exception handling via `@ControllerAdvice`, returning standardized responses with timestamp, HTTP status, error type, message, and request path

## Future improvements

Two features were intentionally left out of this version, to be developed later:

- Referral of consultations/patients between doctors
- Reception / administrator module for the clinic's administrative management

## How to run

1. Clone the repository
2. Configure the PostgreSQL database connection in `application.properties` (or `application.yml`)
3. Run with Maven:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Author

Project developed by Shelton Chambule, Computer Programming student, as a learning exercise in backend development with Java and Spring Boot.

---

# Sistema de Agendamento de Consultas Médicas

*[Read in English above](#medical-appointment-scheduling-system)*

API REST para gestão de agendamento de consultas numa clínica médica, desenvolvida como projeto de aprendizagem para consolidar conceitos de arquitetura em camadas, JPA/Hibernate e boas práticas de construção de APIs REST com Spring Boot.

## Tecnologias

- **Java 21**
- **Spring Boot 4.1.0**
- **Spring Data JPA / Hibernate**
- **Spring Web** — construção da API REST
- **PostgreSQL** — base de dados relacional
- **Maven** — gestão de dependências e build
- **Lombok** — redução de boilerplate (`@Getter`, `@Setter`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`)
- **Jakarta Persistence (JPA)**
- **StarUML** — modelação do diagrama de classes do domínio, antes da implementação

## Arquitetura

O projeto segue uma arquitetura em camadas, com DTOs a fazer a fronteira entre as entidades JPA e a API, evitando exposição direta das entidades e loops de serialização JSON causados pelas relações bidirecionais:

```
Resource (Controller) → Service → Repository → Entity
```

Estrutura de pacotes principal:

- `entities` — Doctor, Patient, Consultation, Specialty, MedicalSchedule, Prescription, PrescriptionItem, Medications, HistoryPatient, Administrator
- `entities.enums` — StatusConsultation (WAITING, CONFIRMED, SCHEDULED, CARRIED_OUT, CANCELED)
- `entities.pk` — chaves compostas (ex.: `PrescriptionPk`, via `@Embeddable`)
- `dto` — DTOs por entidade
- `repository` — interfaces Spring Data JPA
- `service` — regras de negócio
- `service.exception` — exceções de domínio (`ResourceNotFoundException`, `DataBaseException`, `ScheduleConflictException`)
- `resource.exception` — tratamento global de exceções (`@ControllerAdvice`) e resposta padronizada de erro (`StandardError`)

## Funcionalidades implementadas

- CRUD completo para: Médicos, Pacientes, Especialidades, Horários médicos, Consultas, Receitas, Itens de receita, Medicamentos e Histórico de paciente
- Validação de conflito de horários por médico ao agendar uma consulta (`hasScheduleConflict`), lançando `ScheduleConflictException` em caso de sobreposição
- Relações JPA modeladas entre entidades:
  - Doctor ↔ Specialty (muitos-para-muitos)
  - Doctor ↔ Consultation (um-para-muitos)
  - Prescription ↔ PrescriptionItem (relação via chave composta)
  - Consultation ↔ HistoryPatient (um-para-um)
- Tratamento global e centralizado de exceções via `@ControllerAdvice`, devolvendo respostas padronizadas com timestamp, status HTTP, tipo de erro, mensagem e path do pedido

## Melhorias futuras

Duas funcionalidades ficaram propositadamente de fora desta versão, para serem desenvolvidas mais tarde:

- Encaminhamento de consultas/pacientes entre médicos
- Módulo de receção / administrador para gestão administrativa da clínica

## Como executar

1. Clonar o repositório
2. Configurar a ligação à base de dados PostgreSQL em `application.properties` (ou `application.yml`)
3. Executar com Maven:

```bash
mvn spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

## Autor

Projeto desenvolvido por Shelton Chambule, estudante de Programação Informática, como exercício de aprendizagem em desenvolvimento backend com Java e Spring Boot.

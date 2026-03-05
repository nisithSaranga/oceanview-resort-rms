# OceanView Resort Reservation Management System (CIS6003)

A Java EE (Servlet-based) web application for managing resort reservations, preventing booking conflicts, and generating invoices.
Scope is internal staff/admin usage (not a guest self-service portal).

### Tech Stack:

* Java (JDK 17)
* Tomcat 9 (Servlet javax.\*)
* Maven (WAR packaging)
* MySQL
* Jackson (JSON)
* JUnit 5 (tests)

#### Build & Deploy

* Build the WAR
```
mvn clean package
```
* Deploy to Tomcat:
```
Copy: target/oceanview-resort-rms.war
To: <TOMCAT_HOME>/webapps/
```
* Start Tomcat:
```
Run: <TOMCAT_HOME>/bin/startup.bat
```
* Base URL:
```
http://localhost:8080/oceanview-resort-rms/
```
#### Data Access (DAO + Factory + DBConnectionManager)

This project uses a simple 3-tier layered architecture:

- Presentation: Servlets (HTTP/JSON)
- Business: Controllers + Services (use case orchestration)
- Persistence: DAO interfaces + JDBC DAO implementations

**Key packages**
- presentation.servlet/  (HTTP endpoints)
- controller/            (use-case orchestration)
- service/ + service.impl/ (business logic)
- dao/ + dao.impl/       (JDBC persistence)
- factory/               (DAOFactory)
- config/                (DBConnectionManager)
- dto/                   (request/response models)
- mapper/                (Entity <-> DTO)
- strategy/              (PricingStrategy implementations)

**How persistence is wired**
- Services depend only on DAO interfaces* (e.g., SystemUserDAO) — not on JDBC classes.
- Concrete JDBC implementations (e.g., SystemUserDAOImpl) are obtained via DAOFactory.
- Each DAO implementation uses the *DBConnectionManager Singleton* to open JDBC connections.

**DBConnectionManager (Singleton)**

DBConnectionManager reads DB settings from src/main/resources/db.properties (local-only, ignored by Git) and provides Connection instances for DAOs.

> db.properties is intentionally not committed. Use db.properties.example as a template and create your own local db.properties.

**Design Patterns Implemented**
* Singleton: DBConnectionManager (central DB config + connection creation)
* Factory Method / Simple Factory: DAOFactory (returns DAO interfaces backed by DAOImpl)
* Builder: ReservationRequestDTO.Builder (immutable request construction)
* Strategy: PricingStrategy (pricing calculation by room type)

### Endpoints

**Auth**
* POST /login (JSON)
* POST /logout (JSON)

**Reservations**
* POST /reservations
* GET /reservations?reservationNo=...
* PUT /reservations (update/relocate/cancel)
* DELETE /reservations?reservationNo=...

**Billing**
* POST /billing (generate invoice)
* GET /billing?reservationNo=... (fetch invoice)

**Help**
* GET /help (health + route list)

**Database setup**
1. Create a MySQL database (example): oceanview_resort
2. Run SQL scripts:
    - 'database/schema.sql'
    - 'database/data.sql'
3. Local DB config (NOT committed)
    - Copy: 'src/main/resources/
      db.properties.example'
    - To: 'src/main/resources/
      db.properties'
    - Fill in your own credentials.

#### Smoke Tests

See the markdown files under tools/http/ for ready-to-run commands:
- Auth: SMOKE_TESTS.md
- Reservations: SMOKE_TESTS-reservation.md
- Billing: SMOKE_TESTS-billing.md

**CI/CD**

GitHub Actions workflow: .github/workflows/ci.yml runs mvn clean test on pushes to main and via manual trigger.

**Expected keys in db.properties**
```properties
db.url=jdbc:mysql://localhost:3306/oceanview_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=CHANGE_ME
db.password=CHANGE_ME
```

#### Common Issues
- 404: WAR not deployed or wrong context path (expected: /oceanview-resort-rms)
- DB errors: create src/main/resources/db.properties and start MySQL
- 400 Invalid JSON: send Content-Type: application/json
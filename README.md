# OceanView Resort Reservation Management System (CIS6003)

A Java EE (Servlet-based) web application for managing resort reservations, preventing booking conflicts, and generating invoices.
Scope is internal staff/admin usage (not a guest self-service portal).

**Tech Stack**
Java (JDK 17)
Tomcat 9 (Servlet javax.\*)
Maven (WAR packaging)
MySQL
Jackson (JSON)
JUnit 5 (tests)

**Architecture**
3-tier layered structure:
 Presentation: Servlets + JSON
 Business: Services + Controllers (orchestrate use cases)
 Persistence: DAO interfaces + DAO implementations (JDBC)

**Design Patterns Implemented**
Singleton: DBConnectionManager (central DB config + connection creation)
Factory Method / Simple Factory: DAOFactory (returns DAO interfaces backed by DAOImpl)
Builder: ReservationRequestDTO.Builder (immutable request construction)
Strategy: PricingStrategy (pricing calculation by room type)

**Database Setup**
1.Create a MySQL database (example):
 - oceanview_resort
2. Run SQL scripts:
 - database/schema.sql
 - database/data.sql
3. Configure DB settings:
 - src/main/resources/db.properties

**Expected keys in db.properties:**
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/oceanview\_resort\_rms?useSSL=false\&allowPublicKeyRetrieval=true\&serverTimezone=UTC 
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD

Build
From the project root:
bash
mvn clean package


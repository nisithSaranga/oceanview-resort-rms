# OceanView Resort Reservation Management System (CIS6003)

A Java EE (Servlet-based) web application for managing resort reservations, preventing booking conflicts, and generating invoices.
Scope is internal staff/admin usage (not a guest self-service portal).

**Tech Stack:**
Java (JDK 17)
Tomcat 9 (Servlet javax.\*)
Maven (WAR packaging)
MySQL
Jackson (JSON)
JUnit 5 (tests)

**Architecture:**
3-tier layered structure:
* Presentation: Servlets + JSON
* Business: Services + Controllers (orchestrate use cases)
* Persistence: DAO interfaces + DAO implementations (JDBC)

**Design Patterns Implemented**
* Singleton: DBConnectionManager (central DB config + connection creation)
* Factory Method / Simple Factory: DAOFactory (returns DAO interfaces backed by DAOImpl)
* Builder: ReservationRequestDTO.Builder (immutable request construction)
* Strategy: PricingStrategy (pricing calculation by room type)

**Database setup**
1. Create a MySQL database (example): oceanview_resort
2. Run SQL scripts:
   - 'database/schema.sql'
   - 'database/data.sql'
3. Local DB config (NOT committed)
  - Copy the template file:
   - 'src/main/resources/'
db.properties.example
  - Create your local file (same folder):
   - 'src/main/resources/db.properties'
  - Fill in your own credentials.

 **Expected keys in db.properties**
```properties
-db.driver=com.mysql.cj.jdbc.Driver
-db.url=jdbc:mysql://localhost:3306/oceanview_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
-db.username=CHANGE_ME
-db.password=CHANGE_ME

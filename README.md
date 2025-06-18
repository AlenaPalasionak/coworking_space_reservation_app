# Coworking Space Reservation

🧾 A modular Java-based rest application for managing coworking space reservations with three interchangeable Repository
implementations:

- **File-based (Jackson)**
- **JDBC**
- **JPA (Hibernate)**

### Technologies:

#### Programming Language: Java

#### Build Tool: Maven

#### Frameworks & Libraries: Spring (MVC, Core, Context, ORM, Annotations), Hibernate, JPA, Lombok, Jackson

#### Testing: JUnit, Mockito, AssertJ

#### Security & Authentication: JBCrypt

#### RDBMS: PostgreSQL

#### Data Formats: JSON (for serialization/deserialization), XML (log4j.xml configuration)

## 📁 Repository Implementations & Setup Instructions

### 1️⃣ File-based (Jackson)

**Configuration Steps:**

1. To switch between implementations change Qualifiers (in constructors) in service layer for CoworkingServiceImpl,
   ReservationServiceImpl, UserServiceImpl:

```
@Qualifier("fileUserRepository")
```

```
@Qualifier("fileReservationRepository")
```

```
@Qualifier("fileCoworkingRepository")
```

### 2️⃣ JDBC

Configuration Steps:

1. To switch between implementations change Qualifiers (in constructors) in service layer for CoworkingServiceImpl,
   ReservationServiceImpl, UserServiceImpl:

```
@Qualifier("jdbcUserRepository")
```

```
@Qualifier("jdbcReservationRepository")
```

```
@Qualifier("jdbcCoworkingRepository")
```

### 3️⃣ JPA (Hibernate)

Configuration Steps:

1. To switch between implementations change Qualifiers (in constructors) in service layer for CoworkingServiceImpl,
   ReservationServiceImpl, UserServiceImpl:

```
@Qualifier("jpaUserRepository")
```

```
@Qualifier("jpaReservationRepository")
 ```

```
@Qualifier("jpaCoworkingRepository")
 ```

###  Common Configuration Steps and Running the application

1. Create a PostgreSQL database named:

```sh
coworking_reservation_app
```

2. Create `application.properties` for JDBC realization in `domain/resources` with the following content (replace `***` with actual values):

```properties
# === DB Credentials ===
datasource.url=***
datasource.username=***
datasource.password=***
# === File ===
user.path=users.json
coworking.places.path=coworking_places.json
reservation.path=reservations.json
# === JDBC ===
datasource.driver-class-name=org.postgresql.Driver
# === Hibernate ===
jpa.hibernate.ddl-auto=validate
jpa.show-sql=true
# === HikariCP ===
hikari.minimumIdle=1
hikari.maximumPoolSize=10
hikari.idleTimeout=10000
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

3. Inside domain/resources, add a META-INF directory for JPA realization:

```sh
META-INF
```

4. Create a persistence.xml file in META-INF with the following content (replace with your DB credentials):

```sh
persistence.xml
```

content of persistence.xml (replace `***` with actual values):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0" xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    <persistence-unit name="coworking-space-reservation_db">
        <class>org.example.coworking.entity.User</class>
        <class>org.example.coworking.entity.Admin</class>
        <class>org.example.coworking.entity.Customer</class>
        <class>org.example.coworking.entity.CoworkingSpace</class>
        <class>org.example.coworking.entity.Facility</class>
        <class>org.example.coworking.entity.Reservation</class>
        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="***"/>
            <property name="jakarta.persistence.jdbc.user" value="***"/>
            <property name="jakarta.persistence.jdbc.password" value="***"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.connection.provider_class"
                      value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider"/>
            <property name="hibernate.hikari.minimumIdle" value="2"/>
            <property name="hibernate.hikari.maximumPoolSize" value="5"/>
            <property name="hibernate.hikari.idleTimeout" value="10000"/>
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
        </properties>
    </persistence-unit>
</persistence>
```

5. Run the schema.sql file from domain/resources to initialize database tables.

### 3️⃣ Running the application

1. Build the project:

 ```sh
mvn clean install
```

2. Create web app archive in project structure.
3. Add tomcat local server (edit configuration). Add archive into it.
4. Copy war, coworking_places.json, reservations.json, users.json and place into webapps folder in TomCat.
5. Run the application using TomCat.

### List of endpoints:

🏢 GET all Coworkings -
http://localhost:8080/coworking_space_reservation_1/api/coworking-spaces

📭 POST add a Coworking
http://localhost:8080/coworking_space_reservation_1/api/coworking-spaces

##### body:

 ```json
{
  "adminId": 1,
  "price": 44.0,
  "coworkingType": "CO_LIVING",
  "facilities": [
    "PRINTER",
    "CONDITIONING"
  ]
}
 ```

❌ DELETE a Coworking
http://localhost:8080/coworking_space_reservation_1/api/coworking-spaces/*?adminId=1

 ```
 instead of * - coworking id
 instead of 1 - admin id
  ```

📎 POST Reservation
http://localhost:8080/coworking_space_reservation_1/api/reservations

##### body:

```json
{
  "customerId": 2,
  "adminId": 1,
  "startTime": "2029-12-01 01:01",
  "endTime": "2031-12-02 01:01",
  "coworkingSpaceId": 26
}
```

❌ DELETE Reservation
http://localhost:8080/coworking_space_reservation_1/api/reservations/*?customerId=2

```
 instead of * - reservation id
 instead of 2 - customer id
  ```

🗒 GET Reservations by adminId
http://localhost:8080/coworking_space_reservation_1/api/reservations/admin?adminId=1

```
 instead of 1 - admin id
  ```

📙 GET Reservations by customerId
http://localhost:8080/coworking_space_reservation_1/api/reservations/customer?customerId=2

```
 instead of 2 - customer id
  ```
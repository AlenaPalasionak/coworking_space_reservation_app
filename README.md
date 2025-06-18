# Coworking Space Reservation

🧾 A modular Java-based application for managing coworking space reservations with three interchangeable Repository
implementations:

- **File-based (Jackson)**
- **JDBC**
- **JPA (Hibernate)**

### Technologies:

#### Programming Language: Java

#### Build Tool: Maven

#### Frameworks & Libraries: Spring (Core, Context, ORM, Annotations), Hibernate, JPA, Lombok, Jackson, Log4j

#### Testing: JUnit, Mockito, AssertJ

#### Security & Authentication: JBCrypt

#### RDBMS: PostgreSQL

#### Data Formats: JSON (for serialization/deserialization), XML (log4j.xml configuration)

## Repository Implementations & Setup Instructions

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

2. Create a PostgreSQL database named:

```
coworking_reservation_app
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

### Common Configuration Steps and Running the application

1. Create `application.properties` in `domain/resources` with the following content (replace `***` with actual values):

```properties
# === DB Credentials ===
datasource.url=***
datasource.username=***
datasource.password=***
# === File ===
menu.path=menu.json
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

2. Create a PostgreSQL database named:

```
coworking_reservation_app
```

3. Inside domain/resources, add a META-INF directory:

```
META-INF
```

4. Create a persistence.xml file in META-INF with the following content (replace with your DB credentials):

```
persistence.xml
```

contents of persistence.xml (replace `***` with actual values):

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
            <property name="jakarta.persistence.jdbc.url" value="***"/>
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

## Build and run the application

1. Build the project:

```sh
mvn clean install
```

2. You can run the app using development environment or terminal.

- Development environment: edit configurations -> set working directory: ui.target -> run Main.
- Terminal: navigate to the target directory:

```
cd ui/target
```

run the application:

```
java -"Djson.coworking="coworking_places.json,reservations.json"" -"Dlog4j.configurationFile=log4j2.xml" -jar ui-1.0-SNAPSHOT.jar
```


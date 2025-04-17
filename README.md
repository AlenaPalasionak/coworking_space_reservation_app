# 🧾 Coworking Space Reservation

A Java-based application for managing coworking space reservations with three interchangeable DAO implementations:

- **File-based (Jackson)**
- **JDBC**
- **JPA (Hibernate)**

To switch between implementations change Qualifiers in service layer 
in CoworkingServiceImpl, ReservationServiceImpl, UserServiceImpl in constructors.

- **File-based (Jackson)**

```sh
@Qualifier("fileUserDao")
```
```sh
@Qualifier("fileReservationDao")
```
```sh
@Qualifier("fileCoworkingDao")
```

- **JDBC** 
```sh
@Qualifier("jdbcUserDao")
```
```sh
@Qualifier("jdbcReservationDao")
```
```sh
@Qualifier("jdbcCoworkingDao")
```

- **JPA (Hibernate)**
```sh
@Qualifier("jpaUserDao")
```
```sh
@Qualifier("jpaReservationDao")
 ```
```sh
@Qualifier("jpaCoworkingDao")
 ```
---

## 📁 DAO Implementations & Setup Instructions
### 1️⃣ File-based (Jackson)
**Configuration Steps:**
1. Create `application.properties` in `domain/resources` with the following content (replace `***` with actual values):
```properties
menu.path=menu.json
user.path=users.json
coworking.places.path=coworking_places.json
reservation.path=reservations.json
```
2. Build the project:
```sh
mvn clean install
```
3. Navigate to the target directory:
```sh
cd ui/target
```
4. Run the application:
```sh
java -"Djson.coworking="coworking_places.json,reservations.json"" -"Dlog4j.configurationFile=log4j2.xml" -jar ui-1.0-SNAPSHOT.jar
```

### 2️⃣ JDBC
Configuration Steps:
1. Create a PostgreSQL database named:
```sh
coworking_reservation_app
```
2. Add application.properties to domain/resources (same content as above).
3. Run the schema.sql script located in domain/resources to create the required tables in the database.
4. Build the project:
 ```sh
mvn clean install
```
5. Navigate to the target directory:
```sh
cd ui/target
```
6. Run the application:
```sh
java -"Djson.coworking="coworking_places.json,reservations.json"" -"Dlog4j.configurationFile=log4j2.xml" -jar ui-1.0-SNAPSHOT.jar

```

### 3️⃣ JPA (Hibernate)
Configuration Steps:
1. Create a PostgreSQL database named:
```sh
coworking_reservation_app
```
2. Inside domain/resources, add a META-INF directory:
```sh
META-INF
```
3. Create a persistence.xml file in META-INF with the following content (replace with your DB credentials):
```sh
persistence.xml
```
content of persistence.xml (replace `***` with actual values):
```sh
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0" xmlns="https://jakarta.ee/xml/ns/persistence"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
<persistence-unit name="coworking-space-reservation_db">
<class>org.example.coworking.model.User</class>
<class>org.example.coworking.model.Admin</class>
<class>org.example.coworking.model.Customer</class>
<class>org.example.coworking.model.CoworkingSpace</class>
<class>org.example.coworking.model.Facility</class>
<class>org.example.coworking.model.Reservation</class>
<properties>
<property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="***"/>
<property name="jakarta.persistence.jdbc.user" value="***"/>
<property name="jakarta.persistence.jdbc.password" value="***"/>
<property name="hibernate.format_sql" value="true"/>
<property name="hibernate.connection.provider_class" value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider"/>
<property name="hibernate.hikari.minimumIdle" value="2"/>
<property name="hibernate.hikari.maximumPoolSize" value="5"/>
<property name="hibernate.hikari.idleTimeout" value="10000"/>
<property name="hibernate.hbm2ddl.auto" value="validate"/>
</properties>
</persistence-unit>
</persistence>
```
4. Run the schema.sql file from domain/resources to initialize database tables.
5. Build the project:
 ```sh
mvn clean install
```
6. Navigate to the target directory:
```sh
cd ui/target
```
7. Run the application:
```sh
java -"Djson.coworking="coworking_places.json,reservations.json"" -"Dlog4j.configurationFile=log4j2.xml" -jar ui-1.0-SNAPSHOT.jar
```

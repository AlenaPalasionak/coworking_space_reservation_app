# 🧾 Coworking Space Reservation

A Java-based application for managing coworking space reservations with three interchangeable DAO implementations:

- **File-based (Jackson)**
- **JDBC**
- **JPA (Hibernate)**

To switch between implementations change Qualifiers in service layer 
in CoworkingServiceImpl, ReservationServiceImpl, UserServiceImpl in constructors.

- **File-based (Jackson)**

```sh
@Qualifier("fileUserRepository")
```
```sh
@Qualifier("fileReservationRepository")
```
```sh
@Qualifier("fileCoworkingRepository")
```

- **JDBC** 
```sh
@Qualifier("jdbcUserRepository")
```
```sh
@Qualifier("jdbcReservationRepository")
```
```sh
@Qualifier("jdbcCoworkingRepository")
```

- **JPA (Hibernate)**
```sh
@Qualifier("jpaUserRepository")
```
```sh
@Qualifier("jpaReservationRepository")
 ```
```sh
@Qualifier("jpaCoworkingRepository")
 ```
---

## 📁 DAO Implementations & Setup Instructions
### 1️⃣ File-based (Jackson)
**Configuration Steps:**
1. Create `application.properties` in `domain/resources` with the following content:
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
### 3️⃣  Running the application
1. Copy war from target directory:
```sh
cd ui/target
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
 ```sh
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
 ```sh
 instead of * - coworking id
 instead of 1 - admin id
  ```
✅ POST authorization
http://localhost:8080/coworking_space_reservation_1/auth/login
##### body:
```sh
{
"username": "c",
"password": "3",
"role": "CUSTOMER"
}
```
📎 POST Reservation
http://localhost:8080/coworking_space_reservation_1/api/reservations
##### body:
```sh
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
```sh
 instead of * - reservation id
 instead of 2 - customer id
  ```
🗒 GET Reservations by adminId
http://localhost:8080/coworking_space_reservation_1/api/reservations/admin?adminId=1
```sh
 instead of 1 - admin id
  ```

📙 GET Reservations by customerId
http://localhost:8080/coworking_space_reservation_1/api/reservations/customer?customerId=2
```sh
 instead of 2 - customer id
  ```
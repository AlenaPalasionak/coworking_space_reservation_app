# 🧾 Coworking Space Reservation

A Java-based application for managing coworking space reservations with three interchangeable DAO implementations:

Add credentials in the file application.properties located in ui\src\main\resources:
(replace * with your data)
 ```sh
# === DB Credentials ===
spring.datasource.url=*
spring.datasource.username=*
spring.datasource.password=*
# === JDBC ===
spring.datasource.driver-class-name=org.postgresql.Driver
# === Hibernate ===
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
 ```

### List of endpoints:

🏢 GET all Coworking Spaces
http://localhost:8080/api/coworking-spaces

📭 POST add a Coworking
http://localhost:8080/api/coworking-spaces
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
http://localhost:8080/api/coworking-spaces/*?adminId=1
 ```sh
 instead of * - coworking id
 instead of 1 - admin id
  ```
✅ POST authorization
http://localhost:8080/auth/login
##### body:
```sh
{
"username": "c",
"password": "3",
"role": "CUSTOMER"
}
```
📎 POST Reservation
http://localhost:8080/api/reservations
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
http://localhost:8080/api/reservations/*?customerId=2
```sh
 instead of * - reservation id
 instead of 2 - customer id
  ```
🗒 GET Reservations by adminId
http://localhost:8080/api/reservations/admin?adminId=1
```sh
 instead of 1 - admin id
  ```

📙 GET Reservations by customerId
http://localhost:8080/api/reservations/customer?customerId=2
```sh
 instead of 2 - customer id
  ```
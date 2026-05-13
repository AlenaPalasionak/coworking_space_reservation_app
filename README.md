# Coworking Space Reservation

A modular Java-based Spring Boot REST application for managing coworking space reservations:

### Technologies:

#### Programming Language: Java 19

#### Build Tool: Maven

#### Frameworks & Libraries: Spring Boot, Hibernate, JPA, Lombok

#### Security & Authentication: Spring Security

#### RDBMS: PostgreSQL

#### Containerization: Docker

#### CI: GitHub Actions

### Project Architecture

The project runs fully in Docker:

#### Spring Boot application container

#### PostgreSQL database container

### How to Run the Project

#### 1. Clone repository

```
git clone
```

#### 2. Create an .env environment file with the following data (You can also copy it from .env.example in the project root):

```
POSTGRES_DB=coworking_reservation_app
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

#### 3. Run application

 ```
 docker-compose up --build
 ```

### API Endpoints:

GET all Coworking Spaces
http://localhost:8080/api/coworking-spaces

POST add a Coworking
http://localhost:8080/api/coworking-spaces

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

DELETE a Coworking
http://localhost:8080/api/coworking-spaces/*?adminId=1

 ```
 instead of * - coworking id
 instead of 1 - admin id
  ```

POST User Registration
http://localhost:8080/api/auth/register

##### body:

```json
{
  "username": "c",
  "password": "3",
  "role": "CUSTOMER"
}
```

POST Reservation
http://localhost:8080/api/reservations

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

DELETE Reservation
http://localhost:8080/api/reservations/*?customerId=2

```
 instead of * - reservation id
 instead of 2 - customer id
  ```

GET Reservations by adminId
http://localhost:8080/api/reservations/admin?adminId=1

```
 instead of 1 - admin id
  ```

GET Reservations by customerId
http://localhost:8080/api/reservations/customer?customerId=2

```
 instead of 2 - customer id
  ```
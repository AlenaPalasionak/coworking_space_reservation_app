# Coworking Space Reservation

## Getting Started

### 1. Create a PostgreSQL database named:

```sh
coworking_reservation_app
```

### 2. Add file to resources package in domain module:

```sh
application.properties
```

### 3. The file application.properties should contain (enter your data):

```sh
database.url=jdbc:postgresql://localhost:5432/coworking_reservation_app
database.username=your_username
database.password=your_password
```

### 4. Run scheme.sql located in the domain module in the resources package.

## Run the project in Terminal

### 1. Build the project

```sh
mvn clean install
```

### 3. Navigate to the target directory

```sh
cd ui/target
```

### 4. Run the application

```sh
java -"Djson.coworking="coworking_places.json,reservations.json"" -"Dlog4j.configurationFile=log4j2.xml" -jar ui-1.0-SNAPSHOT.jar
```




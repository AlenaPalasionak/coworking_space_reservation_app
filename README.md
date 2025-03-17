# Coworking Space Reservation

## Getting Started

### 1. Clone the project

```sh
git clone https://github.com/AlenaPalasionak/coworking_space_reservation_app.git
cd coworking_space_reservation_app
git checkout feature-branch-6
```

### 2. Build the project

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




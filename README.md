# Coworking Space Reservation

## Getting Started
## JPA implementation

### 1. Create a PostgreSQL database named:

```sh
coworking_reservation_app
```

### 2. Add directory to resources package in domain module:

```sh
META-INF
```
### 3. Create the file persistence.xml in META-INF directory. It should contain (enter your data):

```sh
<property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="Your db url"/>
            <property name="jakarta.persistence.jdbc.user" value="Your user name"/>
            <property name="jakarta.persistence.jdbc.password" value="Your password"/>

            <property name="hibernate.format_sql" value="true"/>

            <property name="hibernate.connection.provider_class" value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider"/>
            <property name="hibernate.hikari.minimumIdle" value="5"/>
            <property name="hibernate.hikari.maximumPoolSize" value="10"/>
            <property name="hibernate.hikari.idleTimeout" value="30000"/>

            <property name="hibernate.hbm2ddl.auto" value="update"/>
```

### 4. Run scheme.sql located in the domain module in the resources package to create a table in DB.

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




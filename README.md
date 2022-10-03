# Word Learning App

## About the project

REST API created with Spring Boot and backed by SQL Server database. Enables creating, modifying, retrieving and
deleting learning sets and learning set items. User authentication is performed using Json Web Tokens passed
in `Authorization` header. Users have a specific role for each learning set; the role is checked at the service level.

## Technologies used

- Spring Boot 2.7.1
- Spring Data JPA 2.7.1
- Spring Security 5.7.2
- SQL Server database (also with support for embedded H2 database)

## Spring profiles

Starting the app without explicitly enabling any profiles will use the default configuration, that is, the app will try
to establish connection to MSSQL database.

There are 3 additional profiles defined:

- `swagger` - enables SwaggerUI
- `dev` - prints SQL queries generated by Hibernate
- `h2` - uses embedded H2 database instead of MSSQL

To enable any of those profiles you can either:

1. Set the value of `SPRING_PROFILES_ACTIVE` environmental variable with a comma separated list of active profiles and
   then run the jar file. For example:

    ```bash
    export SPRING_PROFILES_ACTIVE=swagger,dev
    java -jar target/wordapp*.jar
    ```

2. Run jar file with option `-Dspring.profiles.active`. For example:

    ```bash
    java -jar -Dspring.profiles.active=h2,swagger target/wordap*.jar`
    ```

If starting the app with IntelliJ IDEA, you can specify active profiles by modifying run configuration.

## .env file

For application to run you have to create a `.env` file and supply values for all variables listed
in [`.env.example`](.env.example).

### Description of required environment variables

| Name                     | Data type | Description                                                                            |
|--------------------------|-----------|----------------------------------------------------------------------------------------|
| `DB_HOST`                | string    | Used in database connection string. In most cases this will be set to `localhost`      |
| `DB_USERNAME`            | string    | Database user                                                                          |
| `DB_PASSWORD`            | string    | Password to connect to db                                                              |
| `POPULATE_DB`            | bool      | Flag indicating whether to execute `data.sql` when using database in container         |
| `SPRING_PROFILES_ACTIVE` | string[]  | Set active profiles when running in container. See [Spring profiles](#spring-profiles) |
| `JWT_SECRET`             | string    | Secret value used for signing JWTs                                                     |
| `JWT_EXPIRATION_TIME`    | integer   | Specifies JWTs' validity (in seconds)                                                  |

## Roles

In order to read contents of public sets, users do not need any roles. However, to access contents of private sets or to
perform any operation a specific role is required in this set. The roles with resulting authorities are listed in the
following table:

| Role      | Can read (applies only to private sets) | Can propose changes | Can edit and approve/reject changes |  Can manage roles  |
|-----------|:---------------------------------------:|:-------------------:|:-----------------------------------:|:------------------:|
| `READER`  |           :heavy_check_mark:            |                     |                                     |                    |
| `PROPSER` |           :heavy_check_mark:            | :heavy_check_mark:  |                                     |                    |
| `EDITOR`  |           :heavy_check_mark:            | :heavy_check_mark:  |         :heavy_check_mark:          |                    |
| `OWNER`   |           :heavy_check_mark:            | :heavy_check_mark:  |         :heavy_check_mark:          | :heavy_check_mark: |

## Using SwaggerUI

If `swagger` profile was enabled, we can access SwaggerUI by navigating
to [localhost:8080/swagger-ui/index.html](localhost:8080/swagger-ui/index.html). In order to authenticate one needs to
send a request to `/login` with valid credentials. If database was populated with sample data, passwords can be found in
the comments in [data.sql](database/data.sql) file. If database wasn't populated with data, one has to register first.
After successful authentication there should be an `authorization` header, of the form  
`authorization: Bearer {TOKEN}`,  
in the response. Copy the `TOKEN` and paste it into *Available authorizations* modal dialog that appears after clicking
on green *Authorize* button. Swagger will then add appropriate header when accessing endpoints with closed black
padlock (next to arrow pointing down).
**BE CAREFUL NOT COPY ANY TRAILING OR PRECEDING SPACES**

## Running MSSQL database with Docker/Podman

**Note:** Database may take up to 60s to start and create tables.

```bash
# build the image
podman build -t db ./database/

# start the container:
podman run -d -p 1433:1433 --env-file .env --name wordapp-db db
```

```bash
# stop container
podman stop wordapp-db

# remove container
podman container rm wordapp-db
```

## Running the app with Docker/Podman

```bash
# build jar file
mvn clean package # if maven is installed

./mvnw clean package

# build the image
podman build -t app .
```

```bash
# start the container
podman run -d -p 8080:8080 --env-file .env --name wordapp app
```

If database is running in container use the following command:

```bash
podman run -d -p 8080:8080 --env-file .env -e SPRING_PROFILES_ACTIVE=swagger --name wordapp --network=host app
```

```bash
# stop the container
podman stop wordapp

# remove the container
podman container rm wordapp
```

## Running using docker-compose

```bash
# build
podman-compose build

# start
podman-compose up -d

```

```bash
# stop
podman-compose down
```

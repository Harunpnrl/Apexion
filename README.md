# Apexion

Apexion is a fitness and workout management application built with Spring Boot. It provides exercise discovery, muscle information, custom workout management, user profiles, favorites, and nutrition calculators.

## Features

- User registration and login with JWT authentication
- Exercise library with filtering and random exercise selection
- Muscle anatomy and muscle information pages
- Favorite exercises
- Custom workout creation, listing, and deletion
- Fitness profile management
- BMI, TDEE, macro, protein, and water-intake calculators
- Static HTML/CSS/JavaScript frontend served by Spring Boot

## Technologies

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- JWT (JJWT)
- Maven Wrapper
- Lombok
- Springdoc OpenAPI

## Requirements

- JDK 21
- MySQL 8 or a compatible MySQL server
- A database named `exercise_helper_db`

## Configuration

The application reads sensitive configuration from environment variables. Do not commit real passwords, database credentials, or JWT secrets to the repository.

Required variables:

| Variable | Description |
|---|---|
| `DB_URL` | MySQL JDBC connection URL |
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Base64-encoded JWT signing key |
| `CORS_ALLOWED_ORIGINS` | Optional comma-separated allowed frontend origins |

Example PowerShell configuration:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/apexion_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-database-password"
$env:JWT_SECRET="your-base64-jwt-secret"
$env:CORS_ALLOWED_ORIGINS="http://localhost:8080"
```

The JWT key must be a sufficiently long Base64-encoded key suitable for HS256.

## Database

The project uses a MySQL database named `apexion_db`. The schema and seed data are provided as ordered SQL scripts:

```text
database/01_setup_tables.sql
database/02_setup_muscle_info_data.sql
database/03_setup_exercise_data.sql
database/04_setup_exercise_muscle_group.sql
```

Run the scripts in this order. From the project root, using the MySQL command-line client:

```powershell
mysql -u root -p < database/01_setup_tables.sql
mysql -u root -p apexion_db < database/02_setup_muscle_info_data.sql
mysql -u root -p apexion_db < database/03_setup_exercise_data.sql
mysql -u root -p apexion_db < database/04_setup_exercise_muscle_group.sql
```

Alternatively, run them from an existing MySQL console:

```sql
SOURCE database/01_setup_tables.sql;
SOURCE database/02_setup_muscle_info_data.sql;
SOURCE database/03_setup_exercise_data.sql;
SOURCE database/04_setup_exercise_muscle_group.sql;
```

The project uses:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Therefore, the database tables and seed data must be created with the scripts above before starting the application. The application does not automatically create or migrate the schema.

## Running locally

Open a terminal in the project root and configure the environment variables first. Then start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Open the frontend through Spring Boot rather than opening the HTML files directly:

```text
http://localhost:8080/pages/Home.html
```

Login page:

```text
http://localhost:8080/pages/LoginRegister.html
```

## Testing and verification

Run the complete Maven verification lifecycle:

```powershell
.\mvnw.cmd clean verify
```

Compile without running tests:

```powershell
.\mvnw.cmd -DskipTests compile
```

The test suite includes an application context test, request validation tests, workout service tests, authentication service tests, and JWT service tests. Tests that load the full application context require the configured MySQL database and environment variables.

JaCoCo coverage is generated during `verify` at:

```text
target/site/jacoco/index.html
```

## API overview

The base API path is:

```text
/api/v1
```

### Authentication

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/auth/register` | Public |
| `POST` | `/auth/login` | Public |

Authenticated requests must send:

```http
Authorization: Bearer <jwt-token>
```

### Exercises and muscles

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/exercises` | Public |
| `GET` | `/exercises/{id}` | Public |
| `GET` | `/exercises/random?limit=3` | Public |
| `GET` | `/muscle-groups` | Public |
| `GET` | `/muscle-groups/primary` | Public |
| `GET` | `/muscle-info/{muscleName}` | Public |

### User and favorites

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/users/me` | Authenticated |
| `PUT` | `/users/me` | Authenticated |
| `GET` | `/users/me/favorites` | Authenticated |
| `POST` | `/users/me/favorites/{exerciseId}` | Authenticated |
| `DELETE` | `/users/me/favorites/{exerciseId}` | Authenticated |

### Fitness profile

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/profiles/me` | Authenticated |
| `POST` | `/profiles/me` | Authenticated |
| `PUT` | `/profiles/me` | Authenticated |

### Workouts

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/workouts/my-workouts` | Authenticated |
| `POST` | `/workouts` | Authenticated |
| `DELETE` | `/workouts/{id}` | Authenticated |

Workout requests are validated on the backend. Workout names may contain up to 100 characters, a workout must contain at least one exercise, and each exercise must have valid sets, reps, weight, and exercise ID values.

## API documentation

When enabled and accessible in the active security configuration:

```text
http://localhost:8080/swagger-ui/index.html
```

## Project structure

```text
src/main/java                 Backend source code
src/main/resources/static     Frontend pages, JavaScript, CSS, and assets
src/main/resources            Application configuration
src/test/java                 Automated tests
```

## Security notes

- Keep all credentials in environment variables or deployment secrets.
- Do not open the frontend using `file://`; serve it through Spring Boot.
- Set `CORS_ALLOWED_ORIGINS` to the real frontend domain in production.
- Use HTTPS in production.
- Review production logging settings and disable SQL logging when appropriate.

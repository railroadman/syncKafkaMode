# Kafka Sync Demo Project

This project is a Spring Boot application that demonstrates how to use Apache Kafka for inter-service communication to enrich user data with address information. It includes CRUD operations for users and addresses, leveraging Kafka for synchronous message exchange between services.

## Features

- **User and Address CRUD operations** via REST controllers.
- **Kafka Integration** to retrieve address information for users.
- **MySQL Database** for persistent data storage.
- **Object Mapping** using MapStruct to convert between entities and DTOs.
- **Optimized Kafka Consumer** for efficient handling of Kafka messages.

## Technologies Used

- **Java 17**
- **Spring Boot 3.x**
- **Apache Kafka**
- **MySQL**
- **MapStruct**
- **Lombok**
- **SpringDoc OpenAPI** (for API documentation)
- **Docker** (optional for Kafka and MySQL setup)

## Getting Started

### Prerequisites

- **Java 17**
- **Apache Maven or Gradle**
- **Docker** (optional but recommended for running Kafka and MySQL containers)

### Running Kafka and MySQL with Docker

To run Kafka and MySQL using Docker, you can use the following commands:

```sh
docker-compose up -d
```

Ensure you have a `docker-compose.yml` file configured for Kafka and MySQL.

### Application Configuration

The application requires configuration properties for Kafka and MySQL. Below is an example of `application.properties`:

```properties
spring.application.name=demo
spring.datasource.url=jdbc:mysql://localhost:3306/kafkatest
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=my-kafkatest
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.address-info-response-topic=address-info-response
spring.kafka.address-info-request-topic=address-info-request
spring.kafka.listener.ack-mode=manual

# SpringDoc OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Tomcat Configuration
server.port=9091

address.api.url=http://localhost:9091/api/addresses
logging.level.org.apache.kafka=ERROR
```

### Building and Running the Application

To build the application, use:

```sh
./mvnw clean install
```

To run the application, use:

```sh
./mvnw spring-boot:run
```

Alternatively, you can use Gradle:

```sh
./gradlew bootRun
```

### API Endpoints

The following REST endpoints are available:

#### User Endpoints

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update an existing user
- `DELETE /api/users/{id}` - Delete a user

#### Address Endpoints

- `GET /api/addresses` - Get all addresses
- `GET /api/addresses/{id}` - Get address by ID
- `POST /api/addresses` - Create a new address
- `PUT /api/addresses/{id}` - Update an existing address
- `DELETE /api/addresses/{id}` - Delete an address

### Kafka Integration

- **Producer**: The application sends a request to retrieve address information for a user using Kafka.
- **Consumer**: The consumer listens to the response topic and processes the received address data.

### How to Test

- You can use tools like **Postman** or **cURL** to test the REST endpoints.
- Kafka messages can be tested using Kafka command-line tools or by setting up a separate producer/consumer to simulate requests and responses.

### Running Tests

To run tests, use the following command:

```sh
./mvnw test
```

### Swagger UI

Swagger UI is available for API documentation at:

```
http://localhost:9091/swagger-ui.html
```

## License

This project is licensed under the MIT License.






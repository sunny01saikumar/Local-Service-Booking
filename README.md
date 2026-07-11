# LocalHub

Production-oriented Spring Boot 3.x backend for a multi-vendor local marketplace supporting product-selling businesses, service providers, and businesses that do both.

## Run Locally

```bash
docker compose up -d postgres redis
./mvnw spring-boot:run
```

Swagger UI: `http://localhost:8080/swagger-ui.html`
Health: `http://localhost:8080/actuator/health`

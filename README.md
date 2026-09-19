\# Sentinel-AI — Analyzer Service



This branch holds Analyzer-Service, the second half of Sentinel-AI — an event-driven, AI-powered incident resolution platform.



Producer-Service (which captures exceptions and publishes them to Kafka) lives on the \[`main`](https://github.com/SubhaZx/sentinel-ai/tree/main) branch. See that branch's README for the full project overview, architecture, and setup instructions.



\## What This Service Does



Analyzer-Service (port 8081) consumes exception events from Kafka and:



1\. Checks if the event was already processed (idempotency, via a unique `correlationId` constraint)

2\. Checks Redis for a cached analysis of the same stack trace (24h TTL)

3\. If not cached, sends the exception to an LLM (Groq, via Spring AI) for root-cause analysis — protected by a Resilience4j Circuit Breaker with a safe fallback

4\. Persists the result to MySQL

5\. Exposes the data via a JWT-secured, paginated REST API with ADMIN/VIEWER roles



\## Key Files



| File | Responsibility |

|---|---|

| `KafkaConsumerService.java` | Consumes events, idempotency check |

| `AIAnalysisService.java` | Redis caching, Groq call, circuit breaker |

| `AppConfig.java` | Caching, Kafka dead-letter queue config |

| `IncidentReport.java` / `IncidentRepository.java` | Persistence layer |

| `SecurityConfig.java` / `JwtService.java` / `JwtAuthenticationFilter.java` | JWT security |

| `IncidentController.java` / `AuthController.java` | REST API |



\## Running



This service is meant to run as part of the full stack via Docker Compose (see the `main` branch README). It depends on Kafka, MySQL, and Redis being available.


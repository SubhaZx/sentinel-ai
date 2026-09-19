# \# Sentinel-AI

# 

# An event-driven, AI-powered incident resolution platform. When an exception occurs anywhere in the application, it is automatically captured, analyzed by an LLM, and stored — without a single manual try-catch in business logic.

# 

# Built as two Spring Boot microservices communicating over Apache Kafka.

# 

# \## The Problem

# 

# In production systems, exceptions typically just get logged and sit there until a user complains or an engineer manually digs through logs. Sentinel-AI catches exceptions automatically and uses AI to explain what went wrong and how to fix it — before anyone has to go hunting.

# 

# \## Architecture

# 

# ```

# Exception thrown

# &#x20; → AOP intercepts it (@AfterThrowing)

# &#x20; → ErrorEvent built and published to Kafka

# &#x20; → Analyzer-Service consumes the event

# &#x20; → Idempotency check (skip if correlationId already processed)

# &#x20; → Redis cache check (skip AI call if this exact error was seen before)

# &#x20; → AI analysis via Spring AI + Groq (protected by a Resilience4j Circuit Breaker)

# &#x20; → Result persisted to MySQL

# &#x20; → Exposed via a JWT-secured, paginated REST API

# ```

# 

# \- \*\*Producer-Service\*\* (port 8080) — intercepts exceptions via Spring AOP, publishes structured events to Kafka

# \- \*\*Analyzer-Service\*\* (port 8081) — consumes events, checks Redis cache, calls an LLM for root-cause analysis, persists results, exposes a secured REST API

# 

# \## Tech Stack

# 

# | Layer | Technology |

# |---|---|

# | Language / Framework | Java 21, Spring Boot 3.5.x |

# | Messaging | Apache Kafka |

# | AI | Spring AI + Groq (OpenAI-compatible) |

# | Cache | Redis (24h TTL) |

# | Database | MySQL + JPA/Hibernate |

# | Resilience | Resilience4j (Circuit Breaker + Retry), Kafka Dead-Letter Queue |

# | Security | Spring Security + JWT, role-based access (ADMIN/VIEWER) |

# | Deployment | Docker Compose (6 containers) |

# 

# \## Key Engineering Decisions

# 

# \- \*\*Idempotency\*\* — Kafka guarantees at-least-once delivery, meaning a message can be redelivered. A unique constraint on `correlationId` plus an application-level check prevents duplicate processing.

# \- \*\*Circuit Breaker on the AI call\*\* — hit the LLM provider's rate limit during development, which caused the Kafka consumer to retry a failing call indefinitely. Wrapped the AI call in a circuit breaker to fail fast and return a safe fallback instead.

# \- \*\*Redis caching\*\* — identical exceptions are common in production. Caching the AI's analysis by a hash of the stack trace avoids redundant, costly LLM calls for repeat errors.

# \- \*\*Kafka Dead-Letter Queue\*\* — if the consumer itself fails to process a message after retries, it's routed to a dead-letter topic instead of being lost.

# 

# \## Running Locally

# 

# ```bash

# docker-compose up -d --build

# ```

# 

# This starts all six containers: Producer-Service, Analyzer-Service, Kafka, Zookeeper, MySQL, and Redis.

# 

# Trigger a test exception:

# ```

# GET http://localhost:8080/trigger-error

# ```

# 

# \## Related Repository

# 

# This repo's `main` branch holds Producer-Service. Analyzer-Service lives on the \[`analyzer`](https://github.com/SubhaZx/sentinel-ai/tree/analyzer) branch.


package com.sentinelai.producer.aspect;

import com.sentinelai.producer.kafka.KafkaProducerService;
import com.sentinelai.producer.model.ErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionAspect {

    private final KafkaProducerService kafkaProducerService;

    @AfterThrowing(
            pointcut = "execution(* com.sentinelai.producer..*(..))",
            throwing = "exception"
    )
    public void captureException(JoinPoint joinPoint, Exception exception) {

        ErrorEvent errorEvent = ErrorEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .exceptionClass(exception.getClass().getName())
                .message(exception.getMessage())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .serviceName("producer-service")
                .timestamp(LocalDateTime.now())
                .build();

        log.info("Exception captured — publishing to Kafka: {}",
                errorEvent.getCorrelationId());

        kafkaProducerService.publishErrorEvent(errorEvent);
    }
}
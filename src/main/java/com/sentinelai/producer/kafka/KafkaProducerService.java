package com.sentinelai.producer.kafka;

import com.sentinelai.producer.model.ErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "error-stream";

    private final KafkaTemplate<String, ErrorEvent> kafkaTemplate;

    public void publishErrorEvent(ErrorEvent errorEvent) {
        log.info("Publishing ErrorEvent to Kafka topic {}: {}",
                TOPIC, errorEvent.getCorrelationId());

        CompletableFuture<SendResult<String, ErrorEvent>> future =
                kafkaTemplate.send(TOPIC, errorEvent.getCorrelationId(), errorEvent);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("ErrorEvent published successfully: {} (partition={}, offset={})",
                        errorEvent.getCorrelationId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish ErrorEvent: {} — {}",
                        errorEvent.getCorrelationId(), ex.getMessage());
            }
        });
    }
}
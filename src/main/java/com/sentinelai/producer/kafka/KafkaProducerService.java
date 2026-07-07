package com.sentinelai.producer.kafka;

import com.sentinelai.producer.model.ErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "error-stream";

    private final KafkaTemplate<String, ErrorEvent> kafkaTemplate;

    public void publishErrorEvent(ErrorEvent errorEvent) {
        log.info("Publishing ErrorEvent to Kafka topic {}: {}",
                TOPIC, errorEvent.getCorrelationId());

        kafkaTemplate.send(TOPIC,
                errorEvent.getCorrelationId(),
                errorEvent);

        log.info("ErrorEvent published successfully: {}",
                errorEvent.getCorrelationId());
    }
}
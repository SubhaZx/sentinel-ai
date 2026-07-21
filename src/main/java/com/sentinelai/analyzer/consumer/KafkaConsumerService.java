package com.sentinelai.analyzer.consumer;


import com.sentinelai.analyzer.entity.IncidentReport;
import com.sentinelai.analyzer.model.ErrorEvent;
import com.sentinelai.analyzer.repository.IncidentRepository;
import com.sentinelai.analyzer.service.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final IncidentRepository incidentRepository;
    private final AIAnalysisService aiAnalysisService;

    @KafkaListener(
            topics = "error-stream",
            groupId = "analyzer-group"
    )
    public void consumeErrorEvent(ErrorEvent errorEvent) {
        log.info("Received ErrorEvent from Kafka: {}",
                errorEvent.getCorrelationId());

        String aiAnalysis = aiAnalysisService.analyzeException(
                errorEvent.getExceptionClass(),
                errorEvent.getMessage(),
                errorEvent.getStackTrace()
        );

        IncidentReport report = IncidentReport.builder()
                .correlationId(errorEvent.getCorrelationId())
                .exceptionClass(errorEvent.getExceptionClass())
                .message(errorEvent.getMessage())
                .stackTrace(errorEvent.getStackTrace())
                .serviceName(errorEvent.getServiceName())
                .timestamp(errorEvent.getTimestamp())
                .aiAnalysis(aiAnalysis)
                .build();

        incidentRepository.save(report);

        log.info("IncidentReport saved with AI analysis: {}",
                errorEvent.getCorrelationId());
    }
}
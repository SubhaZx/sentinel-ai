package com.sentinelai.producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent {

    private String correlationId;
    private String exceptionClass;
    private String message;
    private String stackTrace;
    private String serviceName;
    private LocalDateTime timestamp;
}
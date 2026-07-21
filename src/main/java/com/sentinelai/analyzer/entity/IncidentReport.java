package com.sentinelai.analyzer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correlationId;
    private String exceptionClass;
    private String message;

    @Column(columnDefinition = "TEXT")
    private String stackTrace;

    private String serviceName;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String aiAnalysis;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
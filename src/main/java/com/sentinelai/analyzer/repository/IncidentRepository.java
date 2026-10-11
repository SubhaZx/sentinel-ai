package com.sentinelai.analyzer.repository;

import com.sentinelai.analyzer.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentRepository
        extends JpaRepository<IncidentReport, Long> {

    boolean existsByCorrelationId(String correlationId);

    Optional<IncidentReport> findByCorrelationId(String correlationId);
}
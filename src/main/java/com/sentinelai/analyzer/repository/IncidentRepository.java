package com.sentinelai.analyzer.repository;

import com.sentinelai.analyzer.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository
        extends JpaRepository<IncidentReport, Long> {
}
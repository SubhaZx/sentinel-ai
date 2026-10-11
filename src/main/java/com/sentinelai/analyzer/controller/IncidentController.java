package com.sentinelai.analyzer.controller;

import com.sentinelai.analyzer.entity.IncidentReport;
import com.sentinelai.analyzer.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentRepository incidentRepository;

    @GetMapping
    public Page<IncidentReport> getAllIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return incidentRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReport> getIncidentById(@PathVariable Long id) {
        return incidentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correlation/{correlationId}")
    public ResponseEntity<IncidentReport> getByCorrelationId(@PathVariable String correlationId) {
        return incidentRepository.findByCorrelationId(correlationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        if (!incidentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        incidentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public long getIncidentCount() {
        return incidentRepository.count();
    }
}
package com.sentinelai.analyzer.controller;

import com.sentinelai.analyzer.entity.IncidentReport;
import com.sentinelai.analyzer.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public IncidentReport getIncidentById(@PathVariable Long id) {
        Optional<IncidentReport> incident = incidentRepository.findById(id);
        return incident.orElse(null);
    }

    @GetMapping("/correlation/{correlationId}")
    public IncidentReport getByCorrelationId(@PathVariable String correlationId) {
        List<IncidentReport> all = incidentRepository.findAll();
        return all.stream()
                .filter(i -> i.getCorrelationId().equals(correlationId))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteIncident(@PathVariable Long id) {
        incidentRepository.deleteById(id);
        return "Incident deleted: " + id;
    }

    @GetMapping("/count")
    public long getIncidentCount() {
        return incidentRepository.count();
    }
}
package com.appia.incidents.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.service.IncidentService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/incidents")
public class IncidentController {
    private final IncidentService svc;
    public IncidentController(IncidentService svc) { this.svc = svc; }

    @GetMapping
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    public Page<Incident> list(@RequestParam(required = false) String status,
                               @RequestParam(required = false) String prioridade,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return svc.list(status, prioridade, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    public ResponseEntity<Incident> get(@PathVariable String id) {
        return svc.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public Incident create(@RequestBody Incident i) {
        return svc.create(i);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WRITE')")
    public Incident update(@PathVariable String id, @RequestBody Incident i) {
        return svc.update(id, i);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}

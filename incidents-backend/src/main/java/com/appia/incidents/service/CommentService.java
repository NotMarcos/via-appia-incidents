package com.appia.incidents.service;

import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import com.appia.incidents.entity.Comment;
import com.appia.incidents.repository.CommentRepository;
import com.appia.incidents.entity.Incident;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository repo;

    public CommentService(CommentRepository repo) {
        this.repo = repo;
    }

    @Caching(evict = {
            @CacheEvict(value = "commentsByIncident", key = "#p0"), // <-- CORREÇÃO
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Comment create(UUID incidentId, Comment c) {

        // Garante relacionamento
        if (c.getIncident() == null) {
            var incident = new Incident();
            incident.setId(incidentId);
            c.setIncident(incident);
        }

        return repo.save(c);
    }

    @Cacheable(
            value = "commentsByIncident",
            key = "#p0"
    )
    public List<Comment> findByIncidentId(UUID incidentId) {
        return repo.findByIncident_IdOrderByDataCriacaoAsc(incidentId);
    }
}

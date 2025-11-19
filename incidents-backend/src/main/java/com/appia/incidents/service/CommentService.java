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
    private final IncidentService incidentService;

    public CommentService(CommentRepository repo, IncidentService incidentService) {
        this.repo = repo;
        this.incidentService = incidentService;
    }

    @Caching(evict = {
            @CacheEvict(value = "commentsByIncident", key = "#incidentId"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Comment create(UUID incidentId, Comment c) {

        // Carrega o incidente completo do banco
        var incident = incidentService.get(incidentId);

        // Vincula corretamente
        c.setIncident(incident);

        // Salva o comentário
        Comment saved = repo.save(c);

        // Auditoria: atualizar dataAtualizacao do incidente
        incidentService.touch(incident);

        return saved;
    }

    @Cacheable(
            value = "commentsByIncident",
            key = "#p0"
    )
    public List<Comment> findByIncidentId(UUID incidentId) {
        return repo.findByIncident_IdOrderByDataCriacaoAsc(incidentId);
    }
}

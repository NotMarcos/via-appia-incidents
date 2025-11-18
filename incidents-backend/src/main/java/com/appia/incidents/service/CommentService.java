package com.appia.incidents.service;

import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import com.appia.incidents.entity.Comment;
import com.appia.incidents.repository.CommentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository repo;

    public CommentService(CommentRepository repo) {
        this.repo = repo;
    }

    // ---------------------------------------------------
    // CREATE COMMENT
    // Ao criar um comentário, deve limpar:
    // - comentários do incidente
    // - lista de incidents (para refletir "atualizado há X tempo")
    // - stats (qtde de comments, se houver)
    // ---------------------------------------------------
    @Caching(evict = {
            @CacheEvict(value = "commentsByIncident", key = "#c.incident.id"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Comment create(Comment c) {
        return repo.save(c);
    }

    // ---------------------------------------------------
    // OBTER COMENTÁRIOS POR INCIDENTE (CACHEADO)
    // ---------------------------------------------------
    @Cacheable(
            value = "commentsByIncident",
            key = "#incidentId"
    )
    public List<Comment> findByIncidentId(UUID incidentId) {
        return repo.findByIncident_IdOrderByDataCriacaoAsc(incidentId);
    }
}

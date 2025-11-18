package com.appia.incidents.service;

import com.appia.incidents.dto.IncidentRequestDTO;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.mapper.IncidentMapper;
import com.appia.incidents.repository.IncidentRepository;
import com.appia.incidents.spec.IncidentSpecs;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository repo;
    private final IncidentMapper mapper;

    public IncidentService(IncidentRepository repo, IncidentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    // ---------------------------------------------------
    // CREATE
    // ---------------------------------------------------
    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Incident create(IncidentRequestDTO dto) {
        Incident i = mapper.toEntity(dto);
        normalizeTags(i);
        return repo.save(i);
    }

    // ---------------------------------------------------
    // LIST (CACHE POR FILTROS + PAGINAÇÃO)
    // ---------------------------------------------------
    @Cacheable(
            value = "incidents",
            key = "{#status, #prioridade, #q, #page, #size}"
    )
    public Page<Incident> list(String status, String prioridade, String q, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("dataAbertura").descending()
        );

        Specification<Incident> spec = Specification
                .where(IncidentSpecs.hasStatus(status))
                .and(IncidentSpecs.hasPrioridade(prioridade))
                .and(IncidentSpecs.hasText(q));

        return repo.findAll(spec, pageable);
    }

    // ---------------------------------------------------
    // GET (CACHE POR ID)
    // ---------------------------------------------------
    @Cacheable(
            value = "incidentById",
            key = "#root.args[0]" // correto mesmo sem debug info
    )
    public Incident get(UUID id) {
        return repo.findById(id).orElseThrow();
    }

    // ---------------------------------------------------
    // UPDATE — INVALIDA O CACHE DO INCIDENT EDITADO
    // ---------------------------------------------------
    @Caching(evict = {
            @CacheEvict(value = "incidentById", key = "#root.args[0]"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Incident update(UUID id, IncidentRequestDTO dto) {
        Incident exist = get(id);
        mapper.updateEntity(exist, dto);
        normalizeTags(exist);
        return repo.save(exist);
    }

    // ---------------------------------------------------
    // DELETE — INVALIDA TUDO RELACIONADO AO INCIDENT
    // ---------------------------------------------------
    @Caching(evict = {
            @CacheEvict(value = "incidentById", key = "#root.args[0]"),
            @CacheEvict(value = "commentsByIncident", key = "#root.args[0]"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    // ---------------------------------------------------
    // NORMALIZAÇÃO DE TAGS (DRY)
    // ---------------------------------------------------
    private void normalizeTags(Incident i) {
        if (i.getTags() == null) return;

        i.setTags(i.getTags().stream()
                .filter(t -> t != null && !t.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toSet()));
    }
}

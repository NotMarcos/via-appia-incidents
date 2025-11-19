package com.appia.incidents.service;

import com.appia.incidents.dto.IncidentRequestDTO;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.exception.BusinessException;
import com.appia.incidents.exception.NotFoundException;
import com.appia.incidents.mapper.IncidentMapper;
import com.appia.incidents.repository.IncidentRepository;
import com.appia.incidents.spec.IncidentSpecs;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.Instant;

import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository repo;
    private final IncidentMapper mapper;

    public IncidentService(IncidentRepository repo, IncidentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Incident create(IncidentRequestDTO dto) {

        // Critério anti duplicação
        if (repo.existsByTituloIgnoreCase(dto.titulo())) {
            throw new BusinessException("Já existe um incidente com este título.");
        }

        Incident i = mapper.toEntity(dto);
        normalizeTags(i);

        return repo.save(i);
    }

    @Cacheable(
            value = "incidents",
            key = "{#status, #prioridade, #responsavel, #tag, #start, #end, #q, #page, #size}"
    )
    public Page<Incident> list(
            String status,
            String prioridade,
            String responsavel,
            String tag,
            Instant start,
            Instant end,
            String q,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dataAbertura").descending());

        Specification<Incident> spec = Specification
                .where(IncidentSpecs.hasStatus(status))
                .and(IncidentSpecs.hasPrioridade(prioridade))
                .and(IncidentSpecs.hasResponsavel(responsavel))
                .and(IncidentSpecs.hasTag(tag))
                .and(IncidentSpecs.betweenDataAbertura(start, end))
                .and(IncidentSpecs.hasText(q));

        return repo.findAll(spec, pageable);
    }

    @Cacheable(value = "incidentById", key = "#id")
    public Incident get(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Incidente não encontrado"));
    }

    @Caching(evict = {
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Incident changeStatus(UUID id, Incident.Status status) {
        Incident incident = get(id);
        incident.setStatus(status);
        return repo.save(incident);
    }


    @Caching(evict = {
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public Incident update(UUID id, IncidentRequestDTO dto) {
        Incident exist = get(id);

        mapper.updateEntity(exist, dto);
        normalizeTags(exist);

        return repo.save(exist);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "commentsByIncident", key = "#id"),
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    public void touch(Incident incident) {
        incident.setDataAtualizacao(Instant.now());
        repo.save(incident);
    }

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


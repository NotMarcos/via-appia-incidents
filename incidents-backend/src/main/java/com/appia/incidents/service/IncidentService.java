package com.appia.incidents.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.repository.IncidentRepository;
import java.util.Optional;

@Service
public class IncidentService {
    private final IncidentRepository repo;
    public IncidentService(IncidentRepository repo) { this.repo = repo; }

    public Incident create(Incident i) { return repo.save(i); }

    public Page<Incident> list(String status, String prioridade, int page, int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("dataAbertura").descending());
        return repo.findByStatusContainingIgnoreCaseAndPrioridadeContainingIgnoreCase(
                status == null ? "" : status,
                prioridade == null ? "" : prioridade, p);
    }

    public Optional<Incident> get(String id) { return repo.findById(id); }

    public Incident update(String id, Incident updated) {
        Incident exist = repo.findById(id).orElseThrow();
        exist.setTitulo(updated.getTitulo());
        exist.setDescricao(updated.getDescricao());
        exist.setPrioridade(updated.getPrioridade());
        exist.setStatus(updated.getStatus());
        exist.setResponsavelEmail(updated.getResponsavelEmail());
        exist.setTags(updated.getTags());
        return repo.save(exist);
    }

    public void delete(String id) { repo.deleteById(id); }
}

package com.appia.incidents.mapper;

import com.appia.incidents.dto.*;
import com.appia.incidents.entity.Incident;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class IncidentMapper {

    public Incident toEntity(IncidentRequestDTO dto) {
        return Incident.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .prioridade(dto.prioridade())
                .status(dto.status())
                .responsavelEmail(dto.responsavelEmail())
                .tags(dto.tags() == null ? new HashSet<>() : new HashSet<>(dto.tags()))
                .build();
    }

    public void updateEntity(Incident incident, IncidentRequestDTO dto) {
        incident.setTitulo(dto.titulo());
        incident.setDescricao(dto.descricao());
        incident.setPrioridade(dto.prioridade());
        incident.setStatus(dto.status());
        incident.setResponsavelEmail(dto.responsavelEmail());
        incident.setTags(dto.tags() == null ? new HashSet<>() : new HashSet<>(dto.tags()));
    }

    public IncidentResponseDTO toDTO(Incident i) {
        return new IncidentResponseDTO(
                i.getId().toString(),
                i.getTitulo(),
                i.getDescricao(),
                i.getPrioridade().name(),
                i.getStatus().name(),
                i.getResponsavelEmail(),
                i.getTags().stream().toList(),
                i.getDataAbertura(),
                i.getDataAtualizacao()
        );
    }
}

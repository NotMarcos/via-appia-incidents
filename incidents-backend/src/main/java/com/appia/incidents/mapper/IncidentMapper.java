package com.appia.incidents.mapper;

import com.appia.incidents.dto.*;
import com.appia.incidents.entity.Incident;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IncidentMapper {

    // --------------------------------------------
    // DTO → ENTITY (CREATE)
    // --------------------------------------------
    public Incident toEntity(IncidentRequestDTO dto) {
        return Incident.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .prioridade(dto.prioridade())
                .status(dto.status())
                .responsavelEmail(dto.responsavelEmail())
                .tags(dto.tags() == null
                        ? new HashSet<>()
                        : new HashSet<>(dto.tags()))
                .build();
    }

    // --------------------------------------------
    // DTO → ENTITY (UPDATE)
    // --------------------------------------------
    public void updateEntity(Incident incident, IncidentRequestDTO dto) {

        incident.setTitulo(dto.titulo());
        incident.setDescricao(dto.descricao());
        incident.setPrioridade(dto.prioridade());
        incident.setStatus(dto.status());
        incident.setResponsavelEmail(dto.responsavelEmail());

        if (dto.tags() != null) {
            incident.setTags(new HashSet<>(dto.tags()));
        }
    }

    // --------------------------------------------
    // ENTITY → DTO
    // --------------------------------------------
    public IncidentResponseDTO toDTO(Incident i) {
        return new IncidentResponseDTO(
                i.getId().toString(),
                i.getTitulo(),
                i.getDescricao(),
                safeEnum(i.getPrioridade()),
                safeEnum(i.getStatus()),
                i.getResponsavelEmail(),
                new ArrayList<>(i.getTags()),
                i.getDataAbertura(),
                i.getDataAtualizacao()
        );
    }

    private String safeEnum(Enum<?> e) {
        return e == null ? null : e.name();
    }
}

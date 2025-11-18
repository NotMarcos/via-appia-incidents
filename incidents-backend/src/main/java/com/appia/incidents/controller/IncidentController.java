package com.appia.incidents.controller;

import com.appia.incidents.dto.*;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.mapper.IncidentMapper;
import com.appia.incidents.service.IncidentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/incidents")
@Tag(name = "Incidents", description = "Endpoints para gerenciamento de incidentes")
public class IncidentController {

    private final IncidentService svc;
    private final IncidentMapper mapper;

    public IncidentController(IncidentService svc, IncidentMapper mapper) {
        this.svc = svc;
        this.mapper = mapper;
    }

    // ---------------------------------------------------------
    // LISTAR
    // ---------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    @Operation(
            summary = "Listar incidentes",
            description = "Lista incidentes com filtros opcionais e paginação."
    )
    public Page<IncidentResponseDTO> list(
            @Parameter(description = "Filtrar por status")
            @RequestParam(name = "status", required = false) String status,

            @Parameter(description = "Filtrar por prioridade")
            @RequestParam(name = "prioridade", required = false) String prioridade,

            @Parameter(description = "Busca textual em título, descrição e tags")
            @RequestParam(name = "q", required = false) String q,

            @Parameter(description = "Número da página (default 0)")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página (default 10)")
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return svc.list(status, prioridade, q, page, size)
                .map(mapper::toDTO);
    }

    // ---------------------------------------------------------
    // OBTER POR ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    @Operation(summary = "Buscar incidente por ID")
    public IncidentResponseDTO get(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "id") UUID id
    ) {
        return mapper.toDTO(svc.get(id));
    }

    // ---------------------------------------------------------
    // CRIAR
    // ---------------------------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    @Operation(summary = "Criar novo incidente")
    public IncidentResponseDTO create(
            @RequestBody IncidentRequestDTO dto
    ) {
        Incident saved = svc.create(dto);
        return mapper.toDTO(saved);
    }

    // ---------------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WRITE')")
    @Operation(summary = "Atualizar incidente por ID")
    public IncidentResponseDTO update(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "id") UUID id,

            @RequestBody IncidentRequestDTO dto
    ) {
        return mapper.toDTO(svc.update(id, dto));
    }

    // ---------------------------------------------------------
    // PATCH - ALTERAR STATUS
    // ---------------------------------------------------------
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('WRITE')")
    @Operation(summary = "Alterar status do incidente")
    public IncidentResponseDTO changeStatus(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "id") UUID id,

            @RequestBody ChangeStatusDTO dto
    ) {
        Incident incident = svc.get(id);
        incident.setStatus(dto.status());

        Incident updated = svc.update(id,
                new IncidentRequestDTO(
                        incident.getTitulo(),
                        incident.getDescricao(),
                        incident.getPrioridade(),
                        incident.getStatus(),
                        incident.getResponsavelEmail(),
                        incident.getTags().stream().toList()
                )
        );

        return mapper.toDTO(updated);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WRITE')")
    @Operation(summary = "Remover incidente por ID")
    @ApiResponse(responseCode = "204", description = "Removido com sucesso")
    public void delete(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "id") UUID id
    ) {
        svc.delete(id);
    }
}

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

import java.time.Instant;
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

            @Parameter(description = "Filtrar por status (ABERTA, EM_ANDAMENTO, RESOLVIDA, CANCELADA)")
            @RequestParam(name = "status", required = false) String status,

            @Parameter(description = "Filtrar por prioridade (BAIXA, MEDIA, ALTA)")
            @RequestParam(name = "prioridade", required = false) String prioridade,

            @Parameter(description = "Filtrar por responsável (email)")
            @RequestParam(name = "responsavel", required = false) String responsavel,

            @Parameter(description = "Filtrar por tag")
            @RequestParam(name = "tag", required = false) String tag,

            @Parameter(description = "Filtrar ocorrências após esta data")
            @RequestParam(name = "start", required = false) Instant start,

            @Parameter(description = "Filtrar ocorrências antes desta data")
            @RequestParam(name = "end", required = false) Instant end,

            @Parameter(description = "Busca textual em título, descrição e tags")
            @RequestParam(name = "q", required = false) String q,

            @Parameter(description = "Número da página (default 0)")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página (default 10)")
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return svc.list(status, prioridade, responsavel, tag, start, end, q, page, size)
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
            @PathVariable UUID id,
            @RequestBody ChangeStatusDTO dto
    ) {
        return mapper.toDTO(
                svc.changeStatus(id, dto.status())
        );
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

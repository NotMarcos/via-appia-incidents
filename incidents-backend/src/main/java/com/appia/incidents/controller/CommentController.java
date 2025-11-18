package com.appia.incidents.controller;

import com.appia.incidents.dto.CommentRequestDTO;
import com.appia.incidents.dto.CommentResponseDTO;
import com.appia.incidents.entity.Incident;
import com.appia.incidents.mapper.CommentMapper;
import com.appia.incidents.service.CommentService;
import com.appia.incidents.service.IncidentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/incidents/{incidentId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comentários associados a um incidente")
public class CommentController {

    private final CommentService commentService;
    private final IncidentService incidentService;
    private final CommentMapper mapper;

    // ---------------------------------------------------------
    // LISTAR
    // ---------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    @Operation(
            summary = "Listar comentários",
            description = "Lista todos os comentários do incidente, ordenados por data de criação."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<CommentResponseDTO> list(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "incidentId") UUID incidentId
    ) {
        return commentService.findByIncidentId(incidentId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ---------------------------------------------------------
    // CRIAR
    // ---------------------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WRITE')")
    @Operation(
            summary = "Adicionar comentário",
            description = "Cria um novo comentário vinculado ao incidente informado."
    )
    @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso")
    public CommentResponseDTO add(
            @Parameter(description = "ID do incidente")
            @PathVariable(name = "incidentId") UUID incidentId,

            @Valid @RequestBody CommentRequestDTO dto
    ) {
        // Carrega o incidente
        Incident incident = incidentService.get(incidentId);

        // Mapper monta entidade já com o incidente vinculado
        var comment = mapper.toEntity(dto, incident);

        // Agora sim: chama o service com incidentId E comment
        var saved = commentService.create(incidentId, comment);

        return mapper.toDTO(saved);
    }
}
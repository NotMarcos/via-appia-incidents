package com.appia.incidents.dto;

import java.time.Instant;
import java.util.List;

public record IncidentResponseDTO(
        String id,
        String titulo,
        String descricao,
        String prioridade,
        String status,
        String responsavelEmail,
        List<String> tags,
        Instant dataAbertura,
        Instant dataAtualizacao
) {}

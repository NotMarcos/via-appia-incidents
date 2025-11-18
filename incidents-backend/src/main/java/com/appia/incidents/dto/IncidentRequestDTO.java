package com.appia.incidents.dto;

import jakarta.validation.constraints.*;
import com.appia.incidents.entity.Incident;

import java.util.List;

public record IncidentRequestDTO(

        @NotBlank
        @Size(min = 5, max = 120)
        String titulo,

        @Size(max = 5000)
        String descricao,

        @NotNull
        Incident.Prioridade prioridade,

        @NotNull
        Incident.Status status,

        @NotBlank
        @Email
        String responsavelEmail,

        List<String> tags
) {}

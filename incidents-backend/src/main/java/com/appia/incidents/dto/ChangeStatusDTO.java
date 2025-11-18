package com.appia.incidents.dto;
import com.appia.incidents.entity.Incident;

import jakarta.validation.constraints.NotNull;

public record ChangeStatusDTO(
        @NotNull Incident.Status status
) {}

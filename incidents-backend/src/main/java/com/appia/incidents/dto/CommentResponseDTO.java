package com.appia.incidents.dto;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentResponseDTO {

    private String id;
    private String incidentId;
    private String autor;
    private String mensagem;
    private Instant dataCriacao;
}

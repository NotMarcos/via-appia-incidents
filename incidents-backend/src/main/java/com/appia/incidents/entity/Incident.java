package com.appia.incidents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Incident {
    @Id
    @Column(length = 36)
    private String id;

    private String titulo;

    @Column(length = 5000)
    private String descricao;

    private String prioridade; // BAIXA, MEDIA, ALTA
    private String status; // ABERTA, EM_ANDAMENTO, RESOLVIDA, CANCELADA
    private String responsavelEmail;

    @Column(columnDefinition = "text")
    private String tags;

    private Instant dataAbertura;
    private Instant dataAtualizacao;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        dataAbertura = Instant.now();
        dataAtualizacao = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = Instant.now();
    }
}

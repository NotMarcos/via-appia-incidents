package com.appia.incidents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comment {
    @Id
    @Column(length = 36)
    private String id;

    private String incidentId;
    private String autor;

    @Column(length = 2000)
    private String mensagem;

    private Instant dataCriacao;

    @PrePersist
    public void prePersist() {
        if (id == null) id = java.util.UUID.randomUUID().toString();
        dataCriacao = Instant.now();
    }
}

package com.appia.incidents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String autor;

    @NotBlank
    @Size(min = 1, max = 2000)
    @Column(nullable = false, length = 2000)
    private String mensagem;

    @Column(nullable = false)
    private Instant dataCriacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = Instant.now();
    }
}

package com.appia.incidents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 5, max = 120)
    private String titulo;

    @Size(max = 5000)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank
    @Email
    private String responsavelEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "incident_tags",
            joinColumns = @JoinColumn(name = "incident_id")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    private Instant dataAbertura;
    private Instant dataAtualizacao;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.dataAbertura = now;
        this.dataAtualizacao = now;
        this.status = (this.status == null ? Status.ABERTA : this.status);
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = Instant.now();
    }

    public enum Prioridade {
        BAIXA, MEDIA, ALTA
    }

    public enum Status {
        ABERTA, EM_ANDAMENTO, RESOLVIDA, CANCELADA
    }
}

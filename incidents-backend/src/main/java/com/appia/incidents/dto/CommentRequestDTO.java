package com.appia.incidents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentRequestDTO {

    @NotBlank
    private String autor;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String mensagem;
}

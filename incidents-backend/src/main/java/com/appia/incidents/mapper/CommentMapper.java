package com.appia.incidents.mapper;

import com.appia.incidents.dto.CommentRequestDTO;
import com.appia.incidents.dto.CommentResponseDTO;
import com.appia.incidents.entity.Comment;
import com.appia.incidents.entity.Incident;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // ---------------------------------------------------
    // DTO → ENTITY
    // ---------------------------------------------------
    public Comment toEntity(CommentRequestDTO dto, Incident incident) {

        if (dto == null)
            throw new IllegalArgumentException("CommentRequestDTO não pode ser nulo");

        if (incident == null)
            throw new IllegalArgumentException("Incident não pode ser nulo");

        return Comment.builder()
                .autor(dto.getAutor())
                .mensagem(dto.getMensagem())
                .incident(incident)
                .build();
    }

    // ---------------------------------------------------
    // ENTITY → DTO
    // ---------------------------------------------------
    public CommentResponseDTO toDTO(Comment c) {

        if (c == null)
            return null;

        return CommentResponseDTO.builder()
                .id(c.getId() != null ? c.getId().toString() : null)
                .incidentId(c.getIncident() != null ? c.getIncident().getId().toString() : null)
                .autor(c.getAutor())
                .mensagem(c.getMensagem())
                .dataCriacao(c.getDataCriacao())
                .build();
    }
}

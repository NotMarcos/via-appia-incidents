package com.appia.incidents.repository;

import com.appia.incidents.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByIncident_IdOrderByDataCriacaoAsc(UUID incidentId);
}

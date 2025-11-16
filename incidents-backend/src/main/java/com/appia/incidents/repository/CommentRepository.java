package com.appia.incidents.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.appia.incidents.entity.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByIncidentIdOrderByDataCriacaoAsc(String incidentId);
}

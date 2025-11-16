// CommentService.java
package com.appia.incidents.service;
import org.springframework.stereotype.Service;
import com.appia.incidents.entity.Comment;
import com.appia.incidents.repository.CommentRepository;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository repo;
    public CommentService(CommentRepository repo){ this.repo = repo; }
    public Comment add(Comment c){ return repo.save(c); }
    public List<Comment> listByIncident(String incidentId){ return repo.findByIncidentIdOrderByDataCriacaoAsc(incidentId); }
}

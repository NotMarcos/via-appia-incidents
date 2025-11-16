// CommentController.java
package com.appia.incidents.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.appia.incidents.entity.Comment;
import com.appia.incidents.service.CommentService;
import java.util.List;

@RestController
@RequestMapping("/incidents/{incidentId}/comments")
public class CommentController {
    private final CommentService svc;
    public CommentController(CommentService svc){ this.svc = svc; }

    @GetMapping
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    public List<Comment> list(@PathVariable String incidentId){
        return svc.listByIncident(incidentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public Comment add(@PathVariable String incidentId, @RequestBody Comment c){
        c.setIncidentId(incidentId);
        return svc.add(c);
    }
}

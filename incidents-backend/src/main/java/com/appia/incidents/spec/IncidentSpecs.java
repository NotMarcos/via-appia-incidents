package com.appia.incidents.spec;

import com.appia.incidents.entity.Incident;
import org.springframework.data.jpa.domain.Specification;

public class IncidentSpecs {

    public static Specification<Incident> hasStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), Incident.Status.valueOf(status));
    }

    public static Specification<Incident> hasPrioridade(String prioridade) {
        return (root, query, cb) ->
                prioridade == null ? null : cb.equal(root.get("prioridade"), Incident.Prioridade.valueOf(prioridade));
    }

    public static Specification<Incident> hasText(String q) {
        return (root, query, cb) ->
                q == null ? null : cb.like(cb.lower(root.get("titulo")), "%" + q.toLowerCase() + "%");
    }
}

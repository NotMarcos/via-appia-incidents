package com.appia.incidents.spec;

import com.appia.incidents.entity.Incident;
import org.springframework.data.jpa.domain.Specification;

public class IncidentSpecs {

    public static Specification<Incident> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return null;

            try {
                Incident.Status enumStatus =
                        Incident.Status.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), enumStatus);
            } catch (IllegalArgumentException e) {
                return null; // evita erro 500
            }
        };
    }

    public static Specification<Incident> hasPrioridade(String prioridade) {
        return (root, query, cb) -> {
            if (prioridade == null || prioridade.isBlank()) return null;

            try {
                Incident.Prioridade enumPrio =
                        Incident.Prioridade.valueOf(prioridade.toUpperCase());
                return cb.equal(root.get("prioridade"), enumPrio);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<Incident> hasText(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;

            String like = "%" + q.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("titulo")), like),
                    cb.like(cb.lower(root.get("descricao")), like)
            );
        };
    }
}

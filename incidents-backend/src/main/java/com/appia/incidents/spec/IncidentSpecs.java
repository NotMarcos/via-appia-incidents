package com.appia.incidents.spec;

import com.appia.incidents.entity.Incident;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class IncidentSpecs {

    public static Specification<Incident> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return null;

            try {
                Incident.Status value =
                        Incident.Status.valueOf(status.trim().toUpperCase());
                return cb.equal(root.get("status"), value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<Incident> hasPrioridade(String prioridade) {
        return (root, query, cb) -> {
            if (prioridade == null || prioridade.isBlank()) return null;

            try {
                Incident.Prioridade value =
                        Incident.Prioridade.valueOf(prioridade.trim().toUpperCase());
                return cb.equal(root.get("prioridade"), value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<Incident> hasText(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;

            String like = "%" + q.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("titulo")), like),
                    cb.like(cb.lower(root.get("descricao")), like)
            );
        };
    }

    public static Specification<Incident> hasResponsavel(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) return null;

            String like = "%" + email.trim().toLowerCase() + "%";

            return cb.like(cb.lower(root.get("responsavelEmail")), like);
        };
    }

    public static Specification<Incident> hasTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null || tag.isBlank()) return null;

            var join = root.join("tags");

            return cb.equal(cb.lower(join.as(String.class)), tag.trim().toLowerCase());
        };
    }

    public static Specification<Incident> betweenDataAbertura(Instant start, Instant end) {
        return (root, query, cb) -> {

            if (start == null && end == null) return null;

            if (start != null && end != null)
                return cb.between(root.get("dataAbertura"), start, end);

            if (start != null)
                return cb.greaterThanOrEqualTo(root.get("dataAbertura"), start);

            return cb.lessThanOrEqualTo(root.get("dataAbertura"), end);
        };
    }
}

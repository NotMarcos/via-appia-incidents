package com.appia.incidents.spec;

import com.appia.incidents.entity.Incident;
import org.springframework.data.jpa.domain.Specification;

public class IncidentSpecs {

    // ---------------------------------------------------------------------
    // STATUS
    // ---------------------------------------------------------------------
    public static Specification<Incident> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return null;

            try {
                Incident.Status enumStatus =
                        Incident.Status.valueOf(status.trim().toUpperCase());

                return cb.equal(root.get("status"), enumStatus);

            } catch (IllegalArgumentException e) {
                return null; // evita 500 ao receber valor inválido
            }
        };
    }

    // ---------------------------------------------------------------------
    // PRIORIDADE
    // ---------------------------------------------------------------------
    public static Specification<Incident> hasPrioridade(String prioridade) {
        return (root, query, cb) -> {
            if (prioridade == null || prioridade.isBlank()) return null;

            try {
                Incident.Prioridade enumPrio =
                        Incident.Prioridade.valueOf(prioridade.trim().toUpperCase());

                return cb.equal(root.get("prioridade"), enumPrio);

            } catch (IllegalArgumentException e) {
                return null; // evita 500 ao receber valor inválido
            }
        };
    }

    // ---------------------------------------------------------------------
    // TEXTO (título ou descrição)
    // ---------------------------------------------------------------------
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
}

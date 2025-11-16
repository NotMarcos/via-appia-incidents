package com.appia.incidents.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.appia.incidents.entity.Incident;
import org.springframework.data.domain.*;
public interface IncidentRepository extends JpaRepository<Incident, String> {
    Page<Incident> findByStatusContainingIgnoreCaseAndPrioridadeContainingIgnoreCase(String status, String prioridade, Pageable pageable);
}

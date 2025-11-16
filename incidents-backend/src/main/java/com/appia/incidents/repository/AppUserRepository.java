package com.appia.incidents.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.appia.incidents.entity.AppUser;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

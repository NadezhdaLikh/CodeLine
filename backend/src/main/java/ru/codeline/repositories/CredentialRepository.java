package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, UUID> {
    Optional<Credential> findByUserEmail(String email);
    List<Credential> findByRole (Role role);
}



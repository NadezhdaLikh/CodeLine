package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.codeline.models.user.CompositeKey;
import ru.codeline.models.user.Progress;

import java.util.UUID;

public interface ProgressRepository extends JpaRepository<Progress, CompositeKey> {
}

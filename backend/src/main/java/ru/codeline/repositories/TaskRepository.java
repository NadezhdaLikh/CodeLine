package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codeline.models.course.Task;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByLectureId(UUID lectureId);
}

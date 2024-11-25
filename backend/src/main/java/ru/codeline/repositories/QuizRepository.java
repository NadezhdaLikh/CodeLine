package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codeline.models.course.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}

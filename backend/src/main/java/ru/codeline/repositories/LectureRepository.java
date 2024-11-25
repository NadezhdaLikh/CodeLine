package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.codeline.models.course.Lecture;

import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    @Query("SELECT l FROM Lecture l WHERE l.course.id = :courseId AND l.numInSeq = 1")
    Lecture findFirstLectureByCourseId(@Param("courseId") UUID courseId);
}


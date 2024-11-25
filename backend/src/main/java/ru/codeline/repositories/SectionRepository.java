package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.course.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    List<Section> findByLecture(Lecture lecture);
}

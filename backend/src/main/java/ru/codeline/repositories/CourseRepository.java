package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.codeline.models.course.Course;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByUserId(UUID userId);

    // This query counts the number of courses for each teacher by grouping by the teacher's ID
    @Query("SELECT c.user.id, COUNT(c) FROM Course c GROUP BY c.user.id")
    List<Object[]> countCoursesByTeacher();

    // This query joins Course with Progress via the composite key (p.id.course.id)
    // It then counts the number of distinct students (p.id.user.id) for each teacher's courses
    @Query("SELECT c.user.id, COUNT(DISTINCT p.id.user.id) FROM Course c JOIN Progress p ON p.id.course.id = c.id GROUP BY c.user.id")
    List<Object[]> countStudentsByTeacher();
}

package ru.codeline.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeline.dto.lecture.LectureWithSectionsResponse;
import ru.codeline.exceptions.AccessDeniedException;
import ru.codeline.exceptions.CourseNotFoundException;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.models.course.Course;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.user.CompositeKey;
import ru.codeline.models.user.Progress;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CourseRepository;
import ru.codeline.repositories.LectureRepository;
import ru.codeline.repositories.ProgressRepository;
import ru.codeline.repositories.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final ProgressRepository progressRepository;

    @Transactional
    public String enrollStudentInCourse(User student, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Lecture firstLecture = lectureRepository.findFirstLectureByCourseId(courseId);

        if (firstLecture == null) {
            throw new EntityNotFoundException("No lectures found for the course");
        }

        CompositeKey compositeKey = new CompositeKey(student, course);

        Progress progress = Progress.builder()
                .id(compositeKey)
                .lecture(firstLecture)
                .numOfMistakes(0)
                .build();

        progressRepository.save(progress);

        return "Congratulations! You successfully enrolled in the course:) Enjoy your studies!";
    }
}

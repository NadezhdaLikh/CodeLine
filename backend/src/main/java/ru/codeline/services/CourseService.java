package ru.codeline.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeline.dto.course.CourseRequest;
import ru.codeline.dto.course.AllCoursesResponse;
import ru.codeline.dto.course.CourseResponse;
import ru.codeline.exceptions.CourseNotFoundException;
import ru.codeline.models.course.Course;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CourseRepository;
import ru.codeline.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Course createCourse(CourseRequest courseRequest, User teacherId) {
        Course newCourse = new Course();
        newCourse.setTitle(courseRequest.getTitle());
        newCourse.setLanguage(courseRequest.getLanguage());
        newCourse.setUser(teacherId);
        courseRepository.save(newCourse);

        return newCourse;
    }

    public List<AllCoursesResponse> getAllCoursesForAdmin() {
        List<Course> courses = courseRepository.findAll();
        List<AllCoursesResponse> coursesForAdmin = new ArrayList<>();

        for (Course course : courses) {
            User teacher = course.getUser();
            UUID teacherId = teacher.getId();
            String teacherFirstName = userRepository.findFirstNameById(teacherId);
            String teacherLastName = userRepository.findLastNameById(teacherId);

            // Calculate the total number of quizzes for the course
            int totalQuizzes = 0;
            for (Lecture lecture : course.getLectures()) {
                totalQuizzes += lecture.getQuizzes().size();
            }

            AllCoursesResponse allCoursesResponseForAdmin = new AllCoursesResponse();
            allCoursesResponseForAdmin.setCourseId(course.getId());
            allCoursesResponseForAdmin.setTitle(course.getTitle());
            allCoursesResponseForAdmin.setLanguage(course.getLanguage());
            allCoursesResponseForAdmin.setNumOfLect(course.getNumOfLect());
            allCoursesResponseForAdmin.setNumOfQuiz(totalQuizzes); // Set the number of quizzes
            allCoursesResponseForAdmin.setTeacherId(teacherId);
            allCoursesResponseForAdmin.setTeacherFirstName(teacherFirstName);
            allCoursesResponseForAdmin.setTeacherLastName(teacherLastName);
            coursesForAdmin.add(allCoursesResponseForAdmin);
        }

        return coursesForAdmin;
    }

    public List<AllCoursesResponse> getAllCoursesForTeachers(UUID teacherId) {
        List<Course> courses = courseRepository.findByUserId(teacherId);
        List<AllCoursesResponse> coursesForTeachers = new ArrayList<>();

        for (Course course : courses) {
            int totalQuizzes = 0;
            for (Lecture lecture : course.getLectures()) {
                totalQuizzes += lecture.getQuizzes().size();
            }
            AllCoursesResponse allCoursesResponseForTeachers = new AllCoursesResponse(course.getId(),
                    course.getTitle(), course.getLanguage(), course.getNumOfLect(), totalQuizzes);
            coursesForTeachers.add(allCoursesResponseForTeachers);
        }

        return coursesForTeachers;
    }

    public List<AllCoursesResponse> getAllCoursesForStudents() {
        List<Course> courses = courseRepository.findAll();
        List<AllCoursesResponse> coursesForStudents = new ArrayList<>();

        for (Course course : courses) {
            User teacher = course.getUser();
            UUID teacherId = teacher.getId();
            String teacherFirstName = userRepository.findFirstNameById(teacherId);
            String teacherLastName = userRepository.findLastNameById(teacherId);

            AllCoursesResponse allCoursesResponseForStudents = new AllCoursesResponse(course.getId(),
                    course.getTitle(), course.getLanguage(), course.getNumOfLect(),
                    teacherFirstName, teacherLastName);
            coursesForStudents.add(allCoursesResponseForStudents);
        }

        return coursesForStudents;
    }

    public CourseResponse updateCourse(UUID courseId, CourseRequest request) throws CourseNotFoundException{
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course updatedCourse = optionalCourse.get();
            updatedCourse.setTitle(request.getTitle());
            updatedCourse.setLanguage(request.getLanguage());

            courseRepository.save(updatedCourse);

            return new CourseResponse(updatedCourse.getId(), updatedCourse.getTitle(), updatedCourse.getLanguage());
        } else {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
    }

    public void deleteCourseById(UUID courseId) throws CourseNotFoundException {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }
}


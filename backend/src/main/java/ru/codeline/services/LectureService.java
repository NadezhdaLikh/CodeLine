package ru.codeline.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeline.dto.forLecture.SectionRequest;
import ru.codeline.dto.lecture.LectureRequest;
import ru.codeline.dto.lecture.LectureResponse;
import ru.codeline.exceptions.AccessDeniedException;
import ru.codeline.exceptions.CourseNotFoundException;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.models.course.*;
import ru.codeline.models.user.CompositeKey;
import ru.codeline.models.user.Progress;
import ru.codeline.models.user.User;
import ru.codeline.repositories.*;
import ru.codeline.dto.lecture.LectureWithSectionsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;
    private final ProgressRepository progressRepository;

    @Transactional
    public Lecture createLecture(UUID courseId, LectureRequest request)
            throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: "
                        + courseId));

        // Create and save the lecture first to get its ID
        Lecture newLecture = lectureRepository.save(createLectureEntity(request, course));

        // Add sections if provided
        addSections(newLecture, request.getSections());

        // Save the lecture again to persist sections
        return lectureRepository.save(newLecture);
    }

    public List<LectureResponse> getAllLecturesByCourseId(UUID courseId) throws CourseNotFoundException {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<LectureResponse> lectureResponseList = new ArrayList<>();
            for (Lecture lecture : course.getLectures()) {
                LectureResponse response = new LectureResponse(
                        lecture.getId(),
                        lecture.getNumInSeq(),
                        lecture.getTitle(),
                        lecture.getDescription(),
                        lecture.getSections().size() // Number of sections
                );
                lectureResponseList.add(response);
            }
            return lectureResponseList;
        } else {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
    }

    public LectureWithSectionsResponse getLectureWithoutCheck(UUID courseId, UUID lectureId)
            throws CourseNotFoundException, LectureNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        return LectureWithSectionsResponse.excludeFromLecture(lecture);
    }

    public LectureWithSectionsResponse getLectureWithCheck(User user, UUID courseId, UUID lectureId)
            throws CourseNotFoundException, LectureNotFoundException, AccessDeniedException {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        CompositeKey progressKey = new CompositeKey(user, course);
        Progress progress = progressRepository.findById(progressKey)
                .orElseThrow(() -> new AccessDeniedException("Progress not found for user in this course."));

        Lecture currentLecture = progress.getLecture();
        if (currentLecture.getNumInSeq() >= lecture.getNumInSeq()) {
            return LectureWithSectionsResponse.excludeFromLecture(lecture);
        } else {
            throw new AccessDeniedException("Student does not have access to this lecture yet.");
        }
    }

    @Transactional
    public LectureWithSectionsResponse updateLecture(UUID courseId, UUID lectureId,
                                                     LectureRequest request)
            throws CourseNotFoundException, LectureNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));
        Lecture updatedLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        updatedLecture.setNumInSeq(request.getNumInSeq());
        updatedLecture.setTitle(request.getTitle());
        updatedLecture.setDescription(request.getDescription());

        List<Section> existingSections = sectionRepository.findByLecture(updatedLecture);
        if (!existingSections.isEmpty()) {
            updatedLecture.getSections().clear();
        }
        addSections(updatedLecture, request.getSections());

        Lecture savedLecture = lectureRepository.save(updatedLecture);

        return LectureWithSectionsResponse.excludeFromLecture(savedLecture);
    }

    @Transactional
    public void deleteLectureById(UUID courseId, UUID lectureId) throws CourseNotFoundException, LectureNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));
        Lecture deletedLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        // Update the course's numberOfLectures
        course.setNumOfLect(course.getNumOfLect() - 1);

        // Remove the lecture from the course's list of lectures
        course.getLectures().remove(deletedLecture);

        courseRepository.save(course);
    }

    private Lecture createLectureEntity(LectureRequest request, Course course) {
        Lecture lecture = new Lecture();
        lecture.setNumInSeq(request.getNumInSeq());
        lecture.setTitle(request.getTitle());
        lecture.setDescription(request.getDescription());
        lecture.setCourse(course);

        course.getLectures().add(lecture);
        course.setNumOfLect(course.getNumOfLect() + 1);

        return lecture;
    }

    private void addSections(Lecture lecture, List<SectionRequest> sections) {
        if (sections != null) {
            for (SectionRequest sectionRequest : sections) {
                Section section = new Section();
                section.setNumInSeq(sectionRequest.getNumInSeq());
                section.setTitle(sectionRequest.getTitle());
                section.setContent(sectionRequest.getContent());
                section.setLecture(lecture);

                lecture.getSections().add(section); // CascadeType.PERSIST will handle saving
                sectionRepository.save(section);
            }
        }
    }
}


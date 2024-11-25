package ru.codeline.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.codeline.dto.lecture.LectureRequest;
import ru.codeline.dto.lecture.LectureResponse;
import ru.codeline.dto.lecture.LectureWithSectionsResponse;
import ru.codeline.exceptions.CourseNotFoundException;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CredentialRepository;
import ru.codeline.repositories.UserRepository;
import ru.codeline.services.JwtService;
import ru.codeline.services.LectureService;
import ru.codeline.services.StudentService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@RestController
public class LectureController {
    private final LectureService lectureService;
    private final JwtService jwtService;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/{courseId}/lecture")
    public ResponseEntity<Lecture> createLecture(@PathVariable UUID courseId,
                                                 @RequestBody LectureRequest request)
            throws CourseNotFoundException {
        Lecture newLecture = lectureService.createLecture(courseId, request);
        return ResponseEntity.ok(newLecture);
    }

    @GetMapping("/{courseId}/lectures")
    public ResponseEntity<List<LectureResponse>> getAllLectures(@PathVariable UUID courseId)
            throws CourseNotFoundException {
        List<LectureResponse> lectures = lectureService.getAllLecturesByCourseId(courseId);
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/{courseId}/lectures/{lectureId}")
    public ResponseEntity<?> getLecture(@PathVariable UUID courseId,
                                        @PathVariable UUID lectureId,
                                        HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token not found in request headers");
        }
        String jwtToken = authorizationHeader.substring(7);

        /*String jwtToken = (String) request.getAttribute("jwtToken");

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token not found in cookies:(");
        }*/

        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Credential credential = credentialRepository.findByUserEmail(userEmail).orElse(null);

            if (credential == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + userEmail + " not found:(");
            }
            if (Role.ADMIN.equals(credential.getRole()) || Role.TEACHER.equals(credential.getRole())) {
                LectureWithSectionsResponse lectureWithSectionsResponse = lectureService.getLectureWithoutCheck(courseId, lectureId);
                return ResponseEntity.ok(lectureWithSectionsResponse);
            } else {
                User user = userRepository.findByEmail(userEmail);
                LectureWithSectionsResponse lectureWithSectionsResponse = lectureService.getLectureWithCheck(user, courseId, lectureId);
                return ResponseEntity.ok(lectureWithSectionsResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get courses from the catalogue:(");
        }
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{courseId}/lectures/{lectureId}")
    public ResponseEntity<LectureWithSectionsResponse> updateLecture(@PathVariable UUID courseId,
                                                                     @PathVariable UUID lectureId,
                                                                     @RequestBody LectureRequest lectureRequest)
            throws LectureNotFoundException, CourseNotFoundException {
        LectureWithSectionsResponse updatedLecture = lectureService.updateLecture(courseId,
                lectureId, lectureRequest);
        return ResponseEntity.ok(updatedLecture);
    }


    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("/{courseId}/lectures/{lectureId}")
    public ResponseEntity<String> deleteLecture(@PathVariable UUID courseId,
                                                @PathVariable UUID lectureId) throws LectureNotFoundException, CourseNotFoundException {
        lectureService.deleteLectureById(courseId, lectureId);
        return ResponseEntity.ok("The lecture was deleted successfully!");
    }
}
























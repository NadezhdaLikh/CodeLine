package ru.codeline.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codeline.dto.lecture.LectureWithSectionsResponse;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Progress;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CredentialRepository;
import ru.codeline.repositories.UserRepository;
import ru.codeline.services.JwtService;
import ru.codeline.services.StudentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<?> enrollStudentInCourse(@PathVariable UUID courseId,
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
            User student = userRepository.findByEmail(userEmail);

            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + userEmail + " not found:(");
            }

            return ResponseEntity.ok(studentService.enrollStudentInCourse(student, courseId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enroll in the course:( Please try again!");
        }
    }
}

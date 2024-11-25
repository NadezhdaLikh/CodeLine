package ru.codeline.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.codeline.dto.compiler.CompilerResponse;
import ru.codeline.models.course.Course;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.course.Task;
import ru.codeline.repositories.CourseRepository;
import ru.codeline.repositories.LectureRepository;
import ru.codeline.repositories.TaskRepository;
import ru.codeline.dto.compiler.SourceCodeRequest;
import ru.codeline.dto.compiler.CompilerRequest;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class CheckCodeController {
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final TaskRepository taskRepository;

    @PostMapping("/{lectureId}/compiler")
    public ResponseEntity<?> checkCode(@PathVariable UUID lectureId,
                                       @RequestBody SourceCodeRequest sourceCodeRequest) {

        // Retrieve the Task record
        Optional<Task> taskOptional = taskRepository.findByLectureId(lectureId);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Task not found for lectureId: " + lectureId);
        }
        Task task = taskOptional.get();

        // Retrieve the Lecture record
        Optional<Lecture> lectureOptional = lectureRepository.findById(lectureId);
        if (!lectureOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Lecture not found for lectureId: " + lectureId);
        }
        Lecture lecture = lectureOptional.get();

        // Retrieve the Course record
        UUID courseId = lecture.getCourse().getId();
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (!courseOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Course not found for courseId: " + courseId);
        }
        Course course = courseOptional.get();

        // Prepare data for the compiler service
        String input = task.getInput();
        String expectedOutput = task.getOutput();
        String language = course.getLanguage();
        String sourceCode = sourceCodeRequest.getSourceCode();

        // Call the external compiler service
        RestTemplate restTemplate = new RestTemplate();
        String compilerUrl = "http://localhost:5000/api/v1/compiler?timeLimit=10&memoryLimit=500";
        CompilerRequest compilerRequest = new CompilerRequest(sourceCode, input, expectedOutput, language);

        ResponseEntity<CompilerResponse> compilerResponse = restTemplate.postForEntity(compilerUrl, compilerRequest, CompilerResponse.class);

        // Return the response from the compiler service
        return ResponseEntity.ok(compilerResponse.getBody());
    }
}

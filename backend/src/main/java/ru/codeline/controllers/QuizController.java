package ru.codeline.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.codeline.dto.forLecture.QuizRequest;
import ru.codeline.dto.forLecture.QuizResponse;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.exceptions.QuizNotFoundException;
import ru.codeline.models.course.Quiz;
import ru.codeline.services.QuizService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
@RestController
public class QuizController {
    private final QuizService quizService;

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/{lectureId}/quiz")
    public ResponseEntity<Quiz> createQuiz(@PathVariable UUID lectureId,
                                           @RequestBody QuizRequest request) throws LectureNotFoundException {
        Quiz newQuiz = quizService.createQuiz(lectureId, request);
        return ResponseEntity.ok(newQuiz);
    }

    @GetMapping("/{lectureId}/quizzes")
    public ResponseEntity<List<QuizResponse>> getAllQuizzes(@PathVariable UUID lectureId) throws LectureNotFoundException {
        List<QuizResponse> quizzes = quizService.getAllQuizzesByLectureId(lectureId);
        return ResponseEntity.ok(quizzes);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @GetMapping("/{lectureId}/quizzes/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable UUID lectureId,
                                        @PathVariable Integer quizId) throws QuizNotFoundException, LectureNotFoundException {
        return ResponseEntity.ok(quizService.getQuizByLectureId(lectureId, quizId));
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{lectureId}/quizzes/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable UUID lectureId,
                                           @PathVariable Integer quizId,
                                           @RequestBody QuizRequest request) throws QuizNotFoundException, LectureNotFoundException {
        Quiz updatedQuiz = quizService.updateQuiz(lectureId, quizId, request);
        return ResponseEntity.ok(updatedQuiz);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @DeleteMapping("/{lectureId}/quizzes/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable UUID lectureId,
                                             @PathVariable Integer quizId) throws QuizNotFoundException, LectureNotFoundException {
        quizService.deleteQuiz(lectureId, quizId);
        return ResponseEntity.ok("The quiz was deleted successfully!");
    }
}

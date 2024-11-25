package ru.codeline.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.codeline.dto.forLecture.TaskRequest;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.exceptions.TaskNotFoundException;
import ru.codeline.models.course.Task;
import ru.codeline.services.TaskService;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
@RestController
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/{lectureId}/task")
    public ResponseEntity<Task> createTask(@PathVariable UUID lectureId,
                                           @RequestBody TaskRequest request) throws LectureNotFoundException {
        Task newTask = taskService.createTask(lectureId, request);
        return ResponseEntity.ok(newTask);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @GetMapping("/{lectureId}/task")
    public ResponseEntity<Task> getTask(@PathVariable UUID lectureId) throws LectureNotFoundException {
        Task task = taskService.getTaskByLectureId(lectureId);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{lectureId}/task")
    public ResponseEntity<Task> updateTask(@PathVariable UUID lectureId,
                                           @RequestBody TaskRequest request) throws TaskNotFoundException, LectureNotFoundException {
        Task updatedTask = taskService.updateTask(lectureId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("/{lectureId}/task")
    public ResponseEntity<String> deleteTask(@PathVariable UUID lectureId) throws TaskNotFoundException, LectureNotFoundException {
        taskService.deleteTaskByLectureId(lectureId);
        return ResponseEntity.ok("The task was deleted successfully!");
    }
}

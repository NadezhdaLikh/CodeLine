package ru.codeline.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeline.dto.forLecture.TaskRequest;
import ru.codeline.exceptions.LectureNotFoundException;
import ru.codeline.exceptions.TaskNotFoundException;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.course.Task;
import ru.codeline.repositories.LectureRepository;
import ru.codeline.repositories.TaskRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final LectureRepository lectureRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public Task createTask(UUID lectureId, TaskRequest request) throws LectureNotFoundException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        Task newTask = new Task();
        newTask.setTask(request.getTask());
        newTask.setInput(request.getInput());
        newTask.setOutput(request.getOutput());
        newTask.setLecture(lecture);

        lecture.setTask(newTask);
        lectureRepository.save(lecture);

        return newTask;
    }

    @Transactional
    public Task updateTask(UUID lectureId, TaskRequest request) throws LectureNotFoundException, TaskNotFoundException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        Task task = lecture.getTask();
        if (task == null) {
            throw new TaskNotFoundException("Task not found for lecture with id: " + lectureId);
        }

        // Update the test properties with the request data
        task.setTask(request.getTask());
        task.setInput(request.getInput());
        task.setOutput(request.getOutput());

        // Save the updated test
        return taskRepository.save(task);
    }

    public Task getTaskByLectureId(UUID lectureId) throws LectureNotFoundException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));
        return lecture.getTask();
    }

    @Transactional
    public void deleteTaskByLectureId(UUID lectureId) throws LectureNotFoundException, TaskNotFoundException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));

        if (lecture.getTask() == null) {
            throw new TaskNotFoundException("Task not found for lecture with id: " + lectureId);
        }

        // Set the test reference to null in lecture to avoid orphaned entity
        lecture.setTask(null);
        lectureRepository.save(lecture);
    }
}

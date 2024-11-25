package ru.codeline.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codeline.dto.NewTeacherRequest;
import ru.codeline.dto.AllTeachersWithDetailsResponse;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CourseRepository;
import ru.codeline.repositories.CredentialRepository;
import ru.codeline.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    // This service is responsible for handling operations that an admin can perform

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CourseRepository courseRepository;

    public void addTeacher(NewTeacherRequest request) {
        User teacherUser = new User();
        teacherUser.setFirstName(request.getFirstName());
        teacherUser.setLastName(request.getLastName());
        teacherUser.setEmail(request.getEmail());
        userRepository.save(teacherUser);

        Credential teacherCredential = new Credential();
        teacherCredential.setUser(teacherUser);

        String plainPassword = PasswordService.generateRandomPassword(8);
        teacherCredential.setPassword(passwordEncoder.encode(plainPassword));

        teacherCredential.setRole(Role.TEACHER);
        credentialRepository.save(teacherCredential);

        // Send email with password to teacher
        emailService.sendPasswordEmail(request.getEmail(), plainPassword);
    }

    public List<AllTeachersWithDetailsResponse> getAllTeachersWithDetails() {
        List<Credential> teacherCredentials = credentialRepository.findByRole(Role.TEACHER);

        List<UUID> teacherIds = new ArrayList<>();
        for (Credential credential : teacherCredentials) {
            teacherIds.add(credential.getUser().getId());
        }

        List<User> teachers = userRepository.findAllById(teacherIds);

        List<Object[]> courseCounts = courseRepository.countCoursesByTeacher();
        List<Object[]> studentCounts = courseRepository.countStudentsByTeacher();

        List<AllTeachersWithDetailsResponse> allTeachersWithDetailsResponseList = new ArrayList<>();

        for (User teacher : teachers) {
            Integer courseCount = 0;
            Integer studentCount = 0;

            for (Object[] courseCountResult : courseCounts) {
                if (courseCountResult[0].equals(teacher.getId())) {
                    courseCount = ((Number) courseCountResult[1]).intValue();
                    break;
                }
            }

            for (Object[] studentCountResult : studentCounts) {
                if (studentCountResult[0].equals(teacher.getId())) {
                    studentCount = ((Number) studentCountResult[1]).intValue();
                    break;
                }
            }

            AllTeachersWithDetailsResponse allTeachersWithDetailsResponse = new AllTeachersWithDetailsResponse(teacher.getId(), teacher.getFirstName(),
                    teacher.getLastName(), teacher.getEmail(),
                    courseCount, studentCount
            );

            allTeachersWithDetailsResponseList.add(allTeachersWithDetailsResponse);
        }

        return allTeachersWithDetailsResponseList;
    }
}

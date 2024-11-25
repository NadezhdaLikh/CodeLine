package ru.codeline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllTeachersWithDetailsResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer numOfCourses;
    private Integer numOfStudents;
}

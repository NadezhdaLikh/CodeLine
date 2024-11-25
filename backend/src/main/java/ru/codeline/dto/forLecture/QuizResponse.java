package ru.codeline.dto.forLecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private Integer id;
    private String question;
    // private String corrAns;
}

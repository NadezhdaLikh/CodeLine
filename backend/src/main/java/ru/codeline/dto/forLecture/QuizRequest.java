package ru.codeline.dto.forLecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizRequest {
    private String question;
    private List<String> options;
    private String corrAns;
}

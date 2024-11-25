package ru.codeline.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codeline.dto.forLecture.SectionRequest;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureRequest {
    private Integer numInSeq;
    private String title;
    private String description;
    private List<SectionRequest> sections;
}


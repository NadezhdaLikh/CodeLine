package ru.codeline.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponse {
    private UUID id;
    private Integer numInSeq;
    private String title;
    private String description;
    private Integer numOfSections;
}

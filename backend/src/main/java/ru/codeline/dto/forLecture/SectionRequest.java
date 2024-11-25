package ru.codeline.dto.forLecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionRequest {
    private Integer numInSeq;
    private String title;
    private String content;
}

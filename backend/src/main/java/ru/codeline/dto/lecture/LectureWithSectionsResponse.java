package ru.codeline.dto.lecture;

import lombok.*;
import ru.codeline.dto.forLecture.SectionResponse;
import ru.codeline.models.course.Lecture;
import ru.codeline.models.course.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureWithSectionsResponse {
    private UUID id;
    private Integer numInSeq;
    private String title;
    private String description;
    private List<SectionResponse> sections;

    public static LectureWithSectionsResponse excludeFromLecture(Lecture lecture) {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        for (Section section : lecture.getSections()) {
            SectionResponse sectionResponse = SectionResponse.builder()
                    .id(section.getId())
                    .numInSeq(section.getNumInSeq())
                    .title(section.getTitle())
                    .content(section.getContent())
                    .build();
            sectionResponses.add(sectionResponse);
        }

        return LectureWithSectionsResponse.builder()
                .id(lecture.getId())
                .numInSeq(lecture.getNumInSeq())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .sections(sectionResponses)
                .build();
    }
}

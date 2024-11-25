package ru.codeline.models.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sections")
public class Section {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "num_in_seq")
    private Integer numInSeq;

    @Column(columnDefinition = "varchar")
    private String title;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(columnDefinition = "text")
    private String content;
}

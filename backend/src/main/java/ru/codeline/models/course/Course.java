package ru.codeline.models.course;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.codeline.models.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "varchar")
    private String title;
    @Column(columnDefinition = "varchar")
    private String language;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User user;

    @Column(name = "num_of_lect", nullable = false)
    @Builder.Default
    private Integer numOfLect = 0;

    // The owning side - the Lecture entity (child), the inverse side - the Course entity (parent)
    @OneToMany(mappedBy = "course",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference
    @ToString.Exclude // Exclude from generated toString() to avoid circular reference
    @Builder.Default
    private List<Lecture> lectures = new ArrayList<>();
}

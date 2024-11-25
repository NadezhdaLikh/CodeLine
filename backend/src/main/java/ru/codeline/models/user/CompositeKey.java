package ru.codeline.models.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codeline.models.course.Course;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CompositeKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /*public CompositeKey(User user, Course course) {
        this.user = user;
        this.course = course;
    }*/
}

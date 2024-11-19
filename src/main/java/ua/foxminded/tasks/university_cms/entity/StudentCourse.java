package ua.foxminded.tasks.university_cms.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "students_courses", schema = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class StudentCourse {

    @EmbeddedId
    private StudentCourseId id;
    
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @NonNull
    private Student student;
    
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    @NonNull
    private Course course;
    
}

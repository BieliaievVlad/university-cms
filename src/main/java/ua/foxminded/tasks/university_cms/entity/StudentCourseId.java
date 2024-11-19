package ua.foxminded.tasks.university_cms.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class StudentCourseId implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Long studentId;
    private Long courseId;
	
    public StudentCourseId(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentCourseId)) return false;
        StudentCourseId that = (StudentCourseId) o;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}

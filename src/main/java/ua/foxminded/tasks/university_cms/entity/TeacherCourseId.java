package ua.foxminded.tasks.university_cms.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class TeacherCourseId implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Long teacherId;
    private Long courseId;
    
    public TeacherCourseId(Long teacherId, Long courseId) {
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherCourseId)) return false;
        TeacherCourseId that = (TeacherCourseId) o;
        return Objects.equals(teacherId, that.teacherId) &&
               Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, courseId);
    }
}

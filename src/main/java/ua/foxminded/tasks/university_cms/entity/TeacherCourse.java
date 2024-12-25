package ua.foxminded.tasks.university_cms.entity;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "teachers_courses", schema = "university")
@Getter
@Setter
@NoArgsConstructor
public class TeacherCourse {

	@EmbeddedId
	private TeacherCourseId id;

	@ManyToOne
	@MapsId("teacherId")
	@JoinColumn(name = "teacher_id")
	@NonNull
	private Teacher teacher;

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	@NonNull
	private Course course;
	
	public TeacherCourse(Teacher teacher, Course course) {
	    this.teacher = teacher;
	    this.course = course;
	    this.id = new TeacherCourseId(teacher.getId(), course.getId());
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    TeacherCourse that = (TeacherCourse) o;
	    return Objects.equals(teacher, that.teacher) && Objects.equals(course, that.course);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(teacher, course);
	}

}


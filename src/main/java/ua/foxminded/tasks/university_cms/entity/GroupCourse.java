package ua.foxminded.tasks.university_cms.entity;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "groups_courses", schema = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupCourse {

	@EmbeddedId
	private GroupCourseId id;

	@ManyToOne
	@MapsId("groupId")
	@JoinColumn(name = "group_id")
	@NonNull
	private Group group;

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	@NonNull
	private Course course;
	
	public GroupCourse(Group group, Course course) {
		this.group = group;
		this.course = course;
		this.id = new GroupCourseId(group.getId(), course.getId());
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    GroupCourse that = (GroupCourse) o;
	    return Objects.equals(group, that.group) && Objects.equals(course, that.course);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(group, course);
	}

}

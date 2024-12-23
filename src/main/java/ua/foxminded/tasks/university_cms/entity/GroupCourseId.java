package ua.foxminded.tasks.university_cms.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class GroupCourseId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long groupId;
	private Long courseId;

	public GroupCourseId(Long groupId, Long courseId) {
		this.groupId = groupId;
		this.courseId = courseId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GroupCourseId))
			return false;
		GroupCourseId that = (GroupCourseId) o;
		return Objects.equals(groupId, that.groupId) && Objects.equals(courseId, that.courseId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupId, courseId);
	}
}

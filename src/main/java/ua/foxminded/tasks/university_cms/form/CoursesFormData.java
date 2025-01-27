package ua.foxminded.tasks.university_cms.form;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursesFormData {
	
	private List<TeacherCourse> teacherCourses = Collections.emptyList();
	private Map<Course, List<Group>> courseGroupsMap = Collections.emptyMap();

}

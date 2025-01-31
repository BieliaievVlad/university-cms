package ua.foxminded.tasks.university_cms.form;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeachersFormData {
	
	private List<Teacher> teachers = Collections.emptyList();
	private Map<Teacher, List<TeacherCourse>> teacherCoursesMap = Collections.emptyMap();

}

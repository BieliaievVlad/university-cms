package ua.foxminded.tasks.university_cms.form;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCoursesFormData {
	
	private Group group;
	private List<Course> filteredCourses;

}

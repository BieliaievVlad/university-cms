package ua.foxminded.tasks.university_cms.form;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.Teacher;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesFormData {
	
    private List<Course> courses = Collections.emptyList();
    private List<Group> groups = Collections.emptyList();
    private List<Teacher> teachers = Collections.emptyList();
    private List<Student> students = Collections.emptyList();

}

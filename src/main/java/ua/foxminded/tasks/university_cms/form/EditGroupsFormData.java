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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditGroupsFormData {
	
	private Course course;
	private Map<Long, List<Group>> courseGroupsMap = Collections.emptyMap();
	private List<Group> filteredGroups = Collections.emptyList();

}

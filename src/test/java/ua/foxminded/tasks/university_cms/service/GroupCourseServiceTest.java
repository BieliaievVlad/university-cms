package ua.foxminded.tasks.university_cms.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.GroupCourseId;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;

@SpringBootTest
@ActiveProfiles("test")
class GroupCourseServiceTest {

	@MockBean
	GroupCourseRepository repository;

	@Autowired
	GroupCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		when(repository.save(groupCourse)).thenReturn(groupCourse);

		service.save(groupCourse);

		verify(repository, times(1)).save(groupCourse);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		when(repository.findById(id)).thenReturn(Optional.of(groupCourse));
		doNothing().when(repository).delete(groupCourse);

		service.delete(groupCourse);

		verify(repository, times(1)).delete(groupCourse);
	}

}

package ua.foxminded.tasks.university_cms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;

@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;

	@Autowired
	GroupService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		Group group = new Group(1L, "Group_1", 10L);
		List<Group> expectedList = Arrays.asList(group);
		when(groupRepository.findAll()).thenReturn(expectedList);

		List<Group> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(groupRepository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Group expected = new Group(1L, "Course_1", 10L);
		Optional<Group> optGroup = Optional.of(expected);
		when(groupRepository.findById(searchId)).thenReturn(optGroup);

		Group actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(groupRepository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		Group group = new Group(1L, "Course_1", 10L);
		when(groupRepository.save(group)).thenReturn(group);

		service.save(group);

		verify(groupRepository, times(1)).save(group);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Group group = new Group(1L, "Course_1", 10L);
		when(groupRepository.findById(id)).thenReturn(Optional.of(group));
		doNothing().when(groupRepository).delete(group);

		service.delete(id);

		verify(groupRepository, times(1)).delete(group);
	}
	
	@Test
	void findByCourse_ValidValue_ReturnsExpected() {
		
		Course course = new Course(1L, "Course_Name");
		Group group = new Group(1L, "Group_Name", 10L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		when(groupCourseRepository.findByCourseId(course.getId())).thenReturn(List.of(groupCourse));
		
		List<Group> actual = service.findByCourse(course);
		
		verify(groupCourseRepository, times(1)).findByCourseId(course.getId());
		assertEquals(List.of(group), actual);
	}
	
	@Test
	void saveGroup_Valid_Value_CalledMethods() {
		
		String name = "New_Group_Name";
		Group newGroup = new Group("New_Group_Name");
		
		when(groupRepository.save(any(Group.class))).thenReturn(newGroup);
		
		service.saveGroup(name);
		
		verify(groupRepository, times(1)).save(any(Group.class));
	}
}

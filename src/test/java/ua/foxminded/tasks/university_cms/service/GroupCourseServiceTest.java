package ua.foxminded.tasks.university_cms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
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
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;

@SpringBootTest
@ActiveProfiles("test")
class GroupCourseServiceTest {
	
	@MockBean
	CourseRepository courseRepository;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;

	@Autowired
	GroupCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		when(groupCourseRepository.save(groupCourse)).thenReturn(groupCourse);

		service.save(groupCourse);

		verify(groupCourseRepository, times(1)).save(groupCourse);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		when(groupCourseRepository.findById(id)).thenReturn(Optional.of(groupCourse));
		doNothing().when(groupCourseRepository).delete(groupCourse);

		service.delete(groupCourse);

		verify(groupCourseRepository, times(1)).delete(groupCourse);
	}
	
	@Test
	void findByGroup_ValidValue_CalledMethodsAndReturnsExpected() {
		fail("Not yet implemented");
	}
	
	@Test
	void updateGroupCourse_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.save(any(GroupCourse.class))).thenReturn(groupCourse);
		
		service.updateGroupCourse(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).save(any(GroupCourse.class));
	}
	
	@Test
	void deleteGroupCourse_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.findById(groupCourse.getId())).thenReturn(Optional.of(groupCourse));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		
		service.deleteGroupCourse(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).findById(groupCourse.getId());
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
	}
	
	@Test
	void addStudentToGroup_ValidValue_CalledMethods() {
		fail("Not yet implemented");
	}
	
	@Test
	void removeStudentFromGroup_ValidValue_CalledMethods() {
		fail("Not yet implemented");
	}

}

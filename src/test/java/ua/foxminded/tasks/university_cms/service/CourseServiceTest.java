package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@SpringBootTest
@ActiveProfiles("test")
class CourseServiceTest {

	@MockBean
	CourseRepository courseRepository;
	
	@MockBean
	TeacherRepository teacherRepository;
	
	@MockBean
	TeacherCourseRepository teacherCourseRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;
	
	@MockBean
	SecurityService securityService;

	@Autowired
	CourseService courseService;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		Course course = new Course(1L, "Course_1");
		List<Course> expectedList = Arrays.asList(course);
		when(courseRepository.findAll()).thenReturn(expectedList);

		List<Course> actualList = courseService.findAll();

		assertEquals(expectedList, actualList);
		verify(courseRepository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Course expected = new Course(1L, "Course_1");
		Optional<Course> optCourse = Optional.of(expected);
		when(courseRepository.findById(searchId)).thenReturn(optCourse);

		Course actual = courseService.findById(searchId);

		assertEquals(expected, actual);
		verify(courseRepository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		when(courseRepository.save(course)).thenReturn(course);

		courseService.save(course);

		verify(courseRepository, times(1)).save(course);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Course course = new Course(1L, "Course_1");
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		doNothing().when(courseRepository).delete(course);

		courseService.delete(id);

		verify(courseRepository, times(1)).delete(course);
	}
	
	@Test
	void prepareTeacherCourseList_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Course course = new Course(1L, "Course_Name");
		List<Course> courses = List.of(course);
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		List<TeacherCourse> expected = List.of(teacherCourse);

		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		
		List<TeacherCourse> actual = courseService.prepareTeacherCourseList(courses);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherRepository, times(1)).findById(id);
	}
	
	@Test
	void prepareCourseGroupsMap_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Course course = new Course(1L, "Course_Name");
		Group group = new Group(1L, "Group_Name", 10L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		List<Course> courses = List.of(course);
		List<Group> groups = List.of(group);
		List<GroupCourse> groupCourses = List.of(groupCourse);
		Map<Course, List<Group>> expected = new HashMap<>();
		expected.put(course, groups);
		
		when(groupCourseRepository.findByCourseId(id)).thenReturn(groupCourses);
		
		Map<Course, List<Group>> actual = courseService.prepareCourseGroupsMap(courses);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}
	
}

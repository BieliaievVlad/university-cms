package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@SpringBootTest
@ActiveProfiles("test")
class FormServiceTest {
	
	@MockBean
	CourseRepository courseRepository;
	
	@MockBean
	TeacherRepository teacherRepository;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	TeacherCourseRepository teacherCourseRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;
	
	@Autowired 
	FormService formService;

	@Test
	void prepareCoursesData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Course course = new Course(1L, "Course_Name");
		Group group = new Group(1L, "Group_Name", 10L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		List<Course> courses = List.of(course);
		List<Group> groups = List.of(group);
		List<GroupCourse> groupCourses = List.of(groupCourse);
		List<TeacherCourse> teacherCourses = List.of(teacherCourse);		
		Map<Course, List<Group>> courseGroupsMap = new HashMap<>();
		courseGroupsMap.put(course, groups);
		
		CoursesFormData expected = new CoursesFormData(teacherCourses, courseGroupsMap);
		
		when(courseRepository.findAll()).thenReturn(courses);
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		when(groupCourseRepository.findByCourseId(id)).thenReturn(groupCourses);
		
		CoursesFormData actual = formService.prepareCoursesFormData();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(courseRepository, times(1)).findAll();
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}

	@Test
	void prepareCourseFormData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		Map<Course, List<Group>> courseGroupsMap = Map.of(course, List.of(group));
		CourseFormData expected = new CourseFormData(teacherCourse, List.of(group), List.of(teacher), courseGroupsMap);
		
		when(courseRepository.findAll()).thenReturn(List.of(course));
		when(teacherRepository.findAll()).thenReturn(List.of(teacher));
		when(groupRepository.findAll()).thenReturn(List.of(group));
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		when(groupCourseRepository.findByCourseId(id)).thenReturn(List.of(groupCourse));
		
		CourseFormData actual = formService.prepareCourseFormData(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(courseRepository, times(1)).findAll();
		verify(teacherRepository, times(1)).findAll();
		verify(groupRepository, times(1)).findAll();
		verify(courseRepository, times(1)).findById(id);
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}

	@Test
	void prepareEditGroupsFormData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Group group1 = new Group(1L, "Group_Name", 10L);
		Group group2 = new Group(2L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group1, course);
		EditGroupsFormData expected = new EditGroupsFormData(course, Map.of(id, List.of(group1)), List.of(group2));
		
		when(groupRepository.findAll()).thenReturn(List.of(group1, group2));
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		when(groupCourseRepository.findByCourseId(id)).thenReturn(List.of(groupCourse));
		
		EditGroupsFormData actual = formService.prepareEditGroupsFormData(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupRepository, times(1)).findAll();
		verify(courseRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}

	@Test
	void testPrepareGroupsFormData() {
		fail("Not yet implemented");
	}

	@Test
	void testPrepareEditCoursesFormData() {
		fail("Not yet implemented");
	}

}

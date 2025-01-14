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
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
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
	GroupRepository groupRepository;

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
		
		CoursesData expected = new CoursesData(teacherCourses, courseGroupsMap);
		
		when(courseRepository.findAll()).thenReturn(courses);
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		when(groupCourseRepository.findByCourseId(id)).thenReturn(groupCourses);
		
		CoursesData actual = courseService.prepareCoursesData();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(courseRepository, times(1)).findAll();
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}
	
	@Test
	void saveCourse_ValidValues_CalledMethods() {
		
		String courseName = "Course_Name";
		Long id = 1L;
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		Course course = new Course(1L, "Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);

	    when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
	        													  Course savedCourse = invocation.getArgument(0);
	        													  savedCourse.setId(id);
	        													  return savedCourse;
	    														  });
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(teacherCourseRepository.save(any(TeacherCourse.class))).thenReturn(teacherCourse);
	
		courseService.saveCourse(courseName, id);
		
		verify(courseRepository, times(1)).save(any(Course.class));
		verify(teacherRepository, times(1)).findById(id);
		verify(courseRepository, times(1)).findById(id);
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));	
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
		
		CourseFormData actual = courseService.prepareCourseFormData(id);
		
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
	void updateTeacherCourse_ValidValue_CalledMethods() {
		
		Long id = 1L;
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		TeacherCourse newTeacherCourse = new TeacherCourse();
		
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(teacherCourseRepository.findById(teacherCourse.getId())).thenReturn(Optional.of(teacherCourse))
																	 .thenReturn(Optional.empty());
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		when(teacherCourseRepository.save(any(TeacherCourse.class))).thenReturn(newTeacherCourse);
		
		courseService.updateTeacherCourse(teacherCourse);
		
		verify(courseRepository, times(1)).findById(id);
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherCourseRepository, times(2)).findById(teacherCourse.getId());
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));
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
		
		EditGroupsFormData actual = courseService.prepareEditGroupsFormData(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupRepository, times(1)).findAll();
		verify(courseRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
	}
	
	@Test
	void updateGroups_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.save(any(GroupCourse.class))).thenReturn(groupCourse);
		
		courseService.updateGroups(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).save(any(GroupCourse.class));
	}
	
	@Test
	void deleteGroup_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.findById(groupCourse.getId())).thenReturn(Optional.of(groupCourse));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		
		courseService.deleteGroupFromCourse(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).findById(groupCourse.getId());
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
	}
	
	@Test
	void deleteCourse_ValidValue_CalledMethods() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(groupCourseRepository.findByCourseId(id)).thenReturn(List.of(groupCourse));
		when(groupCourseRepository.findById(groupCourse.getId())).thenReturn(Optional.of(groupCourse));
		when(teacherCourseRepository.findById(teacherCourse.getId())).thenReturn(Optional.of(teacherCourse));
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		doNothing().when(courseRepository).delete(any(Course.class));
		
		courseService.deleteCourse(id);
		
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
		verify(groupCourseRepository, times(1)).findById(groupCourse.getId());
		verify(teacherCourseRepository, times(1)).findById(teacherCourse.getId());
		verify(courseRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(courseRepository, times(1)).delete(any(Course.class));
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

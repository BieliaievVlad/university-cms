package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditCoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.form.GroupsFormData;
import ua.foxminded.tasks.university_cms.form.SchedulesFormData;
import ua.foxminded.tasks.university_cms.form.TeachersFormData;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;
import ua.foxminded.tasks.university_cms.util.DateUtil;

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
	StudentRepository studentRepository;
	
	@MockBean
	ScheduleRepository scheduleRepository;
	
	@MockBean
	TeacherCourseRepository teacherCourseRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;
	
	@MockBean
	DateUtil dateUtil;
	
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
	void prepareGroupsFormData_ValidValue_CalledMethodsAndReturnsExpected() {
	
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupsFormData expected = new GroupsFormData(List.of(group), Map.of(group, List.of(course)));
		
		when(groupRepository.findAll()).thenReturn(List.of(group));
		when(groupCourseRepository.findByGroupId(id)).thenReturn(List.of(new GroupCourse(group, course)));
		
		GroupsFormData actual = formService.prepareGroupsFormData();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupRepository, times(1)).findAll();
		verify(groupCourseRepository, times(1)).findByGroupId(id);
		
		
	}

	@Test
	void prepareEditCoursesFormData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course1 = new Course(1L, "Course_Name1");
		Course course2 = new Course(2L, "Course_Name2");
		EditCoursesFormData expected = new EditCoursesFormData(group, List.of(course2));
		
		when(groupRepository.findById(id)).thenReturn(Optional.of(group));
		when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
		when(groupCourseRepository.findByGroupId(id)).thenReturn(List.of(new GroupCourse(group, course1)));
		
		EditCoursesFormData actual = formService.prepareEditCoursesFormData(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupRepository, times(1)).findById(id);
		verify(courseRepository, times(1)).findAll();
		verify(groupCourseRepository, times(1)).findByGroupId(id);
		
	}
	
	@Test
	void prepareStudentsFormData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Student student1 = new Student("First_Name1", "Last_Name1");
		Student student2 = new Student("First_Name2", "Last_Name2");
		student1.setId(1L);
		student2.setId(2L);
		List<Student> resultList = List.of(student2, student1);
		List<Student> expectedSortedList = List.of(student1, student2);
		
		when(studentRepository.findAll()).thenReturn(resultList);
		
		List<Student> actual = formService.prepareStudentsFormData();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expectedSortedList);
		verify(studentRepository, times(1)).findAll();
	}
	
	@Test
	void prepareTeachersFormData_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Teacher teacher1 = new Teacher("First_Name1", "LastName1");
		teacher1.setId(1L);
		Teacher teacher2 = new Teacher("First_Name2", "LastName2");
		teacher2.setId(2L);
		Course course1 = new Course(1L, "Course_Name1");
		Course course2 = new Course(2L, "Course_Name2");
		TeacherCourse teacherCourse1 = new TeacherCourse(teacher1, course1);
		TeacherCourse teacherCourse2 = new TeacherCourse(teacher2, course2);
		Map<Teacher, List<TeacherCourse>> map = new HashMap<>();
		map.put(teacher1, List.of(teacherCourse1));
		map.put(teacher2, List.of(teacherCourse2));
		TeachersFormData expected = new TeachersFormData();
		expected.setTeachers(List.of(teacher1, teacher2));
		expected.setTeacherCoursesMap(map);
		
		when(teacherRepository.findAll()).thenReturn(List.of(teacher2, teacher1));
		when(teacherCourseRepository.findAll()).thenReturn(List.of(teacherCourse1, teacherCourse2));
		when(teacherCourseRepository.findByTeacherId(anyLong())).thenReturn(List.of(teacherCourse1))
																.thenReturn(List.of(teacherCourse2));
		
		TeachersFormData actual = formService.prepareTeachersFormData();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(teacherRepository, times(1)).findAll();
		verify(teacherCourseRepository, times(2)).findByTeacherId(anyLong());
	}
	
	@Test
	void prepareSchedulesFormData_ValidValue_CalledMethodsAndReturnsExpected() {

		List<Course> courses = List.of(new Course(1L, "Course_Name"));
		List<Group> groups = List.of(new Group(1L, "Group_Name", 10L));
		List<Teacher> teachers = List.of(new Teacher("First_Name", "LastName"));
		List<Student> students = List.of(new Student("First_Name", "Last_Name"));
		SchedulesFormData expected = new SchedulesFormData(courses, groups, teachers, students);
		
		when(courseRepository.findAll()).thenReturn(courses);
		when(groupRepository.findAll()).thenReturn(groups);
		when(teacherRepository.findAll()).thenReturn(teachers);
		when(studentRepository.findAll()).thenReturn(students);
		
		SchedulesFormData actual = formService.prepareSchedulesForm();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(courseRepository, times(1)).findAll();
		verify(groupRepository, times(1)).findAll();
		verify(teacherRepository, times(1)).findAll();
		verify(studentRepository, times(1)).findAll();
	}
	
	@Test
	void prepareEditScheduleCourseForm_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course1 = new Course(1L, "Course_name1");
		Course course2 = new Course(2L, "Course_name2");
		Group group = new Group(1L, "Group_Name");
		Schedule schedule = new Schedule(1L, dateTime, group, course1);
		List<Course> expected = List.of(course2);
		
		when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
		when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
		
		List<Course> actual = formService.prepareEditScheduleCourseForm(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(scheduleRepository, times(1)).findById(anyLong());
		verify(courseRepository, times(1)).findAll();
	}
	
	@Test
	void prepareEditScheduleGroupForm_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name");
		Group group1 = new Group(1L, "Group_Name1");
		Group group2 = new Group(2L, "Group_Name1");
		Schedule schedule = new Schedule(1L, dateTime, group1, course);
		List<Group> expected = List.of(group2);
		
		when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
		when(groupRepository.findAll()).thenReturn(List.of(group1, group2));
		
		List<Group> actual = formService.prepareEditScheduleGroupForm(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(scheduleRepository, times(1)).findById(anyLong());
		verify(groupRepository, times(1)).findAll();
	}
	
	@Test
	void prepareScheduleListForStudent_ValidValue_CalledMethodsAndReturnsExpected() {
		
		LocalDateTime dateTime1 = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course1 = new Course(1L, "Course_name1");
		Group group1 = new Group(1L, "Group_Name1");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 1, 31, 15, 30);
		Course course2 = new Course(2L, "Course_name2");
		Group group2 = new Group(2L, "Group_Name2");
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);
		Student student = new Student("First_Name", "Last_name", group2);
		student.setId(1L);
		
		List<LocalDate> dates = List.of(dateTime1.toLocalDate(), dateTime2.toLocalDate());
		
		List<Schedule> expected = List.of(schedule2);
		
		when(groupCourseRepository.findByGroupId(anyLong())).thenReturn(List.of(new GroupCourse(group2, course2)));
		when(dateUtil.getDateListOfWeek()).thenReturn(dates);
		when(scheduleRepository.findAll()).thenReturn(List.of(schedule1, schedule2));
		
		List<Schedule> actual = formService.prepareScheduleListForStudent(student);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupCourseRepository, times(1)).findByGroupId(anyLong());
		verify(scheduleRepository, times(1)).findAll();
	}
	
	@Test
	void prepareScheduleListForGroup_ValidValue_CalledMethodsAndReturnsExpected() {
		
		LocalDateTime dateTime1 = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course1 = new Course(1L, "Course_name1");
		Group group1 = new Group(1L, "Group_Name1");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 1, 31, 15, 30);
		Course course2 = new Course(2L, "Course_name2");
		Group group2 = new Group(2L, "Group_Name2");
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);
		List<Schedule> expected = List.of(schedule2);
		
		List<LocalDate> dates = List.of(dateTime1.toLocalDate(), dateTime2.toLocalDate());
		
		when(scheduleRepository.findAll()).thenReturn(List.of(schedule1, schedule2));
		when(dateUtil.getDateListOfWeek()).thenReturn(dates);
		
		List<Schedule> actual = formService.prepareScheduleListForGroup(List.of(course2));
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(scheduleRepository, times(1)).findAll();
	}
	
	@Test
	void prepareScheduleListForTeacher_ValidValue_CalledMethodsAndReturnsExpected() {
		
		LocalDateTime dateTime1 = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course1 = new Course(1L, "Course_name1");
		Group group1 = new Group(1L, "Group_Name1");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 1, 31, 15, 30);
		Course course2 = new Course(2L, "Course_name2");
		Group group2 = new Group(2L, "Group_Name2");
		Teacher teacher2 = new Teacher("First_Name", "Last_Name");
		teacher2.setId(1L);
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);
		
		List<LocalDate> dates = List.of(dateTime1.toLocalDate(), dateTime2.toLocalDate());
		
		List<TeacherCourse> list = List.of(new TeacherCourse(teacher2, course2));
		List<Schedule> expected = List.of(schedule2);
		
		when(scheduleRepository.findAll()).thenReturn(List.of(schedule1, schedule2));
		when(dateUtil.getDateListOfWeek()).thenReturn(dates);
		
		List<Schedule> actual = formService.prepareScheduleListForTeacher(list);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(scheduleRepository, times(1)).findAll();
	}
}

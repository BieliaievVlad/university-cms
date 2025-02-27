package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.entity.TeacherCourseId;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@SpringBootTest
@ActiveProfiles("test")
class TeacherCourseServiceTest {

	@MockBean
	CourseRepository courseRepository;
	
	@MockBean
	TeacherRepository teacherRepository;
	
	@MockBean
	TeacherCourseRepository teacherCourseRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;
	
	@MockBean
	ScheduleRepository scheduleRepository;

	@Autowired
	TeacherCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		when(teacherCourseRepository.findById(id)).thenReturn(Optional.empty());
		when(teacherCourseRepository.save(teacherCourse)).thenReturn(teacherCourse);

		service.save(teacherCourse);

		verify(teacherCourseRepository, times(1)).findById(id);
	    verify(teacherCourseRepository, times(1)).save(argThat(savedCourse -> 
        										  savedCourse.getTeacher().equals(teacherCourse.getTeacher()) &&
        										  savedCourse.getCourse().equals(teacherCourse.getCourse())
	    										  ));

	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		when(teacherCourseRepository.findById(id)).thenReturn(Optional.of(teacherCourse));
		doNothing().when(teacherCourseRepository).delete(teacherCourse);

		service.delete(teacherCourse);

	    verify(teacherCourseRepository, times(1)).delete(argThat(deletedCourse -> 
		  										    deletedCourse.getTeacher().equals(teacherCourse.getTeacher()) &&
		  										    deletedCourse.getCourse().equals(teacherCourse.getCourse())
	    										    ));
	}
	
	@Test
	void findByCourseId_ValidValue_ReturnsExpectedAndCalledOnce() {
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		when(teacherCourseRepository.findByCourseId(course.getId())).thenReturn(teacherCourse);
		
		TeacherCourse actual = teacherCourseRepository.findByCourseId(course.getId());
		
		verify(teacherCourseRepository, times(1)).findByCourseId(course.getId());
		assertEquals(teacherCourse, actual);
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
	
		service.saveCourse(courseName, id);
		
		verify(courseRepository, times(1)).save(any(Course.class));
		verify(teacherRepository, times(1)).findById(id);
		verify(courseRepository, times(1)).findById(id);
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));	
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
		
		service.updateTeacherCourse(teacherCourse);
		
		verify(courseRepository, times(1)).findById(id);
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(teacherCourseRepository, times(2)).findById(teacherCourse.getId());
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));
	}
	
	@Test
	void deleteTeacherCourse_ValidValue_CalledMethods() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		GroupCourse groupCourse = new GroupCourse(group, course);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule schedule = new Schedule(date, group, course);
		schedule.setId(1L);
		
		when(teacherCourseRepository.findByCourseId(id)).thenReturn(teacherCourse);
		when(groupCourseRepository.findByCourseId(id)).thenReturn(List.of(groupCourse));
		when(scheduleRepository.findByCourseId(anyLong())).thenReturn(List.of(schedule));
		when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
		doNothing().when(scheduleRepository).delete(any(Schedule.class));
		when(groupCourseRepository.findById(groupCourse.getId())).thenReturn(Optional.of(groupCourse));
		when(teacherCourseRepository.findById(teacherCourse.getId())).thenReturn(Optional.of(teacherCourse));
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		doNothing().when(courseRepository).delete(any(Course.class));
		
		service.deleteTeacherCourse(id);
		
		verify(teacherCourseRepository, times(1)).findByCourseId(id);
		verify(groupCourseRepository, times(1)).findByCourseId(id);
		verify(scheduleRepository, times(1)).findByCourseId(anyLong());
		verify(scheduleRepository, times(1)).findById(anyLong());
		verify(scheduleRepository, times(1)).delete(any(Schedule.class));
		verify(groupCourseRepository, times(1)).findById(groupCourse.getId());
		verify(teacherCourseRepository, times(1)).findById(teacherCourse.getId());
		verify(courseRepository, times(1)).findById(id);
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(courseRepository, times(1)).delete(any(Course.class));
	}
	
	@Test
	void deleteCourseFromTeacher_ValidValue_CalledMethods() {
		
		Long courseId = 1L;
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		when(teacherCourseRepository.findByCourseId(anyLong())).thenReturn(teacherCourse);
		when(teacherCourseRepository.findById(any())).thenReturn(Optional.of(teacherCourse))
													 .thenReturn(Optional.empty());
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		when(teacherCourseRepository.save(any(TeacherCourse.class))).thenReturn(teacherCourse);
		
		service.deleteCourseFromTeacher(courseId);
		
		verify(courseRepository, times(1)).findById(anyLong());
		verify(teacherCourseRepository, times(1)).findByCourseId(anyLong());
		verify(teacherCourseRepository, times(2)).findById(any());
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));
	}
	
	@Test
	void addCourseToTeacher_ValidValue_CalledMethods() {
		
		Long courseId = 1L;
		Long teacherId = 1L;
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		Course course = new Course(1L, "Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
		when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		when(teacherCourseRepository.findByCourseId(anyLong())).thenReturn(teacherCourse);
		when(teacherCourseRepository.findById(any())).thenReturn(Optional.of(teacherCourse))
		 											 .thenReturn(Optional.empty());
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		when(teacherCourseRepository.save(any(TeacherCourse.class))).thenReturn(teacherCourse);
		
		service.addCourseToTeacher(teacherId, courseId);
		
		verify(teacherRepository, times(1)).findById(anyLong());
		verify(courseRepository, times(1)).findById(anyLong());
		verify(teacherCourseRepository, times(1)).findByCourseId(anyLong());
		verify(teacherCourseRepository, times(2)).findById(any());
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));
	}
	
	@Test
	void deleteTeacherAndAssociations_ValidValue_CalledMethods() {

		Long teacherId = 1L;
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		Course course = new Course(1L, "Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
		when(teacherCourseRepository.findByTeacherId(teacherId)).thenReturn(List.of(teacherCourse));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		when(teacherCourseRepository.findByCourseId(anyLong())).thenReturn(teacherCourse);
		when(teacherCourseRepository.findById(any())).thenReturn(Optional.of(teacherCourse))
													 .thenReturn(Optional.empty());
		doNothing().when(teacherCourseRepository).delete(any(TeacherCourse.class));
		when(teacherCourseRepository.save(any(TeacherCourse.class))).thenReturn(teacherCourse);
		when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
		doNothing().when(teacherRepository).delete(any(Teacher.class));
		
		service.deleteTeacherAndAssociations(teacherId);
		
		verify(teacherCourseRepository, times(1)).findByTeacherId(anyLong());
		verify(courseRepository, times(1)).findById(anyLong());
		verify(teacherCourseRepository, times(1)).findByCourseId(anyLong());
		verify(teacherCourseRepository, times(2)).findById(any());
		verify(teacherCourseRepository, times(1)).delete(any(TeacherCourse.class));
		verify(teacherCourseRepository, times(1)).save(any(TeacherCourse.class));
		verify(teacherRepository, times(1)).findById(anyLong());
		verify(teacherRepository, times(1)).delete(any(Teacher.class));
	}
	
	@Test
	void prepareFilteredCoursesList_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long teacherId = 1L;
		Teacher teacher1 = new Teacher(1L, "First_Name1", "Last_Name1");
		Course course1 = new Course(1L, "Course_Name1");
		Teacher teacher2 = new Teacher(2L, "First_Name2", "Last_Name2");
		Course course2 = new Course(2L, "Course_Name2");
		TeacherCourse teacherCourse1 = new TeacherCourse(teacher1, course1);
		TeacherCourse teacherCourse2 = new TeacherCourse(teacher2, course2);
		List<TeacherCourse> expected = List.of(teacherCourse2);
		
		when(teacherCourseRepository.findAll()).thenReturn(List.of(teacherCourse1, teacherCourse2));
		when(teacherCourseRepository.findByTeacherId(anyLong())).thenReturn(List.of(teacherCourse1));
		
		List<TeacherCourse> actual = service.prepareFilteredCoursesList(teacherId);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(teacherCourseRepository, times(1)).findAll();
		verify(teacherCourseRepository, times(1)).findByTeacherId(anyLong());
		
		
	}

}

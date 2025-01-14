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
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.entity.User;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@SpringBootTest
@ActiveProfiles("test")
class TeacherServiceTest {

	@MockBean
	TeacherRepository teacherRepository;
	
	@MockBean
	TeacherCourseRepository teacherCourseRepository;

	@Autowired
	TeacherService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		User user = new User("username", "password");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName", user);
		List<Teacher> expectedTeacher = Arrays.asList(teacher);
		when(teacherRepository.findAll()).thenReturn(expectedTeacher);

		List<Teacher> actualTeacher = service.findAll();

		assertEquals(expectedTeacher, actualTeacher);
		verify(teacherRepository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Teacher expected = new Teacher("FirstName", "LastName");
		Optional<Teacher> optTeacher = Optional.of(expected);
		when(teacherRepository.findById(searchId)).thenReturn(optTeacher);

		Teacher actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(teacherRepository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		User user = new User("username", "password");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName", user);
		when(teacherRepository.save(teacher)).thenReturn(teacher);

		service.save(teacher);

		verify(teacherRepository, times(1)).save(teacher);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Teacher teacher = new Teacher("FirstName", "LastName");
		when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
		doNothing().when(teacherRepository).delete(teacher);

		service.delete(id);

		verify(teacherRepository, times(1)).delete(teacher);
	}
	
	@Test
	void findByCourse_ValidValue_ReturnsExpected() {
		
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		when(teacherCourseRepository.findByCourseId(course.getId())).thenReturn(teacherCourse);
		when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
		
		Teacher actual = service.findByCourse(course);
		
		verify(teacherRepository, times(1)).findById(teacher.getId());
		verify(teacherCourseRepository, times(1)).findByCourseId(course.getId());
		assertEquals(teacher, actual);
		
	}

}

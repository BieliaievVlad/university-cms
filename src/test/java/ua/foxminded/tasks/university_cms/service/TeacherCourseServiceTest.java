package ua.foxminded.tasks.university_cms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.entity.TeacherCourseId;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;

@SpringBootTest
@ActiveProfiles("test")
class TeacherCourseServiceTest {

	@MockBean
	TeacherCourseRepository repository;

	@Autowired
	TeacherCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		when(repository.findById(id)).thenReturn(Optional.empty());
		when(repository.save(teacherCourse)).thenReturn(teacherCourse);

		service.save(teacherCourse);

		verify(repository, times(1)).findById(id);
	    verify(repository, times(1)).save(argThat(savedCourse -> 
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
		when(repository.findById(id)).thenReturn(Optional.of(teacherCourse));
		doNothing().when(repository).delete(teacherCourse);

		service.delete(teacherCourse);

	    verify(repository, times(1)).delete(argThat(deletedCourse -> 
		  										    deletedCourse.getTeacher().equals(teacherCourse.getTeacher()) &&
		  										    deletedCourse.getCourse().equals(teacherCourse.getCourse())
	    										    ));
	}
	
	@Test
	void findByCourseId_ValidValue_ReturnsExpectedAndCalledOnce() {
		Course course = new Course(1L, "Course_Name");
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		when(repository.findByCourseId(course.getId())).thenReturn(teacherCourse);
		
		TeacherCourse actual = repository.findByCourseId(course.getId());
		
		verify(repository, times(1)).findByCourseId(course.getId());
		assertEquals(teacherCourse, actual);
	}

}

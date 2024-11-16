package ua.foxminded.tasks.university_cms.db.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.db.entity.Course;
import ua.foxminded.tasks.university_cms.db.entity.Student;
import ua.foxminded.tasks.university_cms.db.entity.StudentCourse;
import ua.foxminded.tasks.university_cms.db.entity.StudentCourseId;
import ua.foxminded.tasks.university_cms.db.repository.StudentCourseRepository;

@SpringBootTest
@ActiveProfiles("test")
class StudentCourseServiceTest {
	
    @MockBean
    StudentCourseRepository repository;
    
    @Autowired
    StudentCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Student student = new Student("FirstName", "LastName");
		StudentCourse studentCourse = new StudentCourse(student, course);
		when(repository.save(studentCourse)).thenReturn(studentCourse);
		
		service.save(studentCourse);
		
		verify(repository, times(1)).save(studentCourse);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Student student = new Student("FirstName", "LastName");
		StudentCourse studentCourse = new StudentCourse(student, course);
		StudentCourseId id = new StudentCourseId(studentCourse.getStudent().getId(), studentCourse.getCourse().getId());
		when(repository.findById(id)).thenReturn(Optional.of(studentCourse));
		doNothing().when(repository).delete(studentCourse);
		
		service.delete(studentCourse);
		
		verify(repository, times(1)).delete(studentCourse);
	}

}

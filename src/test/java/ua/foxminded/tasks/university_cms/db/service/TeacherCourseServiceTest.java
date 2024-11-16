package ua.foxminded.tasks.university_cms.db.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.db.entity.Course;
import ua.foxminded.tasks.university_cms.db.entity.Teacher;
import ua.foxminded.tasks.university_cms.db.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.db.entity.TeacherCourseId;
import ua.foxminded.tasks.university_cms.db.repository.TeacherCourseRepository;

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
		Teacher teacher = new Teacher("FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		when(repository.save(teacherCourse)).thenReturn(teacherCourse);
		
		service.save(teacherCourse);
		
		verify(repository, times(1)).save(teacherCourse);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Teacher teacher = new Teacher("FirstName", "LastName");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		when(repository.findById(id)).thenReturn(Optional.of(teacherCourse));
		doNothing().when(repository).delete(teacherCourse);
		
		service.delete(teacherCourse);
		
		verify(repository, times(1)).delete(teacherCourse);
	}

}

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
import ua.foxminded.tasks.university_cms.repository.CourseRepository;

@SpringBootTest
@ActiveProfiles("test")
class CourseServiceTest {

    @MockBean
    CourseRepository repository;
    
    @Autowired
    CourseService service;
	

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {
		
        Course course = new Course(1L, "Course_1");
        List<Course> expectedList = Arrays.asList(course);
        when(repository.findAll()).thenReturn(expectedList);
        
        List<Course> actualList = service.findAll();
        
        assertEquals(expectedList, actualList);
        verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {
		
		Long searchId = 1L;
		Course expected = new Course(1L, "Course_1");
		Optional<Course> optCourse = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optCourse);
		
		Course actual = service.findById(searchId);
		
		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);
		
	}

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		when(repository.save(course)).thenReturn(course);
		
		service.save(course);
		
		verify(repository, times(1)).save(course);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Course course = new Course(1L, "Course_1");
		when(repository.findById(id)).thenReturn(Optional.of(course));
		doNothing().when(repository).delete(course);
		
		service.delete(id);
		
		verify(repository, times(1)).delete(course);
	}

}

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

import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceTest {

	@MockBean
	StudentRepository repository;

	@Autowired
	StudentService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {
		Student student = new Student("FirstName", "LastName");
		List<Student> expectedList = Arrays.asList(student);
		when(repository.findAll()).thenReturn(expectedList);

		List<Student> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Student expected = new Student("FirstName", "LastName");
		Optional<Student> optStudent = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optStudent);

		Student actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);
	}

	@Test
	void save_ValidValue_CalledOnce() {
		Student student = new Student(1L, "FirstName", "LastName");
		when(repository.save(student)).thenReturn(student);

		service.save(student);

		verify(repository, times(1)).save(student);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Student student = new Student("FirstName", "LastName");
		when(repository.findById(id)).thenReturn(Optional.of(student));
		doNothing().when(repository).delete(student);

		service.delete(id);

		verify(repository, times(1)).delete(student);
	}

}

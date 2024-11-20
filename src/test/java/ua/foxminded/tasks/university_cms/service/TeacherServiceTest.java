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

import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@SpringBootTest
@ActiveProfiles("test")
class TeacherServiceTest {

	@MockBean
	TeacherRepository repository;

	@Autowired
	TeacherService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		List<Teacher> expectedTeacher = Arrays.asList(teacher);
		when(repository.findAll()).thenReturn(expectedTeacher);

		List<Teacher> actualTeacher = service.findAll();

		assertEquals(expectedTeacher, actualTeacher);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Teacher expected = new Teacher(1L, "FirstName", "LastName");
		Optional<Teacher> optTeacher = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optTeacher);

		Teacher actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		when(repository.save(teacher)).thenReturn(teacher);

		service.save(teacher);

		verify(repository, times(1)).save(teacher);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Teacher teacher = new Teacher(1L, "FirstName", "LastName");
		when(repository.findById(id)).thenReturn(Optional.of(teacher));
		doNothing().when(repository).delete(teacher);

		service.delete(id);

		verify(repository, times(1)).delete(teacher);
	}

}

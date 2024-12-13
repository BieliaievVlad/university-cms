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

import ua.foxminded.tasks.university_cms.entity.User;
import ua.foxminded.tasks.university_cms.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

	@MockBean
	UserRepository repository;

	@Autowired
	UserService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		User user = new User("username", "password");
		List<User> expectedList = Arrays.asList(user);
		when(repository.findAll()).thenReturn(expectedList);

		List<User> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		User expected = new User("username", "password");
		Optional<User> optUser = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optUser);

		User actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);
	}

	@Test
	void save_ValidValue_CalledOnce() {

		User user = new User("username", "password");
		when(repository.save(user)).thenReturn(user);

		service.save(user);

		verify(repository, times(1)).save(user);
	}

	@Test
	void delete_ValidValue_CalledOnce() {

		Long id = 1L;
		User user = new User("username", "password");
		when(repository.findById(id)).thenReturn(Optional.of(user));
		doNothing().when(repository).delete(user);

		service.delete(id);

		verify(repository, times(1)).delete(user);
	}

}

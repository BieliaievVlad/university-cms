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

import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;

@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

	@MockBean
	GroupRepository repository;

	@Autowired
	GroupService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		Group group = new Group(1L, "Group_1", 10L);
		List<Group> expectedList = Arrays.asList(group);
		when(repository.findAll()).thenReturn(expectedList);

		List<Group> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Group expected = new Group(1L, "Course_1", 10L);
		Optional<Group> optGroup = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optGroup);

		Group actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		Group group = new Group(1L, "Course_1", 10L);
		when(repository.save(group)).thenReturn(group);

		service.save(group);

		verify(repository, times(1)).save(group);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Group group = new Group(1L, "Course_1", 10L);
		when(repository.findById(id)).thenReturn(Optional.of(group));
		doNothing().when(repository).delete(group);

		service.delete(id);

		verify(repository, times(1)).delete(group);
	}
	
	@Test
	void findByCourse_ValidValue_ReturnsExpected() {
		fail("Not yet implemented");
	}

}

package ua.foxminded.tasks.university_cms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Role;
import ua.foxminded.tasks.university_cms.repository.RoleRepository;

@SpringBootTest
@ActiveProfiles("test")
class RoleServiceTest {
	
	@MockBean
	RoleRepository repository;
	
	@Autowired
	RoleService service;

	@Test
	void findByName_ValidValue_ReturnsExpectedAndCalledOnce() {
		
		Role role = new Role("ROLE_NAME");
		when(repository.findByName(role.getName())).thenReturn(role);
		
		Role actual = service.findByName(role.getName());
		
		assertEquals(role, actual);
		verify(repository, times(1)).findByName(role.getName());
		
	}

}

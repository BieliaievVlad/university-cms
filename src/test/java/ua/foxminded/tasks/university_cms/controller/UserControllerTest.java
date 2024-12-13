package ua.foxminded.tasks.university_cms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.RoleService;
import ua.foxminded.tasks.university_cms.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DataGeneratorService dataService;
	
	@MockBean
	UserService userService;
	
	@MockBean
	RoleService roleService;
	
    @TestConfiguration
    static class PasswordEncoderTestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showUsers_usersPageRequest_returnsUsersView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.view().name("users"));
	}

}

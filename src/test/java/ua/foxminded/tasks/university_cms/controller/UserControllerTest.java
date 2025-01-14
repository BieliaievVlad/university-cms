package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.foxminded.tasks.university_cms.entity.Role;
import ua.foxminded.tasks.university_cms.entity.User;
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
	
    @MockBean
    BCryptPasswordEncoder passwordEncoder;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showUsers_usersPageRequest_returnsUsersView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.view().name("users"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddUserForm_addUserPageRequest_returnsAddUserView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/add-user"))
		   	   .andExpect(MockMvcResultMatchers.status().isOk())
		   	   .andExpect(MockMvcResultMatchers.view().name("add-user"));
		
	}
	
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addUser_validInput_callMethodsAndRedirectsToUsersPage() throws Exception {
        String username = "newUser";
        String password = "password123";
        String role = "USER";

        Role mockRole = new Role();
        mockRole.setName(role);
        
        when(roleService.findByName(role)).thenReturn(mockRole);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        doNothing().when(userService).save(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/add-user")
											  .with(SecurityMockMvcRequestPostProcessors.csrf())
											  .param("username", username)
											  .param("password", password)
											  .param("role", role))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/users"));

        verify(roleService, times(1)).findByName(role);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userService, times(1)).save(any(User.class));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteUser_validInput_calledMethodAndRedirectsToUserPage() throws Exception {
    	Long id = 1L;
    	doNothing().when(userService).delete(id);
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/delete-user/{id}", id))
        	   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        	   .andExpect(MockMvcResultMatchers.header().string("Location", "/users"));
    	verify(userService, times(1)).delete(id);
    }

}

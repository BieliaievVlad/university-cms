package ua.foxminded.tasks.university_cms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;
	
	@MockBean
	TeacherService service;
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showTeachers_teachersPageRequest_returnsTeachersView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
	  	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	  	   	   .andExpect(MockMvcResultMatchers.view().name("teachers"));
	}

}

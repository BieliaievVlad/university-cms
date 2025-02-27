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

@WebMvcTest(ToolsController.class)
class ToolsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showToolsForm_ToolsPageRequest_ReturnToolsView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/tools"))
	   			.andExpect(MockMvcResultMatchers.status().isOk())
	   			.andExpect(MockMvcResultMatchers.view().name("tools"));
	}

}

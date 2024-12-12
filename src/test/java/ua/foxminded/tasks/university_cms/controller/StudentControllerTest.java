package ua.foxminded.tasks.university_cms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;
	
	@MockBean
	StudentService service;
	
	@Test
	void showStudents_studentsPageRequest_returnsStudentsView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/students"))
		  	   .andExpect(MockMvcResultMatchers.status().isOk())
		  	   .andExpect(MockMvcResultMatchers.view().name("students"));
	}

}

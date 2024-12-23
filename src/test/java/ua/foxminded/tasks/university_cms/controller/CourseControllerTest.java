package ua.foxminded.tasks.university_cms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.service.CourseService;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DataGeneratorService dataService;
	
	@MockBean
	CourseService courseService;
	
	@MockBean
	TeacherService teacherService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showCourses_coursesPageRequest_returnsCoursesView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.view().name("courses"));
	}

}

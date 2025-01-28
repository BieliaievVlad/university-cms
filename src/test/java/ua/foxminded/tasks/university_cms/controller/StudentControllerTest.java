package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;
	
	@MockBean
	StudentService studentService;
	
	@MockBean
	FormService formService;
	
	@MockBean
	GroupService groupService;
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showStudentsForm_StudentsPageRequest_ReturnsStudentsView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/students"))
		  	   .andExpect(MockMvcResultMatchers.status().isOk())
		  	   .andExpect(MockMvcResultMatchers.view().name("students"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showStudentForm_StudentPageRequest_ReturnsStudentView() throws Exception {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Student student = new Student("First_Name", "Last_Name", group);
		
		when(studentService.findById(id)).thenReturn(student);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
		   	   .andExpect(MockMvcResultMatchers.status().isOk())
		   	   .andExpect(MockMvcResultMatchers.view().name("student"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddStudentForm_AddStudentPageRequest_ReturnsAddStudentView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/add-student"))
	  	       .andExpect(MockMvcResultMatchers.status().isOk())
	  	       .andExpect(MockMvcResultMatchers.view().name("add-student"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addStudent_ValidInput_CalledMethodsAndRedirectToStudentsPage() throws Exception {
		
		String firstName = "";
		String lastName = "";
		
		doNothing().when(studentService).addStudent(firstName, lastName);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/add-student")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", firstName)
				.param("lastName", lastName))
	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/students"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteStudent_ValidInput_CalledMethodsAndRedirectToStudentsPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(studentService).delete(id);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-student/{id}", id))
			   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	   	   	   .andExpect(MockMvcResultMatchers.header().string("Location", "/students"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditStudentGroupForm_EditStudentGroupPageRequest_ReturnsEditStudentGroupView() throws Exception {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Student student = new Student("First_Name", "Last_Name");
		
		when(groupService.findAll()).thenReturn(List.of(group));
		when(studentService.findById(id)).thenReturn(student);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-student-group/{id}", id))
	           .andExpect(MockMvcResultMatchers.status().isOk())
	           .andExpect(MockMvcResultMatchers.view().name("edit-student-group"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateStudentGroup_ValidInput_CalledMethodsAndRedirectToStudentsPage() throws Exception {

		Long studentId = 1L;
		Long groupId = 1L;
		
		doNothing().when(studentService).updateStudentGroup(studentId, groupId);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/edit-student-group")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("group", String.valueOf(groupId)))
	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/students"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditStudentNameForm_EditStudentNamePageRequest_ReturnsEditStudentNameView() throws Exception {
		
		Long id = 1L;
		Student student = new Student("First_Name", "Last_Name");
		
		when(studentService.findById(id)).thenReturn(student);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-student-name/{id}", id))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("edit-student-name"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateStudentName_ValidInput_CalledMethodsAndRedirectToStudentsPage() throws Exception {
		
		Long id = 1L;
		String firstName = "";
		String lastName = "";
		
		doNothing().when(studentService).updateStudentName(id, firstName, lastName);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/edit-student-name/{id}", id)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", firstName)
				.param("lastName", lastName))			
	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/students"));
	}

}

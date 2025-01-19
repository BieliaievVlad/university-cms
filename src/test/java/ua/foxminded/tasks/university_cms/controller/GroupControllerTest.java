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

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.form.EditCoursesFormData;
import ua.foxminded.tasks.university_cms.form.GroupsFormData;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.GroupCourseService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@WebMvcTest(GroupController.class)
class GroupControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;

	@MockBean
	FormService formService;
	
	@MockBean
	GroupService groupService;
	
	@MockBean
	StudentService studentService;
	
	@MockBean
	GroupCourseService groupCourseService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showGroupsForm_GroupsPageRequest_ReturnsGroupsView() throws Exception {
		
		when(formService.prepareGroupsFormData()).thenReturn(new GroupsFormData());
		mockMvc.perform(MockMvcRequestBuilders.get("/groups"))
		   	   .andExpect(MockMvcResultMatchers.status().isOk())
		   	   .andExpect(MockMvcResultMatchers.view().name("groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showGroupForm_GroupPageRequest_ReturnsGroupView() throws Exception {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		Student student = new Student("First_Name", "Last_Name");
		
		when(groupService.findById(anyLong())).thenReturn(group);
		when(studentService.findByGroupId(anyLong())).thenReturn(List.of(student));
		when(groupCourseService.findByGroup(any(Group.class))).thenReturn(List.of(course));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/group/{id}", id))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.view().name("group"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddGroupForm_AddGroupPageRequest_ReturnsAddGroupView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/add-group"))
		   	   .andExpect(MockMvcResultMatchers.status().isOk())
		   	   .andExpect(MockMvcResultMatchers.view().name("add-group"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditCoursesForm_EditCoursesPageRequest_ReturnsEditCoursesView() throws Exception {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_name", 10L);
		Course course = new Course(1L, "Course_Name");
		
		when(formService.prepareEditCoursesFormData(id)).thenReturn(new EditCoursesFormData(group, List.of(course)));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-courses/{id}", id))
	   	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   	   .andExpect(MockMvcResultMatchers.view().name("edit-courses"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateCourses_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Course course = new Course(1L, "Course_Name");
		
		doNothing().when(groupCourseService).updateGroupCourse(groupId, courseId);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/update-courses")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("course", String.valueOf(course.getId())))
	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteCourseFromGroup_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(groupCourseService).deleteGroupCourse(anyLong(), anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-course-from-group/{courseId}", id)
				.param("groupId", String.valueOf(id)))
			   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			   .andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addGroup_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		String groupName = "Group_Name";
		
		doNothing().when(groupService).saveGroup(groupName);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/add-group")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("groupName", groupName))
			   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			   .andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteGroup_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(groupCourseService).deleteGroupAndAssociations(id);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-group/{id}", id))
			   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			   .andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddStudentForm_AddStudentPageRequest_ReturnsAddStudentView() throws Exception {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Student student = new Student("First_Name", "Last_Name");
		student.setId(id);
		
		when(studentService.findAll()).thenReturn(List.of(student));
		when(groupService.findById(id)).thenReturn(group);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/add-student-to-group/{id}", id))
	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   .andExpect(MockMvcResultMatchers.view().name("/add-student-to-group"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addStudentToGroup_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(groupCourseService).addStudentToGroup(anyLong(), anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/add-student-to-group/{groupId}", id)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("studentId", String.valueOf(id)))
		   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		   .andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void removeStudentFromGroup_ValidInput_CalledMethodsAndRedirectToGroupsPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(groupCourseService).removeStudentFromGroup(id);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-student-from-group/{id}", id))
		  		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		  		.andExpect(MockMvcResultMatchers.header().string("Location", "/groups"));
	}

}

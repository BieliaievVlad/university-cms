package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.GroupCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DataGeneratorService dataService;

	@MockBean
	FormService formService;

	@MockBean
	TeacherService teacherService;
	
	@MockBean
	TeacherCourseService teacherCourseService;
	
	@MockBean
	GroupCourseService groupCourseService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showCoursesForm_CoursesPageRequest_ReturnsCoursesView() throws Exception {
		
		when(formService.prepareCoursesFormData()).thenReturn(new CoursesFormData());
		mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.view().name("courses"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddCourseForm_AddCoursePageRequest_ReturnsAddCourseView() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get("/add-course"))
	   	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   	   .andExpect(MockMvcResultMatchers.view().name("add-course"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addCourse_ValidInput_CallMethodsAndRedirectsToCoursesPage() throws Exception {
		
		String courseName = "Course_name";
		Long teacherId = 10L;

		doNothing().when(teacherCourseService).saveCourse(courseName, teacherId);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/add-course")
        									  .with(SecurityMockMvcRequestPostProcessors.csrf())
				  							  .param("courseName", courseName)
				  							  .param("teacherId", teacherId.toString()))
        	.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        	.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));		
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showCourseForm_coursePageRequest_returnsCourseView() throws Exception {

		Long id = 10L;
		Course course = new Course(10L, "Course_Name");
		Teacher teacher = new Teacher();
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		List<Group> groups = Collections.emptyList();
		List<Teacher> teachers = Collections.emptyList();
		Map<Course, List<Group>> courseGroupsMap = Collections.emptyMap();
		
		when(formService.prepareCourseFormData(id)).thenReturn(new CourseFormData(teacherCourse,
																					groups,
																					teachers,
																					courseGroupsMap));

		mockMvc.perform(MockMvcRequestBuilders.get("/course/{id}", id))
	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   .andExpect(MockMvcResultMatchers.view().name("course"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditTeacherForm_EditTeacherPageRequest_ShowEditTeacherView() throws Exception {
		
		Long id = 10L;
		Teacher teacher = new Teacher();
		Course course = new Course();
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
		when(teacherService.findAll()).thenReturn(List.of(teacher));
		when(teacherCourseService.findByCourseId(id)).thenReturn(teacherCourse);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-teacher/{id}", id))
	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   .andExpect(MockMvcResultMatchers.view().name("edit-teacher"));
	
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateTeacher_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {

		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		Course course = new Course(1L, "Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);

		doNothing().when(teacherCourseService).updateTeacherCourse(teacherCourse);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/update-teacher")
    				.with(SecurityMockMvcRequestPostProcessors.csrf())
    				.flashAttr("teacherCourse", teacherCourse))
 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
    	
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditGroupsForm_EditGroupsPageRequest_ShowEditGroupsView() throws Exception {
		
		Long id = 10L;
		Course course = new Course(id, "Course_Name");
		Map<Long, List<Group>> courseGroupsMap = Collections.emptyMap();
		List<Group> filteredGroups = Collections.emptyList();
		
		when(formService.prepareEditGroupsFormData(id)).thenReturn(new EditGroupsFormData(course,
																							courseGroupsMap,
																							filteredGroups));

		mockMvc.perform(MockMvcRequestBuilders.get("/edit-groups/{id}", id))
	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   .andExpect(MockMvcResultMatchers.view().name("edit-groups"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateGroups_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {
		
		Long groupId = 1L;
		Long courseId = 1L;

		doNothing().when(groupCourseService).updateGroupCourse(groupId, courseId);
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/update-groups")
    			.with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("group", String.valueOf(groupId))
                .param("courseId", String.valueOf(courseId)))
 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
    	

	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteGroupFromCourse_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {
		
		Long groupId = 1L;
		Long courseId = 2L;

		doNothing().when(groupCourseService).deleteGroupCourse(groupId, courseId);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-group-from-course/{groupId}", groupId)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("courseId", String.valueOf(courseId)))
	 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteCourse_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {
		
		Long id = 10L;

    	doNothing().when(teacherCourseService).deleteTeacherCourse(id);
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/delete-course/{id}", id))
        	   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        	   .andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));

	}
	
}

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
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.TeachersFormData;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

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

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showTeachersForm_TeachersPageRequest_ReturnsTeachersView() throws Exception {

		when(formService.prepareTeachersFormData()).thenReturn(new TeachersFormData());

		mockMvc.perform(MockMvcRequestBuilders.get("/teachers")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teachers"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showTeacherForm_TeacherPageRequest_ReturnsTeacherView() throws Exception {

		Long id = 1L;
		Teacher teacher = new Teacher("First_Name", "Last_Name");
		Course course = new Course("Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);

		when(teacherService.findById(id)).thenReturn(teacher);
		when(teacherCourseService.findByTeacherId(id)).thenReturn(List.of(teacherCourse));

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teacher"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditTeacherNameForm_EditTeacherNamePageRequest_ReturnsEditTeacherNameView() throws Exception {

		Long id = 1L;
		Teacher teacher = new Teacher("First_Name", "Last_Name");

		when(teacherService.findById(id)).thenReturn(teacher);

		mockMvc.perform(MockMvcRequestBuilders.get("/edit-teacher-name/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("edit-teacher-name"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditTeacherCourseForm_EditTeacherCoursePageRequest_ReturnsEditTeacherCourseView() throws Exception {

		Long id = 1L;
		Teacher teacher = new Teacher("First_Name", "Last_Name");
		Course course = new Course("Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);

		when(teacherService.findById(id)).thenReturn(teacher);
		when(teacherCourseService.prepareFilteredCoursesList(id)).thenReturn(List.of(teacherCourse));

		mockMvc.perform(MockMvcRequestBuilders.get("/edit-teacher-course/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("edit-teacher-course"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addCourseToTeacher_ValidInput_CalledMethodsAndRedirectToTeachersPage() throws Exception {

		Long teacherId = 1L;
		Long courseId = 1L;

		doNothing().when(teacherCourseService).addCourseToTeacher(teacherId, courseId);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/edit-teacher-courses").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("teacherId", String.valueOf(teacherId)).param("courseId", String.valueOf(courseId)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/teachers"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteCourseFromTeacher_ValidInput_CalledMethodsAndRedirectToTeachersPage() throws Exception {

		Long id = 1L;

		doNothing().when(teacherCourseService).deleteCourseFromTeacher(id);

		mockMvc.perform(MockMvcRequestBuilders.get("/delete-course-from-teacher/{courseId}", id))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/teachers"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateTeacherName_ValidInput_CalledMethodsAndRedirectToTeachersPage() throws Exception {

		Long id = 1L;
		String firstName = "";
		String lastName = "";

		doNothing().when(teacherService).updateTeacherName(id, firstName, lastName);

		mockMvc.perform(MockMvcRequestBuilders.post("/edit-teacher-name/{id}", id)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", firstName)
				.param("lastName", lastName))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/teachers"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddTeacherForm_AddTeacherPageRequest_ReturnsAddTeacherView() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/add-teacher"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("add-teacher"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addTeacher_ValidInput_CalledMethodsAndRedirectToTeachersPage() throws Exception {
		
		String firstName = "";
		String lastName = "";
		
		doNothing().when(teacherService).save(any(Teacher.class));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/add-teacher")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", firstName)
				.param("lastName", lastName))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/teachers"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteTeacher_ValidInput_CalledMethodsAndRedirectToTeachersPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(teacherCourseService).deleteTeacherAndAssociations(id);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-teacher/{id}", id))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/teachers"));
	}
}

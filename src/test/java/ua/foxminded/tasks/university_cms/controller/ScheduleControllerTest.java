package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.ScheduleService;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DataGeneratorService dataService;

	@MockBean
	FormService formService;
	
	@MockBean
	ScheduleService scheduleService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showSchedulesForm_SchedulesPageRequest_ReturnsShedulesView() throws Exception {

		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name");
		Group group = new Group(1L, "Group_Name");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(scheduleService.findAll()).thenReturn(List.of(schedule));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/schedules"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("schedules"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showScheduleForm_SchedulePageRequest_ReturnsScheduleView() throws Exception {

		Long id = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name");
		Group group = new Group(1L, "Group_Name");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(scheduleService.findById(id)).thenReturn(schedule);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("schedule"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showAddScheduleForm_AddSchedulePageRequest_ReturnsAddScheduleView() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/add-schedule"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("add-schedule"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void addSchedule_ValidInput_CallMethodsAndRedirectsToSchedulesPage() throws Exception {
		
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		
		doNothing().when(scheduleService).save(any(Schedule.class));
		
        mockMvc.perform(MockMvcRequestBuilders.post("/add-schedule")
				  							  .with(SecurityMockMvcRequestPostProcessors.csrf())
				  							  .param("dateTime", dateTime.toString()))
        		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        		.andExpect(MockMvcResultMatchers.header().string("Location", "/schedules"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteSchedule_ValidInput_CallMethodsAndRedirectsToSchedulesPage() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(scheduleService).delete(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-schedule/{id}", id))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/schedules"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditScheduleCourseForm_EditScheduleCoursePageRequest_ReturnsEditScheduleCourseView() throws Exception {
		
		Long id = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name");
		Group group = new Group(1L, "Group_Name");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(scheduleService.findById(anyLong())).thenReturn(schedule);
		when(formService.prepareEditScheduleCourseForm(id)).thenReturn(List.of(course));	
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-schedule-course/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("edit-schedule-course"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditScheduleGroupForm_EditScheduleGroupPageRequest_ReturnsEditScheduleGroupView() throws Exception {
		
		Long id = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name");
		Group group = new Group(1L, "Group_Name");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(scheduleService.findById(anyLong())).thenReturn(schedule);
		when(formService.prepareEditScheduleGroupForm(id)).thenReturn(List.of(group));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-schedule-group/{id}", id))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("edit-schedule-group"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateScheduleCourse_ValidInput_CallMethodsAndRedirectsToSchedulesPage() throws Exception {
		
		Long courseId = 1L;
		Long scheduleId = 1L;
		
		doNothing().when(scheduleService).updateScheduleCourse(anyLong(), anyLong());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/update-schedule-course")
				  							  .with(SecurityMockMvcRequestPostProcessors.csrf())
				  							  .param("courseId", String.valueOf(courseId))
				  							  .param("scheduleId", String.valueOf(scheduleId)))
        		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        		.andExpect(MockMvcResultMatchers.header().string("Location", "/schedules"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateScheduleGroup_ValidInput_CallMethodsAndRedirectsToSchedulesPage() throws Exception {
		
		Long groupId = 1L;
		Long scheduleId = 1L;
		
		doNothing().when(scheduleService).updateScheduleGroup(anyLong(), anyLong());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/update-schedule-group")
				  							  .with(SecurityMockMvcRequestPostProcessors.csrf())
				  							  .param("groupId", String.valueOf(groupId))
				  							  .param("scheduleId", String.valueOf(scheduleId)))
        		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        		.andExpect(MockMvcResultMatchers.header().string("Location", "/schedules"));
	}

}

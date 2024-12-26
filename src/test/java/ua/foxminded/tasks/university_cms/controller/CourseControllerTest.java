package ua.foxminded.tasks.university_cms.controller;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
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
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.service.CourseService;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.GroupCourseService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
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
	GroupService groupService;
	
	@MockBean
	TeacherService teacherService;
	
	@MockBean
	TeacherCourseService teacherCourseService;
	
	@MockBean
	GroupCourseService groupCourseService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showCourses_coursesPageRequest_returnsCoursesView() throws Exception {
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
		Long id = 1L;
		Course newCourse = new Course();
		Teacher teacher = new Teacher("First_name", "Last_Name");
		Course course = new Course(id, "Course_name");

		doNothing().when(courseService).save(any(Course.class));
		when(teacherService.findById(teacherId)).thenReturn(teacher);
		when(courseService.findById(newCourse.getId())).thenReturn(course);
		doNothing().when(teacherCourseService).save(any(TeacherCourse.class));
		
        mockMvc.perform(MockMvcRequestBuilders.post("/add-course")
        									  .with(SecurityMockMvcRequestPostProcessors.csrf())
				  							  .param("courseName", courseName)
				  							  .param("teacherId", teacherId.toString()))
        	.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        	.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
        
        verify(courseService, times(1)).save(any(Course.class));
        verify(teacherService, times(1)).findById(teacherId);
        verify(courseService, times(1)).findById(newCourse.getId());
        verify(teacherCourseService, times(1)).save(any(TeacherCourse.class));
			
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showCourseForm_coursePageRequest_returnsCourseView() throws Exception {

		Long id = 10L;
		Course course = new Course();
		Teacher teacher = new Teacher();
		Group group = new Group();
		when(courseService.findById(id)).thenReturn(course);
		when(teacherService.findByCourse(course)).thenReturn(teacher);
		when(groupService.findByCourse(course)).thenReturn(List.of(group));
		
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

		Long id = 1L;
		Teacher teacher = new Teacher(1L, "First_Name", "Last_Name");
		Course course = new Course(1L, "Course_Name");
		TeacherCourse teacherCourse = new TeacherCourse();
	    teacherCourse.setTeacher(teacher);
	    teacherCourse.setCourse(course);

		when(courseService.findById(id)).thenReturn(course);
		when(teacherCourseService.findByCourseId(id)).thenReturn(teacherCourse);
		doNothing().when(teacherCourseService).delete(any(TeacherCourse.class));
		doNothing().when(teacherCourseService).save(any(TeacherCourse.class));
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/update-teacher")
    			.with(SecurityMockMvcRequestPostProcessors.csrf())
    			.flashAttr("teacherCourse", teacherCourse))
 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
    	
        verify(teacherCourseService, times(1)).delete(any(TeacherCourse.class)); 
        verify(teacherCourseService, times(1)).save(any(TeacherCourse.class));

	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showEditGroupsForm_EditGroupsPageRequest_ShowEditGroupsView() throws Exception {
		
		Long id = 10L;
		Group group = new Group();
		Course course = new Course(id, "Course_Name");
		Map<Long, List<Group>> courseGroupsMap = new HashMap<>();
		courseGroupsMap.put(id, List.of(group));
		
		when(groupService.findAll()).thenReturn(List.of(group));
		when(courseService.findById(id)).thenReturn(course);
		when(groupService.findByCourse(course)).thenReturn(List.of(group));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/edit-groups/{id}", id))
	   	   .andExpect(MockMvcResultMatchers.status().isOk())
	   	   .andExpect(MockMvcResultMatchers.view().name("edit-groups"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateGroups_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(groupId, "Group_Name", 10L);
		Course course = new Course(courseId, "Course_Name");
		
		when(groupService.findById(groupId)).thenReturn(group);
		when(courseService.findById(courseId)).thenReturn(course);
		doNothing().when(groupCourseService).save(any(GroupCourse.class));
		
    	mockMvc.perform(MockMvcRequestBuilders.post("/update-groups")
    			.with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("group", String.valueOf(groupId))
                .param("courseId", String.valueOf(courseId)))
 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
    	
        verify(groupCourseService, times(1)).save(any(GroupCourse.class));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteGroup() throws Exception {
		
		Long groupId = 1L;
		Long courseId = 2L;
		Group group = new Group(groupId, "Group_Name", 10L);
		Course course = new Course(courseId, "Course_Name");
		
		when(groupService.findById(groupId)).thenReturn(group);
		when(courseService.findById(courseId)).thenReturn(course);
		doNothing().when(groupCourseService).delete(any(GroupCourse.class));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/delete-group/{groupId}", groupId)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("courseId", String.valueOf(courseId)))
	 	   			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	 	   			.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void deleteCourse_ValidInput_CalledMethodsAndRedirectsToCoursesPage() throws Exception {
		
		Long id = 10L;
    	Teacher teacher = new Teacher();
    	Course course = new Course();
    	Group group = new Group();
    	TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
    	GroupCourse groupCourse = new GroupCourse(group, course);
    	
    	when(teacherCourseService.findByCourseId(id)).thenReturn(teacherCourse);
    	when(groupCourseService.findByCourseId(id)).thenReturn(List.of(groupCourse));
    	doNothing().when(groupCourseService).delete(groupCourse);
    	doNothing().when(teacherCourseService).delete(teacherCourse);
    	doNothing().when(courseService).delete(id);
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/delete-course/{id}", id))
        	   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        	   .andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));
    	verify(groupCourseService, times(1)).delete(groupCourse);
    	verify(teacherCourseService, times(1)).delete(teacherCourse);
    	verify(courseService, times(1)).delete(id);
	}
	
}

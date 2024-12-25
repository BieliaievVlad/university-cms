package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	void editCourse_ValidValue_CalledMethodsAndAndSendAttributes() throws Exception {
		
    	Long id = 1L;
    	Teacher teacher = new Teacher("First_name", "Last_Name");
    	List<Teacher> teachers = List.of(teacher);
    	Group group = new Group(1L,"Group_Name", 10L);
    	List<Group> groups = List.of(group);
    	Course course = new Course(1L, "Course_name");
    	GroupCourse groupCourse = new GroupCourse();
    	TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
    	
    	when(teacherService.findAll()).thenReturn(teachers);
    	when(groupService.findAll()).thenReturn(groups);
    	when(courseService.findById(id)).thenReturn(course);
    	when(teacherService.findByCourse(course)).thenReturn(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/edit-course/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(MockMvcResultMatchers.status().isOk()) 
            .andExpect(MockMvcResultMatchers.view().name("edit-course")) 
            .andExpect(MockMvcResultMatchers.model().attribute("teachers", teachers))
            .andExpect(MockMvcResultMatchers.model().attribute("groups", groups))
            .andExpect(MockMvcResultMatchers.model().attribute("groupCourse", groupCourse))
            .andExpect(MockMvcResultMatchers.model().attribute("teacherCourse", teacherCourse));

        verify(teacherService, times(1)).findAll();
        verify(groupService, times(1)).findAll();
        verify(courseService, times(1)).findById(id);
        verify(teacherService, times(1)).findByCourse(course);
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void updateCourse_ValidValue_CalledMethodsAndRedirect() throws Exception {

		Teacher teacher = new Teacher("First_name", "Last_Name");
		Group group = new Group(1L,"Group_Name", 10L);
		Course course = new Course(1L, "Course_name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
		doNothing().when(courseService).save(any(Course.class));
        doNothing().when(groupCourseService).save(any(GroupCourse.class));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/update-course")
        		.with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("teacherCourse", teacherCourse)
                .flashAttr("groupCourse", groupCourse))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        		.andExpect(MockMvcResultMatchers.header().string("Location", "/courses"));

        verify(courseService, times(1)).save(any(Course.class)); 
        verify(groupCourseService, times(1)).save(any(GroupCourse.class));
    	
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

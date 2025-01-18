package ua.foxminded.tasks.university_cms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.form.EditCoursesFormData;
import ua.foxminded.tasks.university_cms.form.GroupsFormData;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.GroupCourseService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@Controller
public class GroupController {
	
	private final GroupService groupService;
	private final StudentService studentService;
	private final GroupCourseService groupCourseService;
	private final FormService formService;

	@Autowired
	public GroupController(GroupService groupService, StudentService studentService, 
						   GroupCourseService groupCourseService, FormService formService) {
		this.groupService = groupService;
		this.studentService = studentService;
		this.groupCourseService = groupCourseService;
		this.formService = formService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/groups")
	public String showGroupsForm(Model model) {
		
		GroupsFormData data = formService.prepareGroupsFormData();
		
		model.addAttribute("groups", data.getGroups());
		model.addAttribute("groupCoursesMap", data.getGroupCoursesMap());
		
		return "groups";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/group/{id}")
	public String showGroupForm(@PathVariable Long id, Model model) {
		
		Group group = groupService.findById(id);
		List<Student> students = studentService.findByGroupId(id);
		List<Course> courses = groupCourseService.findByGroup(group);
		
		model.addAttribute("group", group);
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		
		return "group";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/add-group")
	public String showAddGroupForm() {
		return "add-group";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-courses/{id}")
	public String showEditCoursesForm(@PathVariable Long id, Model model) {
		
		EditCoursesFormData data = formService.prepareEditCoursesFormData(id);
		
		model.addAttribute("group", data.getGroup());
		model.addAttribute("filteredCourses", data.getFilteredCourses());
		
		return "edit-courses";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/update-courses")
	public String updateCourses(Long groupId, @RequestParam("course") Long courseId) {
		
		groupCourseService.updateGroupCourse(groupId, courseId);
		
		return "redirect:/groups";
	}
	
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/delete-course-from-group/{courseId}")
    public String deleteCourseFromGroup(@PathVariable Long courseId, @RequestParam Long groupId) {
    	
    	groupCourseService.deleteGroupCourse(groupId, courseId);
    	
    	return "redirect:/groups";
    }
	
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add-group")
	public String addGroup(@RequestParam String groupName) {
		
		groupService.saveGroup(groupName);
		
		return "redirect:/groups";
	}
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete-group/{id}")
    public String deleteGroup(@PathVariable Long id) {
    	
    	groupCourseService.deleteGroupAndAssociations(id);
    	
    	return "redirect:/groups";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/add-student-to-group/{id}")
    public String showAddStudentForm(@PathVariable Long id, Model model) {
    	
    	List<Student> students = studentService.findAll();
    	Group group = groupService.findById(id);
    	
    	model.addAttribute("students", students);
    	model.addAttribute("group", group);
    	
    	return "/add-student-to-group";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/add-student-to-group/{groupId}")
    public String addStudentToGroup(@RequestParam Long studentId, @PathVariable Long groupId) {
    	
    	groupCourseService.addStudentToGroup(studentId, groupId);
    	
    	return "redirect:/groups";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/delete-student-from-group/{id}")
    public String removeStudentFromGroup(@PathVariable Long id) {
    	
    	groupCourseService.removeStudentFromGroup(id);
    	
    	return "redirect:/groups";
    }

}

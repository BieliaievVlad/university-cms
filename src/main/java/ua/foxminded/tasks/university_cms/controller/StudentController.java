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

import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@Controller
public class StudentController {
	
	private final StudentService studentService;
	private final GroupService groupService;
	private final FormService formService;
	
	@Autowired
	public StudentController(StudentService studentService, GroupService groupService, FormService formService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.formService = formService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/students")
	public String showStudentsForm(@RequestParam(value = "group", required = false) Long groupId, 
								   Model model) {
		
		List<Student> students = studentService.filterStudents(groupId);
		List<Group> groups = groupService.findAll();
	    
		model.addAttribute("students", students);
		model.addAttribute("groups", groups);
		return "students";	
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/student/{id}")
	public String showStudentForm(@PathVariable Long id, Model model) {
		
		Student student = studentService.findById(id);
		List<Schedule> schedules = formService.prepareScheduleListForStudent(student);
		
		model.addAttribute("student", student);
		model.addAttribute("schedules", schedules);
		
		return "student";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/add-student")
	public String showAddStudentForm() {
		return "add-student";
	}
	
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add-student")
	public String addStudent(@RequestParam String firstName, @RequestParam String lastName) {
		
		studentService.addStudent(firstName, lastName);
		
		return "redirect:/students";
	}
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete-student/{id}")
    public String deleteStudent(@PathVariable Long id) {
    	
    	studentService.delete(id);
    	
    	return "redirect:/students";
    }
    
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-student-group/{id}")
	public String showEditStudentGroupForm(@PathVariable Long id,  Model model) {
		
		List<Group> groups = groupService.findAll();
		Student student = studentService.findById(id);
		
		model.addAttribute("groups", groups);
		model.addAttribute("student", student);
		
		return "edit-student-group";
	}
    
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/edit-student-group")
	public String updateStudentGroup(Long studentId, @RequestParam("group") Long groupId) {

		studentService.updateStudentGroup(studentId, groupId);
		
		return "redirect:/students";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/edit-student-name/{id}")
	public String showEditStudentNameForm(@PathVariable Long id, Model model) {
		
		Student student = studentService.findById(id);
		
		model.addAttribute("student", student);
		
		return "edit-student-name";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/edit-student-name/{id}")
	public String updateStudentName(@PathVariable Long id, 
									@RequestParam("firstName") String firstName, 
									@RequestParam("lastName") String lastName) {
		
		studentService.updateStudentName(id, firstName, lastName);
		
		return "redirect:/students";
	}

}

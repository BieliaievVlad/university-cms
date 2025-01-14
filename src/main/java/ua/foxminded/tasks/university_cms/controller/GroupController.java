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
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.StudentService;

@Controller
public class GroupController {
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	StudentService studentService;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/groups")
	public String showGroupsForm(Model model) {
		
		List<Group> groups = groupService.findAll();
		
		model.addAttribute("groups", groups);
		
		return "groups";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/group/{id}")
	public String showGroupForm(@PathVariable Long id, Model model) {
		
		Group group = groupService.findById(id);
		List<Student> students = studentService.findByGroupId(id);
		
		model.addAttribute("group", group);
		model.addAttribute("students", students);
		
		return "group";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/add-group")
	public String showAddGroupForm() {
		return "add-group";
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
    	
    	groupService.deleteGroup(id);
    	
    	return "redirect:/groups";
    }

}

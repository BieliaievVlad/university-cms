package ua.foxminded.tasks.university_cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.service.StudentService;

@Controller
public class StudentController {
	
	@Autowired
	StudentService service;
	
	@GetMapping("/students")
	public String showStudents(Model model) {
		List<Student> students = service.findAll();
		model.addAttribute("students", students);
		return "students";
		
	}
	
	

}

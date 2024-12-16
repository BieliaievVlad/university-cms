package ua.foxminded.tasks.university_cms.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class TeacherController {
	
	@Autowired
	TeacherService service;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/teachers")
	public String showTeachers(Model model) {
		List<Teacher> teachers = service.findAll();
		model.addAttribute("teachers", teachers);
		return "teachers";
		
	}

}

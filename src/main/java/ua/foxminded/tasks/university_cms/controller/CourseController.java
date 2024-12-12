package ua.foxminded.tasks.university_cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.service.CourseService;

@Controller
public class CourseController {
	
	@Autowired
	CourseService service;
	
	@GetMapping("/courses")
	public String showCourses(Model model) {
		List<Course> courses = service.findAll();
		model.addAttribute("courses", courses);
		return "courses";
	}

}

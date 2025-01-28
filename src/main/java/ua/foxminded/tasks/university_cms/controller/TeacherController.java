package ua.foxminded.tasks.university_cms.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.form.TeachersFormData;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class TeacherController {
	
	private final FormService formService;
	private final TeacherService teacherService;
	
	@Autowired
	public TeacherController(FormService formService, TeacherService teacherService) {
		this.formService = formService;
		this.teacherService = teacherService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/teachers")
	public String showTeachersForm(Model model) {
		
		TeachersFormData data = formService.prepareTeachersFormData();
		
		model.addAttribute("teachers", data.getTeachers());
		model.addAttribute("teacherCoursesMap", data.getTeacherCoursesMap());
		return "teachers";
		
	}

}

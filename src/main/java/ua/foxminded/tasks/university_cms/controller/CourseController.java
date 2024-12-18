package ua.foxminded.tasks.university_cms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.service.CourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TeacherService teacherService;
		
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/courses")
	public String showCourses(Model model) {
		List<Course> courses = courseService.findAll();
		List<TeacherCourse> teacherCourses = new ArrayList<>();
		for(Course course:courses) {
			Teacher teacher = teacherService.findByCourse(course);
			TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
			teacherCourses.add(teacherCourse);
		}
		model.addAttribute("teacherCourses", teacherCourses);
		return "courses";
	}
	
//	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
//	@GetMapping("/courses")
//	public String showCourses(Model model) {
//		List<Course> courses = service.findAll();
//		model.addAttribute("courses", courses);
//		return "courses";
//	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/add-course")
	public String showAddCourseForm() {
		return "add-course";
	}

}

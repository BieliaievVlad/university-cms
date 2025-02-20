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

import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.TeachersFormData;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class TeacherController {
	
	private final FormService formService;
	private final TeacherService teacherService;
	private final TeacherCourseService teacherCourseService;
	
	@Autowired
	public TeacherController(FormService formService, TeacherService teacherService, TeacherCourseService teacherCourseService) {
		this.formService = formService;
		this.teacherService = teacherService;
		this.teacherCourseService = teacherCourseService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/teachers")
	public String showTeachersForm(Model model) {
		
		TeachersFormData data = formService.prepareTeachersFormData();
		
		model.addAttribute("teachers", data.getTeachers());
		model.addAttribute("teacherCoursesMap", data.getTeacherCoursesMap());
		return "teachers";
		
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/teacher/{id}")
	String showTeacherForm(@PathVariable Long id, Model model) {
		
		Teacher teacher = teacherService.findById(id);
		List<TeacherCourse> teacherCourses = teacherCourseService.findByTeacherId(id);
		List<Schedule> schedules = formService.prepareScheduleListForTeacher(teacherCourses);
		
		model.addAttribute("teacher", teacher);
		model.addAttribute("teacherCourses", teacherCourses);
		model.addAttribute("schedules", schedules);
		
		return "teacher";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/edit-teacher-name/{id}")
	public String showEditTeacherNameForm(@PathVariable Long id, Model model) {
		
		Teacher teacher = teacherService.findById(id);
		
		model.addAttribute("teacher", teacher);
		
		return "edit-teacher-name";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-teacher-course/{id}")
	public String showEditTeacherCourseForm(@PathVariable Long id, Model model) {
		
		Teacher teacher = teacherService.findById(id);
		List<TeacherCourse> filteredCourses = teacherCourseService.prepareFilteredCoursesList(id);
		
		model.addAttribute("teacher", teacher);
		model.addAttribute("filteredCourses", filteredCourses);
		
		return "edit-teacher-course";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@PostMapping("/edit-teacher-courses")
	public String addCourseToTeacher(@RequestParam Long teacherId, @RequestParam Long courseId) {
		
		teacherCourseService.addCourseToTeacher(teacherId, courseId);
		
		return "redirect:/teachers";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/delete-course-from-teacher/{courseId}")
	public String deleteCourseFromTeacher(@PathVariable Long courseId) {
		
		teacherCourseService.deleteCourseFromTeacher(courseId);
		
		return "redirect:/teachers";
	}
	
	
	
	@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/edit-teacher-name/{id}")
	public String updateTeacherName(@PathVariable Long id, 
									@RequestParam("firstName") String firstName, 
									@RequestParam("lastName") String lastName) {
		
		teacherService.updateTeacherName(id, firstName, lastName);
		
		return "redirect:/teachers";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/add-teacher")
	public String showAddTeacherForm() {
		
		return "add-teacher";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/add-teacher")
	public String addTeacher(@RequestParam String firstName, @RequestParam String lastName) {
		
		teacherService.save(new Teacher(firstName, lastName));
		
		return "redirect:/teachers";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/delete-teacher/{id}")
	public String deleteTeacher(@PathVariable Long id) {
		
		teacherCourseService.deleteTeacherAndAssociations(id);
		
		return "redirect:/teachers";
	}
}

package ua.foxminded.tasks.university_cms.controller;

import java.time.LocalDateTime;
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
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.service.FormService;
import ua.foxminded.tasks.university_cms.service.ScheduleService;

@Controller
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	private final FormService formService;
	
	@Autowired
	public ScheduleController(ScheduleService scheduleService, FormService formService) {
		this.scheduleService = scheduleService;
		this.formService = formService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/schedules")
	public String showSchedulesForm(Model model) {
		
		List<Schedule> schedules = scheduleService.findAll();
		
		model.addAttribute("schedules", schedules);
		
		return "schedules";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/schedule/{id}")
	public String showScheduleForm(@PathVariable Long id, Model model) {
		
		Schedule schedule = scheduleService.findById(id);
		
		model.addAttribute("schedule", schedule);
		
		return "schedule";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/add-schedule")
	public String showAddScheduleForm(Model model) {
		return "add-schedule";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@PostMapping("/add-schedule")
	public String addSchedule(@RequestParam LocalDateTime dateTime) {

		scheduleService.addSchedule(dateTime);
		
		return "redirect:/schedules";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/delete-schedule/{id}")
	public String deleteSchedule(@PathVariable Long id) {

		scheduleService.delete(id);
		
		return "redirect:/schedules";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-schedule-course/{id}")
	public String showEditScheduleCourseForm(@PathVariable Long id, Model model) {
		
		Schedule schedule = scheduleService.findById(id);
		List<Course> courses = formService.prepareEditScheduleCourseForm(id);
		
		model.addAttribute("schedule", schedule);
		model.addAttribute("courses", courses);
		
		return "edit-schedule-course";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-schedule-group/{id}")
	public String showEditScheduleGroupForm(@PathVariable Long id, Model model) {
		
		Schedule schedule = scheduleService.findById(id);
		List<Group> groups = formService.prepareEditScheduleGroupForm(id);
		
		model.addAttribute("schedule", schedule);
		model.addAttribute("groups", groups);
		
		return "edit-schedule-group";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@PostMapping("/update-schedule-course")
	public String updateScheduleCourse(@RequestParam Long courseId, @RequestParam Long scheduleId) {
		
		scheduleService.updateScheduleCourse(courseId, scheduleId);
		
		return "redirect:/schedules";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@PostMapping("/update-schedule-group")
	public String updateScheduleGroup(@RequestParam Long groupId, @RequestParam Long scheduleId) {
		
		scheduleService.updateScheduleGroup(groupId, scheduleId);
		
		return "redirect:/schedules";
	}

}

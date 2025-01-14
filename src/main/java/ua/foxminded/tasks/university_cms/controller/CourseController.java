package ua.foxminded.tasks.university_cms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.service.CourseService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TeacherService teacherService;
	
	@Autowired
	TeacherCourseService teacherCourseService;
		
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/courses")
	public String showCoursesForm(Model model) {

		CoursesData data = courseService.prepareCoursesData();
		
		model.addAttribute("teacherCourses", data.getTeacherCourses());
		model.addAttribute("courseGroupsMap", data.getCourseGroupsMap());
		return "courses";
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/add-course")
	public String showAddCourseForm() {
		return "add-course";
	}
	
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/add-course")
    public String addCourse(@RequestParam String courseName, @RequestParam Long teacherId) {
    	
    	courseService.saveCourse(courseName, teacherId);
    	
    	return "redirect:/courses";
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/course/{id}")
    public String showCourseForm(@PathVariable Long id, Model model) {

		CourseFormData data = courseService.prepareCourseFormData(id);
        
        model.addAttribute("teachers", data.getTeachers());
        model.addAttribute("groups", data.getGroups());
        model.addAttribute("teacherCourse", data.getTeacherCourse());
	    model.addAttribute("courseGroupsMap", data.getCourseGroupsMap());
        return "course";
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-teacher/{id}")
	public String showEditTeacherForm(@PathVariable Long id, Model model) {
		
		List<Teacher> teachers = teacherService.findAll();
		TeacherCourse teacherCourse = teacherCourseService.findByCourseId(id);
		
		model.addAttribute("teachers", teachers);
		model.addAttribute("teacherCourse", teacherCourse);
			
		return "edit-teacher";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/update-teacher")
    public String updateTeacher(@ModelAttribute TeacherCourse teacherCourse) {
			
		courseService.updateTeacherCourse(teacherCourse);
        
        return "redirect:/courses";
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-groups/{id}")
	public String showEditGroupsForm(@PathVariable Long id, Model model) {
		
		EditGroupsFormData data = courseService.prepareEditGroupsFormData(id);
        
        model.addAttribute("courseGroupsMap", data.getCourseGroupsMap());
        model.addAttribute("filteredGroups", data.getFilteredGroups());
        model.addAttribute("course", data.getCourse());
        
		return "edit-groups";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/update-groups")
    public String updateGroups(@RequestParam("group") Long groupId, Long courseId) {
		
		courseService.updateGroups(groupId, courseId);
        
        return "redirect:/courses";
    }
	
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/delete-group-from-course/{groupId}")
    public String deleteGroupFromCourse(@PathVariable Long groupId, @RequestParam Long courseId) {
    	
    	courseService.deleteGroupFromCourse(groupId, courseId);

        return "redirect:/courses";
    }
	
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete-course/{id}")
    public String deleteCourse(@PathVariable Long id) {
   	
    	courseService.deleteCourse(id);

        return "redirect:/courses";
    }

}

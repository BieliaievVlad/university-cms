package ua.foxminded.tasks.university_cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.service.CourseService;
import ua.foxminded.tasks.university_cms.service.GroupCourseService;
import ua.foxminded.tasks.university_cms.service.GroupService;
import ua.foxminded.tasks.university_cms.service.TeacherCourseService;
import ua.foxminded.tasks.university_cms.service.TeacherService;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TeacherService teacherService;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	TeacherCourseService teacherCourseService;
	
	@Autowired
	GroupCourseService groupCourseService;
		
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER', 'STUDENT')")
	@GetMapping("/courses")
	public String showCourses(Model model) {
		
		List<Course> courses = courseService.findAll();
		List<TeacherCourse> teacherCourses = new ArrayList<>();
		
		for (Course course : courses) {
			Teacher teacher = teacherService.findByCourse(course);
			TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
			teacherCourses.add(teacherCourse);
		}

	    Map<Course, List<Group>> courseGroupsMap = new HashMap<>();
	    for (Course course : courses) {
	        List<Group> groups = groupService.findByCourse(course);
	        courseGroupsMap.put(course, groups);
	    }
		
		model.addAttribute("teacherCourses", teacherCourses);
	    model.addAttribute("courseGroupsMap", courseGroupsMap);
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
    	
    	Course newCourse = new Course();
    	Course course = new Course();
    	Teacher teacher = new Teacher();
    	TeacherCourse newTeacherCourse = new TeacherCourse();
    	
    	newCourse.setName(courseName);    	
    	courseService.save(newCourse);
    	
    	teacher = teacherService.findById(teacherId);
    	course = courseService.findById(newCourse.getId());
    	    	
    	newTeacherCourse.setTeacher(teacher);
    	newTeacherCourse.setCourse(course);
    	
    	teacherCourseService.save(newTeacherCourse);
    	
    	return "redirect:/courses";
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	@GetMapping("/edit-course/{id}")
    public String editCourse(@PathVariable Long id, Model model) {

		List<Teacher> teachers = teacherService.findAll();
		List<Group> groups = groupService.findAll();
		GroupCourse groupCourse = new GroupCourse();
        Course course = courseService.findById(id);
        if (course == null) {
            return "redirect:/courses";
        }
		Teacher teacher = teacherService.findByCourse(course);
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
        
        model.addAttribute("teachers", teachers);
        model.addAttribute("groups", groups);
        model.addAttribute("groupCourse", groupCourse);
        model.addAttribute("teacherCourse", teacherCourse);
        return "edit-course";
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/update-course")
    public String updateCourse(@ModelAttribute TeacherCourse teacherCourse, 
    						   @ModelAttribute GroupCourse groupCourse,
    						   Model model) {
		
		GroupCourse newGroupCourse = new GroupCourse(groupCourse.getGroup(), teacherCourse.getCourse());
        courseService.save(teacherCourse.getCourse());
        
        try {
        	
        	groupCourseService.save(newGroupCourse);
        	
        } catch (IllegalStateException e) {
        	
            model.addAttribute("error", "Group is already enrolled in this course.");
            return "edit-course";
        }
        
        return "redirect:/courses";
    }
	
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete-course/{id}")
    public String deleteUser(@PathVariable Long id) {

    	TeacherCourse teacherCourse = teacherCourseService.findByCourseId(id);
    	List<GroupCourse> groupCourses = groupCourseService.findByCourseId(id);
    	
    	for (GroupCourse groupCourse : groupCourses) {
    		groupCourseService.delete(groupCourse);
    	}
    	
    	teacherCourseService.delete(teacherCourse);
        courseService.delete(id);

        return "redirect:/courses";
    }

}

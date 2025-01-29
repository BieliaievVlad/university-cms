package ua.foxminded.tasks.university_cms.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditCoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.form.GroupsFormData;

@Service
public class FormService {
	
	private final CourseService courseService;
	private final GroupService groupService;
	private final StudentService studentService;
	private final TeacherService teacherService;
	private final GroupCourseService groupCourseService;
	private final TeacherCourseService teacherCourseService;
	
	public FormService(CourseService courseService, GroupService groupService, StudentService studentService,
					   TeacherService teacherService, GroupCourseService groupCourseService, 
					   TeacherCourseService teacherCourseService) {
		this.courseService = courseService;
		this.groupService = groupService;
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.groupCourseService = groupCourseService;
		this.teacherCourseService = teacherCourseService;
	}
	
	public CoursesFormData prepareCoursesFormData() {
		
		CoursesFormData data = new CoursesFormData();
		
		List<Course> courses = courseService.findAll();
		List<TeacherCourse> teacherCourses = courseService.prepareTeacherCourseList(courses);
		Map<Course, List<Group>> courseGroupsMap = courseService.prepareCourseGroupsMap(courses);
		
		data.setTeacherCourses(teacherCourses);
		data.setCourseGroupsMap(courseGroupsMap);
		
		return data;
	}
	
	public CourseFormData prepareCourseFormData(Long id) {
		
		CourseFormData data = new CourseFormData();
		
	    Map<Course, List<Group>> courseGroupsMap = new HashMap<>();
		List<Course> courses = courseService.findAll();
		List<Teacher> teachers = teacherService.findAll();
		List<Group> groups = groupService.findAll();
        Course course = courseService.findById(id);

		Teacher teacher = teacherService.findByCourse(course);
		TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
		
	    for (Course c : courses) {
	        List<Group> g = groupService.findByCourse(c);
	        courseGroupsMap.put(c, g);
	    }

	    data.setTeacherCourse(teacherCourse);
	    data.setGroups(groups);
	    data.setTeachers(teachers);
	    data.setCourseGroupsMap(courseGroupsMap);
		
		return data;
	}
	
	public EditGroupsFormData prepareEditGroupsFormData(Long id) {
		
		EditGroupsFormData data = new EditGroupsFormData();
		
		List<Group> allGroups = groupService.findAll();
	    Map<Long, List<Group>> courseGroupsMap = new HashMap<>();
        Course course = courseService.findById(id);
        List<Group> groups = groupService.findByCourse(course);

        courseGroupsMap.put(id, groups);
        
        List<Group> filteredGroups = allGroups.stream()
                .filter(g -> !groups.contains(g))
                .collect(Collectors.toList());
		
        data.setCourse(course);
        data.setCourseGroupsMap(courseGroupsMap);
        data.setFilteredGroups(filteredGroups);
        
		return data;
	}
	
	public GroupsFormData prepareGroupsFormData() {
		
		GroupsFormData data = new GroupsFormData();
		
		List<Group> groups = groupService.findAll();
		Map<Group, List<Course>> groupCoursesMap = new HashMap<>();
		
		for(Group g : groups) {
			List<Course> c = groupCourseService.findByGroup(g);
			groupCoursesMap.put(g, c);
		}
		
		data.setGroups(groups);
		data.setGroupCoursesMap(groupCoursesMap);
		
		return data;
	}
	
	public EditCoursesFormData prepareEditCoursesFormData(Long id) {
		
		EditCoursesFormData data = new EditCoursesFormData();
		
		Group group = groupService.findById(id);
		List<Course> allCourses = courseService.findAll();
		List<Course> courses = groupCourseService.findByGroup(group);
		List<Course> filteredCourses = allCourses.stream()
                .filter(c -> !courses.contains(c))
                .collect(Collectors.toList());
		
		data.setGroup(group);
		data.setFilteredCourses(filteredCourses);
		
		return data;
	}
	
	public List<Student> prepareStudentsFormData() {
		
		List<Student> students = studentService.findAll();
		
	    students = students.stream()
                .sorted(Comparator.comparing(Student::getId))
                .collect(Collectors.toList());
	    
	    return students;
	}

}

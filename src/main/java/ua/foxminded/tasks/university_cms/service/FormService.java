package ua.foxminded.tasks.university_cms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditCoursesFormData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.form.GroupsFormData;
import ua.foxminded.tasks.university_cms.form.LogsFormData;
import ua.foxminded.tasks.university_cms.form.SchedulesFormData;
import ua.foxminded.tasks.university_cms.form.TeachersFormData;

@Service
public class FormService {
	
	private final CourseService courseService;
	private final GroupService groupService;
	private final StudentService studentService;
	private final TeacherService teacherService;
	private final GroupCourseService groupCourseService;
	private final TeacherCourseService teacherCourseService;
	private final ScheduleService scheduleService;
	private final AuditLogService auditLogService; 
	
	public FormService(CourseService courseService, GroupService groupService, StudentService studentService,
					   TeacherService teacherService, GroupCourseService groupCourseService, 
					   TeacherCourseService teacherCourseService, ScheduleService scheduleService, 
					   AuditLogService auditLogService) {
		this.courseService = courseService;
		this.groupService = groupService;
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.groupCourseService = groupCourseService;
		this.teacherCourseService = teacherCourseService;
		this.scheduleService = scheduleService;
		this.auditLogService = auditLogService;
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
	
	public TeachersFormData prepareTeachersFormData() {
		
		TeachersFormData data = new TeachersFormData();
		
		List<Teacher> teachers = teacherService.findAll();
		
		teachers = teachers.stream()
				.sorted(Comparator.comparing(Teacher::getId))
				.collect(Collectors.toList());
		
		data.setTeachers(teachers);
		
		Map<Teacher, List<TeacherCourse>> map = new HashMap<>();
		
		for(Teacher t : teachers) {
			
			List<TeacherCourse> tcs = teacherCourseService.findByTeacherId(t.getId());
			map.put(t, tcs);
		}
		data.setTeacherCoursesMap(map);
		
		return data;
	}
	
	public SchedulesFormData prepareSchedulesForm() {
		
        List<Course> courses = courseService.findAll();
        List<Group> groups = groupService.findAll();
        List<Teacher> teachers = teacherService.findAll();
        List<Student> students = studentService.findAll();
        
        return new SchedulesFormData(courses, groups, teachers, students);
	}
	
	public List<Course> prepareEditScheduleCourseForm(Long id) {
		
		Schedule schedule = scheduleService.findById(id);
		Course course = schedule.getCourse();
		List<Course> courses = courseService.findAll();
		
		return courses.stream()
					  .filter(c -> !c.equals(course))
					  .collect(Collectors.toList());
	}
	
	public List<Group> prepareEditScheduleGroupForm(Long id) {
		
		Schedule schedule = scheduleService.findById(id);
		Group group = schedule.getGroup();
		List<Group> groups = groupService.findAll();
		
		return groups.stream()
				.filter(g -> !g.equals(group))
				.collect(Collectors.toList());
	}
	
	public List<Schedule> prepareScheduleListForTeacher(List<TeacherCourse> teacherCourses) {
		
		List<Course> courses = new ArrayList<>();
		
		for (TeacherCourse tc : teacherCourses) {
			courses.add(tc.getCourse());
		}
		
		return scheduleService.filterByWeek(courses);
	}
	
	public List<Schedule> prepareScheduleListForGroup(List<Course> courses) {
		
		return scheduleService.filterByWeek(courses);
	}
	
	public List<Schedule> prepareScheduleListForStudent(Student student) {
		
		List<Course> courses = groupCourseService.findByGroup(student.getGroup());
		
		return scheduleService.filterByWeek(courses);
	}
	
	public LogsFormData prepareLogsForm() {
		
		LogsFormData data = new LogsFormData();
		
		List<String> usernames = auditLogService.findUsernames();
		List<String> tableNames = auditLogService.findTableNames();
		List<String> operations = auditLogService.findOperations();
		
		data.setUsernames(usernames);
		data.setTableNames(tableNames);
		data.setOperations(operations);
		
		return data;
	}

}

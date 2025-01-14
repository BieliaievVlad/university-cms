package ua.foxminded.tasks.university_cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.form.CourseFormData;
import ua.foxminded.tasks.university_cms.form.CoursesData;
import ua.foxminded.tasks.university_cms.form.EditGroupsFormData;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

	@Autowired
	TeacherService teacherService;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	TeacherCourseService teacherCourseService;
	
	@Autowired
	GroupCourseService groupCourseService;
	
	@Autowired
	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course findById(Long searchId) {

		Optional<Course> optCourse = courseRepository.findById(searchId);

		if (optCourse.isPresent()) {

			return optCourse.get();

		} else {
			throw new EntityNotFoundException("Course is not found.");
		}
	}

	public void save(Course course) {

		if (!isCourseValid(course)) {

			throw new IllegalArgumentException("Course is not valid.");
		}
		courseRepository.save(course);
	}

	public void delete(Long courseId) {

		Optional<Course> optCourse = courseRepository.findById(courseId);

		if (optCourse.isPresent()) {

			courseRepository.delete(optCourse.get());

		} else {
			throw new EntityNotFoundException("Course is not found.");
		}
	}
	
	public CoursesData prepareCoursesData() {
		
		CoursesData data = new CoursesData();
		
		List<Course> courses = courseRepository.findAll();
		List<TeacherCourse> teacherCourses = prepareTeacherCourseList(courses);
		Map<Course, List<Group>> courseGroupsMap = prepareCourseGroupsMap(courses);
		
		data.setTeacherCourses(teacherCourses);
		data.setCourseGroupsMap(courseGroupsMap);
		
		return data;
	}
	
	public void saveCourse(String courseName, Long teacherId) {
		
    	Course newCourse = new Course();
    	Course course = new Course();
    	Teacher teacher = new Teacher();
    	TeacherCourse newTeacherCourse = new TeacherCourse();
    	
    	newCourse.setName(courseName);    	
    	courseRepository.save(newCourse);
    	
    	teacher = teacherService.findById(teacherId);
    	course = findById(newCourse.getId());
    	    	
    	newTeacherCourse.setTeacher(teacher);
    	newTeacherCourse.setCourse(course);
    	
    	teacherCourseService.save(newTeacherCourse);
	}
	
	public CourseFormData prepareCourseFormData(Long id) {
		
		CourseFormData data = new CourseFormData();
		
	    Map<Course, List<Group>> courseGroupsMap = new HashMap<>();
		List<Course> courses = courseRepository.findAll();
		List<Teacher> teachers = teacherService.findAll();
		List<Group> groups = groupService.findAll();
        Course course = findById(id);

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
	
	public void updateTeacherCourse(TeacherCourse teacherCourse) {
		
		Long id = teacherCourse.getCourse().getId();
		Course course = findById(id);
		TeacherCourse oldTeacherCourse = teacherCourseService.findByCourseId(id);		
        TeacherCourse newTeacherCourse = new TeacherCourse();
        newTeacherCourse.setTeacher(teacherCourse.getTeacher());
        newTeacherCourse.setCourse(course);
        
        teacherCourseService.delete(oldTeacherCourse);
        teacherCourseService.save(newTeacherCourse);
	}
	
	public EditGroupsFormData prepareEditGroupsFormData(Long id) {
		
		EditGroupsFormData data = new EditGroupsFormData();
		
		List<Group> allGroups = groupService.findAll();
	    Map<Long, List<Group>> courseGroupsMap = new HashMap<>();
        Course course = findById(id);
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
	
	public void updateGroups(Long groupId, Long courseId) {
		
		Group group = groupService.findById(groupId);
		Course course = findById(courseId);
		GroupCourse newGroupCourse = new GroupCourse();

		newGroupCourse.setGroup(group);
		newGroupCourse.setCourse(course);
		
		groupCourseService.save(newGroupCourse);
	}
	
	public void deleteGroup(Long groupId, Long courseId) {
		
    	Group group = groupService.findById(groupId);
    	Course course = findById(courseId);
    	
    	GroupCourse groupCourse = new GroupCourse(group, course);
    	
    	groupCourseService.delete(groupCourse);
	}
	
	public void deleteCourse(Long id) {
		
    	TeacherCourse teacherCourse = teacherCourseService.findByCourseId(id);
    	List<GroupCourse> groupCourses = groupCourseService.findByCourseId(id);
    	
    	for (GroupCourse groupCourse : groupCourses) {
    		groupCourseService.delete(groupCourse);
    	}
    	
    	teacherCourseService.delete(teacherCourse);
        delete(id);
	}
	
	 List<TeacherCourse> prepareTeacherCourseList(List<Course> courses) {
		
		List<TeacherCourse> teacherCourses = new ArrayList<>();
		for (Course course : courses) {
			Teacher teacher = teacherService.findByCourse(course);

			if (teacher.getId() != 0L) {

				TeacherCourse teacherCourse = new TeacherCourse(teacher, course);
				teacherCourses.add(teacherCourse);
			} else {

				Teacher dummyTeacher = new Teacher(0L, "dummy", "dummy");
				TeacherCourse teacherCourse = new TeacherCourse(dummyTeacher, course);
				teacherCourses.add(teacherCourse);
			}
		}		
		return teacherCourses;
	}
	
	Map<Course, List<Group>> prepareCourseGroupsMap(List<Course> courses) {
		
		Map<Course, List<Group>> courseGroupsMap = new HashMap<>();
		for (Course course : courses) {
			List<Group> groups = groupService.findByCourse(course);
			courseGroupsMap.put(course, groups);
		}
		return courseGroupsMap;
	}

	private boolean isCourseValid(Course course) {
		return course != null && course.getName() != null;
	}

}

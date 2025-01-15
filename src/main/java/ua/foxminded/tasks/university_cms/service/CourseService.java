package ua.foxminded.tasks.university_cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;
	private final TeacherService teacherService;
	private final GroupService groupService;
	
	@Autowired
	public CourseService(CourseRepository courseRepository, TeacherService teacherService, GroupService groupService) {
		this.courseRepository = courseRepository;
		this.teacherService = teacherService;
		this.groupService = groupService;
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

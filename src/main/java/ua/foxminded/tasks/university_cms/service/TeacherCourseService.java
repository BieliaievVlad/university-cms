package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.entity.TeacherCourseId;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;

@Service
public class TeacherCourseService {

	private final TeacherCourseRepository repository;
	private final CourseService courseService;
	private final TeacherService teacherService;
	private final GroupCourseService groupCourseService;

	@Autowired
	public TeacherCourseService(TeacherCourseRepository repository, 
								CourseService courseService, 
								TeacherService teacherService, 
								GroupCourseService groupCourseService) {
		this.repository = repository;
		this.courseService = courseService;
		this.teacherService = teacherService;
		this.groupCourseService = groupCourseService;
	}
	
	public List<TeacherCourse> findAll() {
		
		return repository.findAll();
	}
	
	public void save(TeacherCourse teacherCourse) {
		
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		Optional<TeacherCourse> optional = repository.findById(id);

		if (optional.isEmpty()) {
			
			TeacherCourse newTeacherCourse= new TeacherCourse(teacherCourse.getTeacher(),
															  teacherCourse.getCourse());
			repository.save(newTeacherCourse);

		} else {
			throw new IllegalStateException("Teacher is already assigned to a course.");
		}
	}

	public void delete(TeacherCourse teacherCourse) {

		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		Optional<TeacherCourse> optional = repository.findById(id);

		if (optional.isPresent()) {
			
			TeacherCourse newTeacherCourse= new TeacherCourse(teacherCourse.getTeacher(),
					  										  teacherCourse.getCourse());
			repository.delete(newTeacherCourse);

		} else {
			throw new IllegalStateException("Teacher is not assigned to a course.");
		}
	}
	
	public void updateTeacherCourse(TeacherCourse teacherCourse) {
		
		Long id = teacherCourse.getCourse().getId();
		Course course = courseService.findById(id);
		TeacherCourse oldTeacherCourse = findByCourseId(id);		
        TeacherCourse newTeacherCourse = new TeacherCourse();
        newTeacherCourse.setTeacher(teacherCourse.getTeacher());
        newTeacherCourse.setCourse(course);
        
        delete(oldTeacherCourse);
        save(newTeacherCourse);
	}
	
	public void addCourseToTeacher(Long teacherId, Long courseId) {
		
		Teacher teacher = teacherService.findById(teacherId);
		Course course = courseService.findById(courseId);
		TeacherCourse oldTeacherCourse = findByCourseId(courseId);
		
		delete(oldTeacherCourse);
		save(new TeacherCourse(teacher, course));
	}
	
	public void deleteCourseFromTeacher(Long courseId) {

		Course course = courseService.findById(courseId);
		Teacher dummy = new Teacher(0L, "dummy", "dummy");
		TeacherCourse oldTeacherCourse = findByCourseId(courseId);
		
		delete(oldTeacherCourse);
		save(new TeacherCourse(dummy, course));
	}
	
	public void deleteTeacherCourse(Long id) {
		
    	TeacherCourse teacherCourse = findByCourseId(id);
    	List<GroupCourse> groupCourses = groupCourseService.findByCourseId(id);
    	
    	for (GroupCourse groupCourse : groupCourses) {
    		groupCourseService.delete(groupCourse);
    	}
    	
    	delete(teacherCourse);
        courseService.delete(id);
	}
	
	public void saveCourse(String courseName, Long teacherId) {
		
    	Course newCourse = new Course();
    	TeacherCourse newTeacherCourse = new TeacherCourse();
    	
    	newCourse.setName(courseName);    	
    	courseService.save(newCourse);
    	
    	Teacher teacher = teacherService.findById(teacherId);
    	Course course = courseService.findById(newCourse.getId());
    	    	
    	newTeacherCourse.setTeacher(teacher);
    	newTeacherCourse.setCourse(course);
    	
    	save(newTeacherCourse);
	}
	
	public void deleteTeacherAndAssociations(Long id) {

		List<TeacherCourse> list = findByTeacherId(id);
		
		for (TeacherCourse tc : list) {
			deleteCourseFromTeacher(tc.getCourse().getId());
		}
		teacherService.delete(id);
	}
	
	public List<TeacherCourse> prepareFilteredCoursesList(Long id) {
		
		List<TeacherCourse> all = findAll();
		List<TeacherCourse> list = findByTeacherId(id);
 
		return all.stream()
				  .filter(course -> !list.contains(course))
				  .collect(Collectors.toList());
	}
	
	public TeacherCourse findByCourseId(Long courseId) {
		
		return repository.findByCourseId(courseId);
	}
	
	public List<TeacherCourse> findByTeacherId(Long teacherId) {
		
		return repository.findByTeacherId(teacherId);
	}
}

package ua.foxminded.tasks.university_cms.db.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.db.entity.Course;
import ua.foxminded.tasks.university_cms.db.repository.CourseRepository;

@Service
public class CourseService {
	
	private final CourseRepository repository;
	
	@Autowired
	public CourseService(CourseRepository repository) {
		this.repository = repository;
	}
	
	public List<Course> findAll() {
		return repository.findAll();
	}
	
	public Course findById(Long searchId) {
		
		Optional<Course> optCourse = repository.findById(searchId);
		
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
		repository.save(course);
	}
	
	public void delete(Long courseId) {
		
		Optional<Course> optCourse = repository.findById(courseId);
		
		if (optCourse.isPresent()) {
			
			repository.delete(optCourse.get());
			
		} else {
			throw new EntityNotFoundException("Course is not found.");
		}
	}
	
	private boolean isCourseValid(Course course) {
		return course != null &&
				course.getId() != null &&
				course.getName() != null;
	}

}

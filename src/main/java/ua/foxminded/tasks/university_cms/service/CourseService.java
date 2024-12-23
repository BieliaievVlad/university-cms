package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

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

	private boolean isCourseValid(Course course) {
		return course != null && course.getName() != null;
	}

}

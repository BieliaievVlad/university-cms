package ua.foxminded.tasks.university_cms.db.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.db.entity.StudentCourse;
import ua.foxminded.tasks.university_cms.db.entity.StudentCourseId;
import ua.foxminded.tasks.university_cms.db.repository.StudentCourseRepository;

@Service
public class StudentCourseService {
	
	private final StudentCourseRepository repository;
	
	@Autowired
	public StudentCourseService(StudentCourseRepository repository) {
		this.repository = repository;
	}
	
	public void save(StudentCourse studentCourse) {

		StudentCourseId id = new StudentCourseId(studentCourse.getStudent().getId(), studentCourse.getCourse().getId());
		Optional<StudentCourse> optional = repository.findById(id);
		
		if (optional.isEmpty()) {
			
			repository.save(studentCourse);
			
		} else {
			throw new IllegalStateException("Student is already enrolled to a course.");
		}
	}
	
	public void delete(StudentCourse studentCourse) {

		StudentCourseId id = new StudentCourseId(studentCourse.getStudent().getId(), studentCourse.getCourse().getId());
		Optional<StudentCourse> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			
			repository.delete(studentCourse);
			
		} else {
			throw new IllegalStateException("Student is not enrolled to a course.");
		}
	}

}

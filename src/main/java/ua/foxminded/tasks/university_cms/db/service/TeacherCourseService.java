package ua.foxminded.tasks.university_cms.db.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.db.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.db.entity.TeacherCourseId;
import ua.foxminded.tasks.university_cms.db.repository.TeacherCourseRepository;

@Service
public class TeacherCourseService {
	
	private final TeacherCourseRepository repository;
	
	@Autowired
	public TeacherCourseService(TeacherCourseRepository repository) {
		this.repository = repository;
	}
	
	public void save(TeacherCourse teacherCourse) {
		
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		Optional<TeacherCourse> optional = repository.findById(id);
		
		if (optional.isEmpty()) {
			repository.save(teacherCourse);
			
		} else {
			throw new IllegalStateException("Teacher is already assigned to a course.");
		}
	}
	
	public void delete(TeacherCourse teacherCourse) {
		
		TeacherCourseId id = new TeacherCourseId(teacherCourse.getTeacher().getId(), teacherCourse.getCourse().getId());
		Optional<TeacherCourse> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			repository.delete(teacherCourse);
			
		} else {
			throw new IllegalStateException("Teacher is not assigned to a course.");
		}
	}

}

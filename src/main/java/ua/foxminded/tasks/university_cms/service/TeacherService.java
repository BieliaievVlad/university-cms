package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@Service
public class TeacherService {
	
	private final TeacherRepository repository;
	
	@Autowired
	public TeacherService(TeacherRepository repository) {
		this.repository = repository;
	}
	
	public List<Teacher> findAll() {
		return repository.findAll();
	}
	
	public Teacher findById(Long searchId) {
		
		Optional<Teacher> optTeacher = repository.findById(searchId);
		
		if (optTeacher.isPresent()) {
			
			return optTeacher.get();
			
		} else {
			throw new EntityNotFoundException("Teacher is not found.");
		}
	}
	
	public void save(Teacher teacher) {
		
		if (!isTeacherValid(teacher)) {
			throw new IllegalArgumentException("Teacher is not valid.");
		}
		
		repository.save(teacher);
	}
	
	public void delete(Long teacherId) {
		
		Optional<Teacher> optTeacher = repository.findById(teacherId);
		
		if (optTeacher.isPresent()) {
			
			repository.delete(optTeacher.get());
			
		} else {
			throw new EntityNotFoundException("Teacher is not found.");
		}
	}
	
	private boolean isTeacherValid(Teacher teacher) {
		return teacher != null &&
				teacher.getId() != null &&
				teacher.getFirstName() != null &&
				teacher.getLastName() != null;
	}

}

package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;

@Service
public class StudentService {

	private final StudentRepository repository;

	@Autowired
	public StudentService(StudentRepository repository) {
		this.repository = repository;
	}

	public List<Student> findAll() {

		return repository.findAll();
	}

	public Student findById(Long searchId) {

		Optional<Student> optStudent = repository.findById(searchId);

		if (optStudent.isPresent()) {

			return optStudent.get();

		} else {
			throw new EntityNotFoundException("Student is not found.");
		}
	}

	public void save(Student student) {

		if (!isStudentValid(student)) {

			throw new IllegalArgumentException("Student is not valid.");
		}

		repository.save(student);

	}

	public void delete(Long studentId) {

		Optional<Student> optStudent = repository.findById(studentId);

		if (optStudent.isPresent()) {

			repository.delete(optStudent.get());

		} else {
			throw new EntityNotFoundException("Student is not found.");
		}

	}
	
	public List<Student> findByGroupId(Long groupId) {
		
		return repository.findByGroupId(groupId);
	}

	private boolean isStudentValid(Student student) {
		return student != null && student.getId() != null && student.getFirstName() != null
				&& student.getLastName() != null;
	}

}

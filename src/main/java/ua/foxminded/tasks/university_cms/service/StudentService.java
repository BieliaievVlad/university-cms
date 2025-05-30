package ua.foxminded.tasks.university_cms.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;
import ua.foxminded.tasks.university_cms.specification.StudentSpecification;

@Service
public class StudentService {

	private final StudentRepository repository;
	private final GroupService groupService;
	private final SecurityService securityService;

	@Autowired
	public StudentService(StudentRepository repository, GroupService groupService,
						  SecurityService securityService) {
		this.repository = repository;
		this.groupService = groupService;
		this.securityService = securityService;
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

		securityService.setCurrentUserFromSecurityContext();
		repository.save(student);

	}

	public void delete(Long studentId) {

		Optional<Student> optStudent = repository.findById(studentId);

		if (optStudent.isPresent()) {

			securityService.setCurrentUserFromSecurityContext();
			repository.delete(optStudent.get());

		} else {
			throw new EntityNotFoundException("Student is not found.");
		}

	}
	
	public List<Student> findByGroupId(Long groupId) {
		
		return repository.findByGroupId(groupId);
	}
	
	public void addStudent(String firstName, String lastName) {
		
    	Group dummyGroup = groupService.findById(0L);
		Student newStudent = new Student(firstName, lastName, dummyGroup);
		save(newStudent);
	}
	
	public void updateStudentGroup(Long studentId, Long groupId) {
		
		Group group = groupService.findById(groupId);
		Student student = findById(studentId);

		student.setGroup(group);

		save(student);
	}
	
	public void updateStudentName(Long id, String firstName, String lastName) {
		
		Student student = findById(id);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		
		save(student);
	}
	
	public List<Student> filterStudents (Long groupId) {
		
		Specification<Student> specification = Specification.where(StudentSpecification.filterByGroupId(groupId));
		List<Student> students = repository.findAll(specification);
		
		return students.stream()
                .sorted(Comparator.comparing(Student::getId))
                .collect(Collectors.toList());
	}

	private boolean isStudentValid(Student student) {
		return student != null && student.getFirstName() != null
				&& student.getLastName() != null;
	}
}

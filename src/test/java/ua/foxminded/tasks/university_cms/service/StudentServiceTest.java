package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.User;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceTest {

	@MockBean
	StudentRepository repository;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	SecurityService securityService;

	@Autowired
	StudentService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {
		Student student = new Student("FirstName", "LastName");
		List<Student> expectedList = Arrays.asList(student);
		when(repository.findAll()).thenReturn(expectedList);

		List<Student> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Student expected = new Student("FirstName", "LastName");
		Optional<Student> optStudent = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optStudent);

		Student actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);
	}

	@Test
	void save_ValidValue_CalledOnce() {
		User user = new User("username", "password");
		Student student = new Student(1L, "FirstName", "LastName", user);
		when(repository.save(student)).thenReturn(student);

		service.save(student);

		verify(repository, times(1)).save(student);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Student student = new Student("FirstName", "LastName");
		when(repository.findById(id)).thenReturn(Optional.of(student));
		doNothing().when(repository).delete(student);

		service.delete(id);

		verify(repository, times(1)).delete(student);
	}
	
	@Test
	void addStudent_ValidValue_CalledMethods() {
		
		Long dummyGroupId = 0L;
		String firstName = "First_Name";
		String lastName = "Last_Name";
		Group dummyGroup = new Group(0L, "dummy", 0L);
		Student student = new Student("First_Name", "Last_Name");
		
		when(groupRepository.findById(dummyGroupId)).thenReturn(Optional.of(dummyGroup));
		when(repository.save(any(Student.class))).thenReturn(student);
		
		service.addStudent(firstName, lastName);
		
		verify(groupRepository, times(1)).findById(dummyGroupId);
		verify(repository, times(1)).save(any(Student.class));
	}
	
	@Test
	void updateStudentGroup_ValidValue_CalledMethods() {
		
		Long groupId = 0L;
		Long studentId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Student student = new Student("First_Name", "Last_Name");
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(repository.findById(studentId)).thenReturn(Optional.of(student));
		when(repository.save(any(Student.class))).thenReturn(student);
		
		service.updateStudentGroup(studentId, groupId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(repository, times(1)).findById(studentId);
		verify(repository, times(1)).save(any(Student.class));
	}
	
	@Test
	void updateStudentName_ValidValue_CalledMethods() {
		
		Long id = 1L;
		String firstName = "";
		String lastName = "";
		Student student = new Student("First_Name", "Last_Name");
		student.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(student));
		when(repository.save(any(Student.class))).thenReturn(student);
		
		service.updateStudentName(id, firstName, lastName);
		
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).save(any(Student.class));
	}
	
	@Test
	void filterStudents_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Group group = new Group(1L, "Group_Name", 10L);
		Student student1 = new Student("First_Name_1", "Last_Name_1", group);
		student1.setId(2L);
		Student student2 = new Student("First_Name_2", "Last_Name_2", group);
		student2.setId(1L);
		List<Student> expected = List.of(student2, student1);
		
		when(repository.findAll(any(Specification.class))).thenReturn(expected);
		
		List<Student> actual = service.filterStudents(group.getId());
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class));
	}
	
	@Test
	void filterStudents_EmptyGroupId_CalledMethodsAndReturnsExpected() {
		
		Long groupId = null;
		Group group1 = new Group(1L, "Group_Name_1", 10L);
		Group group2 = new Group(2L, "Group_Name_2", 20L);
		Student student1 = new Student("First_Name_1", "Last_Name_1", group1);
		student1.setId(2L);
		Student student2 = new Student("First_Name_2", "Last_Name_2", group2);
		student2.setId(1L);
		List<Student> expected = List.of(student2, student1);
		
		when(repository.findAll(any(Specification.class))).thenReturn(expected);
		
		List<Student> actual = service.filterStudents(groupId);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class));
	}

}

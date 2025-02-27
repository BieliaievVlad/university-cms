package ua.foxminded.tasks.university_cms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.GroupCourseId;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;
import ua.foxminded.tasks.university_cms.repository.StudentRepository;

@SpringBootTest
@ActiveProfiles("test")
class GroupCourseServiceTest {
	
	@MockBean
	CourseRepository courseRepository;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	StudentRepository studentRepository;
	
	@MockBean
	GroupCourseRepository groupCourseRepository;
	
	@MockBean
	ScheduleRepository scheduleRepository;
	
	@MockBean
	SecurityService securityService;

	@Autowired
	GroupCourseService service;

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		when(groupCourseRepository.save(groupCourse)).thenReturn(groupCourse);

		service.save(groupCourse);

		verify(groupCourseRepository, times(1)).save(groupCourse);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 22L);
		GroupCourse groupCourse = new GroupCourse(group, course);
		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		when(groupCourseRepository.findById(id)).thenReturn(Optional.of(groupCourse));
		doNothing().when(groupCourseRepository).delete(groupCourse);

		service.delete(groupCourse);

		verify(groupCourseRepository, times(1)).delete(groupCourse);
	}
	
	@Test
	void findByGroup_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		List<Course> expected = List.of(course);
		
		when(groupCourseRepository.findByGroupId(id)).thenReturn(List.of(new GroupCourse(group, course)));
		
		List<Course> actual = service.findByGroup(group);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(groupCourseRepository, times(1)).findByGroupId(id);
		
	}
	
	@Test
	void updateGroupCourse_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.save(any(GroupCourse.class))).thenReturn(groupCourse);
		
		service.updateGroupCourse(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).save(any(GroupCourse.class));
	}
	
	@Test
	void deleteGroupCourse_ValidValue_CalledMethods() {
		
		Long groupId = 1L;
		Long courseId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		GroupCourse groupCourse = new GroupCourse(group, course);
		
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(groupCourseRepository.findById(groupCourse.getId())).thenReturn(Optional.of(groupCourse));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		
		service.deleteGroupCourse(groupId, courseId);
		
		verify(groupRepository, times(1)).findById(groupId);
		verify(courseRepository, times(1)).findById(courseId);
		verify(groupCourseRepository, times(1)).findById(groupCourse.getId());
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
	}
	
	@Test
	void addStudentToGroup_ValidValue_CalledMethods() {
		
		Long studentId = 1L;
		Long groupId = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Student student = new Student("First_Name", "Last_Name");
		
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.save(student)).thenReturn(student);
		
		service.addStudentToGroup(studentId, groupId);
		
		verify(studentRepository, times(1)).findById(studentId);
		verify(groupRepository, times(1)).findById(groupId);
		verify(studentRepository, times(1)).save(student);
		
	}
	
	@Test
	void removeStudentFromGroup_ValidValue_CalledMethods() {
		
		Long id = 1L;
		Student student = new Student("First_Name", "Last_Name");
		Group dummyGroup = new Group(0L, "dummy", 0L);
		
		when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		when(groupRepository.findById(0L)).thenReturn(Optional.of(dummyGroup));
		when(studentRepository.save(student)).thenReturn(student);
		
		service.removeStudentFromGroup(id);
		
		verify(studentRepository, times(1)).findById(id);
		verify(groupRepository, times(1)).findById(0L);
		verify(studentRepository, times(1)).save(student);
	}
	
	@Test
	void deleteGroupAndAssociations_ValidValue_CalledMethods() {
		
		Long id = 1L;
		Group group = new Group(1L, "Group_Name", 10L);
		Course course = new Course(1L, "Course_Name");
		Student student = new Student("First_Name", "Last_Name");
		student.setId(1L);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule schedule = new Schedule(date, group, course);
		schedule.setId(1L);
		
		when(groupCourseRepository.findByGroupId(id)).thenReturn(List.of(new GroupCourse(group, course)));
		when(studentRepository.findByGroupId(id)).thenReturn(List.of(student));
		when(scheduleRepository.findByGroupId(anyLong())).thenReturn(List.of(schedule));
		when(groupCourseRepository.findById(any(GroupCourseId.class))).thenReturn(Optional.of(new GroupCourse(group, course)));
		doNothing().when(groupCourseRepository).delete(any(GroupCourse.class));
		when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		doNothing().when(scheduleRepository).delete(any(Schedule.class));
		doNothing().when(groupRepository).delete(any(Group.class));
		
		service.deleteGroupAndAssociations(id);
		
		verify(groupCourseRepository, times(1)).findByGroupId(id);
		verify(studentRepository, times(1)).findByGroupId(id);
		verify(scheduleRepository, times(1)).findByGroupId(id);
		verify(groupCourseRepository, times(1)).findById(any(GroupCourseId.class));
		verify(groupCourseRepository, times(1)).delete(any(GroupCourse.class));
		verify(studentRepository, times(1)).findById(anyLong());
		verify(groupRepository, times(2)).findById(anyLong());
		verify(studentRepository, times(1)).save(any(Student.class));
		verify(scheduleRepository, times(1)).findById(anyLong());
		verify(scheduleRepository, times(1)).delete(any(Schedule.class));
		verify(groupRepository, times(1)).delete(any(Group.class));	
	}

}

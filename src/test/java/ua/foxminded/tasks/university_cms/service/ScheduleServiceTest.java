package ua.foxminded.tasks.university_cms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.repository.CourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;

@SpringBootTest
@ActiveProfiles("test")
class ScheduleServiceTest {

	@MockBean
	ScheduleRepository repository;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	CourseRepository courseRepository;

	@Autowired
	ScheduleService service;

	@Test
	void findAll_ValidValue_ReturnsExpectedList() {

		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 10L);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule schedule = new Schedule(date, group, course);
		List<Schedule> expectedList = Arrays.asList(schedule);
		when(repository.findAll()).thenReturn(expectedList);

		List<Schedule> actualList = service.findAll();

		assertEquals(expectedList, actualList);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_ReturnsExpected() {

		Long searchId = 1L;
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 10L);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule expected = new Schedule(date, group, course);
		Optional<Schedule> optSсhedule = Optional.of(expected);
		when(repository.findById(searchId)).thenReturn(optSсhedule);

		Schedule actual = service.findById(searchId);

		assertEquals(expected, actual);
		verify(repository, times(1)).findById(searchId);

	}

	@Test
	void save_ValidValue_CalledOnce() {
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 10L);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule schedule = new Schedule(1L, date, group, course);
		when(repository.save(schedule)).thenReturn(schedule);

		service.save(schedule);

		verify(repository, times(1)).save(schedule);
	}

	@Test
	void delete_ValidValue_CalledOnce() {
		Long id = 1L;
		Course course = new Course(1L, "Course_1");
		Group group = new Group(1L, "Group_1", 10L);
		LocalDateTime date = LocalDateTime.of(2024, 10, 10, 11, 30);
		Schedule schedule = new Schedule(date, group, course);
		when(repository.findById(id)).thenReturn(Optional.of(schedule));
		doNothing().when(repository).delete(schedule);

		service.delete(id);

		verify(repository, times(1)).delete(schedule);
	}
	
	@Test
	void findByCourses_ValidValue_CalledMethodsAndReturnsExpected() {
		
		LocalDateTime dateTime1 = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course1 = new Course(1L, "Course_name1");
		Group group1 = new Group(1L, "Group_Name1");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 1, 31, 15, 30);
		Course course2 = new Course(2L, "Course_name2");
		Group group2 = new Group(2L, "Group_Name2");
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);
		List<Schedule> expected = List.of(schedule2);
		
		when(repository.findAll()).thenReturn(List.of(schedule1, schedule2));
		
		List<Schedule> actual = service.findByCourses(List.of(course2));
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll();
		
	}
	
	@Test
	void addSchedule_ValidValue_CalledMethods() {
		
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name1");
		Group group = new Group(1L, "Group_Name1");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(repository.save(any(Schedule.class))).thenReturn(schedule);
		
		service.addSchedule(dateTime, course.getId(), group.getId());
		
		verify(courseRepository, times(1)).findById(anyLong());
		verify(groupRepository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Schedule.class));
	}
	
	@Test
	void updateScheduleCourse_ValidValue_CalledMethods() {

		Long courseId = 1L;
		Long scheduleId = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name1");
		Group group = new Group(1L, "Group_Name1");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(schedule));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		when(repository.save(any(Schedule.class))).thenReturn(schedule);
		
		service.updateScheduleCourse(courseId, scheduleId);
		
		verify(repository, times(1)).findById(anyLong());
		verify(courseRepository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Schedule.class));
	}
	
	@Test
	void updateScheduleGroup_ValidValue_CalledMethods() {

		Long groupId = 1L;
		Long scheduleId = 1L;
		LocalDateTime dateTime = LocalDateTime.of(2024, 1, 31, 15, 30);
		Course course = new Course(1L, "Course_name1");
		Group group = new Group(1L, "Group_Name1");
		Schedule schedule = new Schedule(1L, dateTime, group, course);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(schedule));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(repository.save(any(Schedule.class))).thenReturn(schedule);
		
		service.updateScheduleGroup(groupId, scheduleId);
		
		verify(repository, times(1)).findById(anyLong());
		verify(groupRepository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Schedule.class));
	}
	
    @Test
    void filterSchedules_ValidValue_CalledMethodsAndReturnsExpected() {
    	
    	Long courseId = 1L;
    	Long groupId = 1L;
    	Long teacherId = 1L;
    	Long studentId = 1L;
    	String startDate = "2025-02-07";
    	String endDate = "2025-02-08";
		LocalDateTime dateTime1 = LocalDateTime.of(2025, 2, 7, 15, 30);
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 2, 8, 15, 30);
		Course course1 = new Course(1L, "Course_name_1");
		Course course2 = new Course(2L, "Course_name_2");
		Group group1 = new Group(1L, "Group_Name_1");
		Group group2 = new Group(2L, "Group_Name_2");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);

        List<Schedule> expected = List.of(schedule1, schedule2);

        when(repository.findAll(any(Specification.class))).thenReturn(List.of(schedule1))
        												  .thenReturn(List.of(schedule2));

        List<Schedule> actual = service.filterSchedules(startDate, endDate, courseId, groupId, teacherId, studentId);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(repository, times(2)).findAll(any(Specification.class));
    }
    
    @Test
    void filterSchedules_EmptyDate_CalledMethodsAndReturnsExpected() {
    	
    	Long courseId = 1L;
    	Long groupId = 1L;
    	Long teacherId = 1L;
    	Long studentId = 1L;
    	String startDate = "";
    	String endDate = "";
		LocalDateTime dateTime1 = LocalDateTime.of(2025, 2, 7, 15, 30);
		LocalDateTime dateTime2 = LocalDateTime.of(2025, 2, 8, 15, 30);
		LocalDateTime dateTime3 = LocalDateTime.of(2025, 2, 9, 15, 30);
		Course course1 = new Course(1L, "Course_name_1");
		Course course2 = new Course(2L, "Course_name_2");
		Course course3 = new Course(3L, "Course_name_3");
		Group group1 = new Group(1L, "Group_Name_1");
		Group group2 = new Group(2L, "Group_Name_2");
		Group group3 = new Group(3L, "Group_Name_3");
		Schedule schedule1 = new Schedule(1L, dateTime1, group1, course1);
		Schedule schedule2 = new Schedule(2L, dateTime2, group2, course2);
		Schedule schedule3 = new Schedule(3L, dateTime3, group3, course3);

        List<Schedule> expected = List.of(schedule1, schedule2, schedule3);

        when(repository.findAll(any(Specification.class))).thenReturn(expected);

        List<Schedule> actual = service.filterSchedules(startDate, endDate, courseId, groupId, teacherId, studentId);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(repository, times(1)).findAll(any(Specification.class));
    }

}

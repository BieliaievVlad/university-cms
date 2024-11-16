package ua.foxminded.tasks.university_cms.db.service;

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
import org.springframework.test.context.ActiveProfiles;

import ua.foxminded.tasks.university_cms.db.entity.Course;
import ua.foxminded.tasks.university_cms.db.entity.Group;
import ua.foxminded.tasks.university_cms.db.entity.Schedule;
import ua.foxminded.tasks.university_cms.db.repository.ScheduleRepository;

@SpringBootTest
@ActiveProfiles("test")
class ScheduleServiceTest {

    @MockBean
    ScheduleRepository repository;
    
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

}

package ua.foxminded.tasks.university_cms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;

@Service
public class ScheduleService {

	private final ScheduleRepository repository;
	private final GroupService groupService;
	private final CourseService courseService;

	@Autowired
	public ScheduleService(ScheduleRepository repository, GroupService groupService, CourseService courseService) {
		this.repository = repository;
		this.groupService = groupService;
		this.courseService = courseService;
	}

	public List<Schedule> findAll() {
		return repository.findAll();
	}

	public Schedule findById(Long searchId) {

		Optional<Schedule> optSсhedule = repository.findById(searchId);

		if (optSсhedule.isPresent()) {
			return optSсhedule.get();

		} else {
			throw new EntityNotFoundException("Sсhedule is not found.");
		}
	}

	public void save(Schedule schedule) {

		if (!isSсheduleValid(schedule)) {
			throw new IllegalArgumentException("Sсhedule is not valid.");
		}
		repository.save(schedule);
	}

	public void delete(Long sсheduleId) {

		Optional<Schedule> optSсhedule = repository.findById(sсheduleId);

		if (optSсhedule.isPresent()) {

			repository.delete(optSсhedule.get());

		} else {
			throw new EntityNotFoundException("Shedule is not found.");
		}
	}
	
	public void addSchedule(LocalDateTime dateTime) {
				
		Schedule schedule = new Schedule();
		schedule.setDateTime(dateTime);
		schedule.setCourse(new Course(0L, "dummy"));
		schedule.setGroup(new Group(0L, "dummy"));
		
		save(schedule);
	}
	
	public void updateScheduleCourse(Long courseId, Long scheduleId) {
		
		Schedule schedule = findById(scheduleId);
		Course course = courseService.findById(courseId);
		schedule.setCourse(course);
		save(schedule);
	}
	
	public void updateScheduleGroup(Long groupId, Long scheduleId) {
		
		Schedule schedule = findById(scheduleId);
		Group group = groupService.findById(groupId);
		schedule.setGroup(group);
		save(schedule);
	}
	
	public List<Schedule> findByCourses(List<Course> courses) {

		List<Schedule> allSchedules = findAll();
		List<Schedule> filteredSchedules = new ArrayList<>();

		for (Course c : courses) {
			filteredSchedules
					.addAll(allSchedules.stream()
										.filter(s -> s.getCourse().equals(c))
										.collect(Collectors.toList()));
		}
		filteredSchedules.sort(Comparator.comparing(Schedule::getDateTime));
		
		return filteredSchedules;
	}

	private boolean isSсheduleValid(Schedule schedule) {
		return schedule != null && schedule.getDateTime() != null &&
				schedule.getGroup() != null && schedule.getCourse() != null;
	}

}

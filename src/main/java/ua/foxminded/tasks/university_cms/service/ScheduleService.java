package ua.foxminded.tasks.university_cms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.repository.ScheduleRepository;
import ua.foxminded.tasks.university_cms.specification.ScheduleSpecification;
import ua.foxminded.tasks.university_cms.util.DateUtil;

@Service
public class ScheduleService {

	private final ScheduleRepository repository;
	private final GroupService groupService;
	private final CourseService courseService;
	private final DateUtil dateUtil;

	@Autowired
	public ScheduleService(ScheduleRepository repository, GroupService groupService, 
						   CourseService courseService, DateUtil dateUtil) {
		this.repository = repository;
		this.groupService = groupService;
		this.courseService = courseService;
		this.dateUtil = dateUtil;
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
			throw new EntityNotFoundException("Schedule is not found.");
		}
	}
	
	public List<Schedule> findByGroupId(Long groupId) {
		return repository.findByGroupId(groupId);
	}
	
	public List<Schedule> findByCourseId(Long courseId) {
		return repository.findByCourseId(courseId);
	}
	
	public void addSchedule(LocalDateTime dateTime, Long courseId, Long groupId) {
				
		Schedule schedule = new Schedule();
		Course course = courseService.findById(courseId);
		Group group = groupService.findById(groupId);
		
		schedule.setDateTime(dateTime);
		schedule.setCourse(course);
		schedule.setGroup(group);
		
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
	
    public List<Schedule> filterSchedules(String startDate, String endDate, 
			  							  Long courseId, Long groupId, 
			  							  Long teacherId, Long studentId) {
    	LocalDate start = null;
    	LocalDate end = null;
    	List<Schedule> schedulesList = new ArrayList<>();
    	
    	if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
    		start = LocalDate.parse(startDate);
    		end = LocalDate.parse(endDate);
    		
        	List<LocalDate> dates = dateUtil.getDateListOfInterval(start, end);
        	
        	for (LocalDate d : dates) {
        		
                Specification<Schedule> specification = Specification.where(ScheduleSpecification.filterByCourseId(courseId))
    					   											 .and(ScheduleSpecification.filterByGroupId(groupId))
    					   											 .and(ScheduleSpecification.filterByTeacherId(teacherId))
    					   											 .and(ScheduleSpecification.filterByStudentId(studentId))
    					   											 .and(ScheduleSpecification.filterByDate(d));
                List<Schedule> schedules = repository.findAll(specification);
                schedulesList.addAll(schedules);
        	}
        	return schedulesList.stream()
                    			.sorted(Comparator.comparing(Schedule::getDateTime))
                    			.collect(Collectors.toList());
    	} else {
    		
    		LocalDate date = null;
            Specification<Schedule> specification = Specification.where(ScheduleSpecification.filterByCourseId(courseId))
						 										 .and(ScheduleSpecification.filterByGroupId(groupId))
						 										 .and(ScheduleSpecification.filterByTeacherId(teacherId))
						 										 .and(ScheduleSpecification.filterByStudentId(studentId))
						 										 .and(ScheduleSpecification.filterByDate(date));
            return repository.findAll(specification);
    	}
    }
    
    public List<Schedule> filterByWeek(List<Course> courses) {
    	
		List<Schedule> schedules = findByCourses(courses);
		List<LocalDate> dates = dateUtil.getDateListOfWeek();
		
		return schedules.stream()
						.filter(schedule -> dates.contains(schedule.getDateTime().toLocalDate()))
						.collect(Collectors.toList());
    }

	private boolean isSсheduleValid(Schedule schedule) {
		return schedule != null && schedule.getDateTime() != null &&
				schedule.getGroup() != null && schedule.getCourse() != null;
	}

}

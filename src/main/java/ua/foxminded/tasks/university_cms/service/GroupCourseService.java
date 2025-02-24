package ua.foxminded.tasks.university_cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.GroupCourseId;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;

@Service
public class GroupCourseService {

	private final GroupCourseRepository repository;
	private final CourseService courseService;
	private final GroupService groupService;
	private final StudentService studentService;
	private final ScheduleService scheduleService;
	private final SecurityService securityService;

	@Autowired
	public GroupCourseService(GroupCourseRepository repository, 
							  CourseService courseService, 
							  GroupService groupService, 
							  StudentService studentService,
							  ScheduleService scheduleService,
							  SecurityService securityService) {
		this.repository = repository;
		this.courseService = courseService;
		this.groupService = groupService;
		this.studentService = studentService;
		this.scheduleService = scheduleService;
		this.securityService = securityService;
	}

	public void save(GroupCourse groupCourse) {

		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		Optional<GroupCourse> optional = repository.findById(id);

		if (optional.isEmpty()) {
			
			GroupCourse newGroupCourse = new GroupCourse(groupCourse.getGroup(),
														 groupCourse.getCourse());
			securityService.setCurrentUserFromSecurityContext();
			repository.save(newGroupCourse);

		} else {
			throw new IllegalStateException("Group is already enrolled to a course.");
		}
	}

	public void delete(GroupCourse groupCourse) {

		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		Optional<GroupCourse> optional = repository.findById(id);

		if (optional.isPresent()) {

			securityService.setCurrentUserFromSecurityContext();
			repository.delete(groupCourse);

		} else {
			throw new IllegalStateException("Group is not enrolled to a course.");
		}
	}
	
	public List<Course>findByGroup(Group group) {
		
		Long groupId = group.getId();
		List<GroupCourse> groupCourses = findByGroupId(groupId);
		List<Course> courses = new ArrayList<>();
		for(GroupCourse gc : groupCourses) {
			courses.add(gc.getCourse());
		}
		
		return courses;
	}
	
	public void updateGroupCourse(Long groupId, Long courseId) {
		
		Group group = groupService.findById(groupId);
		Course course = courseService.findById(courseId);
		GroupCourse newGroupCourse = new GroupCourse();

		newGroupCourse.setGroup(group);
		newGroupCourse.setCourse(course);
		
		save(newGroupCourse);
	}
	
	public void deleteGroupCourse(Long groupId, Long courseId) {
		
    	Group group = groupService.findById(groupId);
    	Course course = courseService.findById(courseId);
    	
    	GroupCourse groupCourse = new GroupCourse(group, course);
    	
    	delete(groupCourse);
	}
	
	public void addStudentToGroup(Long studentId, Long groupId) {
		
		Student student = studentService.findById(studentId);
		Group group = groupService.findById(groupId);
		student.setGroup(group);
		
		studentService.save(student);
	}
	
	public void removeStudentFromGroup(Long id) {
		
		Student student = studentService.findById(id);
		Group dummyGroup = groupService.findById(0L);
		student.setGroup(dummyGroup);
		
		studentService.save(student);
	}
	
	public void deleteGroupAndAssociations(Long id) {
		
		List<GroupCourse> groupCourses = findByGroupId(id);
		List<Student> students = studentService.findByGroupId(id);
		List<Schedule> schedules = scheduleService.findByGroupId(id);
		
		for (GroupCourse gc : groupCourses) {
			delete(gc);
		}
		
		for (Student s : students) {
			Long studentId = s.getId();
			removeStudentFromGroup(studentId);
		}
		
		for (Schedule s : schedules) {
			Long scheduleId = s.getId();
			scheduleService.delete(scheduleId);
		}
		
		groupService.delete(id);
	}
	
	public List<GroupCourse> findByCourseId(Long courseId) {
		
		return repository.findByCourseId(courseId);
	}
	
	public List<GroupCourse> findByGroupId(Long groupId) {
		
		return repository.findByGroupId(groupId);
	}

}

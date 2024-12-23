package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.GroupCourseId;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;

@Service
public class GroupCourseService {

	private final GroupCourseRepository repository;

	@Autowired
	public GroupCourseService(GroupCourseRepository repository) {
		this.repository = repository;
	}

	public void save(GroupCourse groupCourse) {

		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		Optional<GroupCourse> optional = repository.findById(id);

		if (optional.isEmpty()) {
			
			GroupCourse newGroupCourse = new GroupCourse(groupCourse.getGroup(),
														 groupCourse.getCourse());
			repository.save(newGroupCourse);

		} else {
			throw new IllegalStateException("Group is already enrolled to a course.");
		}
	}

	public void delete(GroupCourse groupCourse) {

		GroupCourseId id = new GroupCourseId(groupCourse.getGroup().getId(), groupCourse.getCourse().getId());
		Optional<GroupCourse> optional = repository.findById(id);

		if (optional.isPresent()) {

			repository.delete(groupCourse);

		} else {
			throw new IllegalStateException("Group is not enrolled to a course.");
		}
	}
	
	public List<GroupCourse> findByCourseId(Long courseId) {
		
		return repository.findByCourseId(courseId);
	}

}

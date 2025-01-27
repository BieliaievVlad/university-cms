package ua.foxminded.tasks.university_cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.repository.GroupCourseRepository;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;

@Service
public class GroupService {

	private final GroupRepository groupRepository;
	private final GroupCourseRepository groupCourseRepository;

	@Autowired
	public GroupService(GroupRepository groupRepository, GroupCourseRepository groupCourseRepository) {
		this.groupRepository = groupRepository;
		this.groupCourseRepository = groupCourseRepository;
	}

	public List<Group> findAll() {

		return groupRepository.findAll();
	}

	public Group findById(Long searchId) {

		Optional<Group> optGroup = groupRepository.findById(searchId);

		if (optGroup.isPresent()) {

			return optGroup.get();

		} else {
			throw new EntityNotFoundException("Group is not found.");
		}
	}

	public void save(Group group) {

		if (!isGroupValid(group)) {

			throw new IllegalArgumentException("Group is not valid.");
		}

		groupRepository.save(group);
	}

	public void delete(Long groupId) {

		Optional<Group> optGroup = groupRepository.findById(groupId);

		if (optGroup.isPresent()) {

			groupRepository.delete(optGroup.get());

		} else {
			throw new EntityNotFoundException("Group is not found.");
		}
	}
	
	public List<Group> findByCourse(Course course) {
		
		Long courseId = course.getId();
		List<GroupCourse> groupCourses = groupCourseRepository.findByCourseId(courseId);
		List<Group> groups = new ArrayList<>();
		
		for (GroupCourse groupCourse : groupCourses) {
			
			groups.add(groupCourse.getGroup());
		}
		return groups;
	}
	
	public void saveGroup(String groupName) {
		
		Group newGroup = new Group(groupName);
		
		groupRepository.save(newGroup);
	}

	private boolean isGroupValid(Group group) {
		return group != null &&  group.getName() != null;
	}
}

package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Group;
import ua.foxminded.tasks.university_cms.repository.GroupRepository;

@Service
public class GroupService {
	
	private final GroupRepository repository;
	
	@Autowired
	public GroupService(GroupRepository repository) {
		this.repository = repository;
	}
	
	public List<Group> findAll() {
		
		return repository.findAll();
	}
	
	public Group findById(Long searchId) {
		
		Optional<Group> optGroup = repository.findById(searchId);
		
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
		
		repository.save(group);
	}
	
	public void delete(Long groupId) {
		
		Optional<Group> optGroup = repository.findById(groupId);
		
		if (optGroup.isPresent()) {
			
			repository.delete(optGroup.get());
			
		} else {
			throw new EntityNotFoundException("Group is not found.");
		}
	}
	
	private boolean isGroupValid(Group group) {
		return group != null &&
				group.getId() != null &&
				group.getName() != null;
	}
}

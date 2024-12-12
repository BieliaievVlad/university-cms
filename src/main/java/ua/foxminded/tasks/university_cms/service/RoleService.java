package ua.foxminded.tasks.university_cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.Role;
import ua.foxminded.tasks.university_cms.repository.RoleRepository;

@Service
public class RoleService {
	
	private final RoleRepository repository;
	
	@Autowired
	public RoleService(RoleRepository repository) {
		this.repository = repository;
	}
	
	public Role findByName(String name) {
		
		return repository.findByName(name);
	}

}

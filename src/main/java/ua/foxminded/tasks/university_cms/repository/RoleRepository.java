package ua.foxminded.tasks.university_cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.tasks.university_cms.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);

}

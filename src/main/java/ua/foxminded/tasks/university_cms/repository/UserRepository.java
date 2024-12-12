package ua.foxminded.tasks.university_cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
	
	User findByUsername(String username);

}

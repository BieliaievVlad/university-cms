package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.User;
import ua.foxminded.tasks.university_cms.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository repository;

	@Autowired
	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public List<User> findAll() {
		return repository.findAll();
	}

	public User findById(Long searchId) {

		Optional<User> optUser = repository.findById(searchId);

		if (optUser.isPresent()) {

			return optUser.get();

		} else {
			throw new EntityNotFoundException("User is not found.");
		}
	}
	
	public void save(User user) {
		
		if (!isUserValid(user)) {
			
			throw new IllegalArgumentException("User is not valid.");
		}
		repository.save(user);
	}
	
	public void delete(Long userId) {
		
		Optional<User> optUser = repository.findById(userId);
		
		if (optUser.isPresent()) {
			
			repository.delete(optUser.get());
			
		} else {
			throw new EntityNotFoundException("User is not found.");
		}
	}
	
	private boolean isUserValid(User user) {
		return user != null && user.getUsername() != null && user.getPassword() != null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

}

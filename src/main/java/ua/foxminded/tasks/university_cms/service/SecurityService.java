package ua.foxminded.tasks.university_cms.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	
	private final JdbcTemplate jdbcTemplate;
	
	public SecurityService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

    public void setCurrentUserFromSecurityContext() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        jdbcTemplate.execute("DELETE FROM university.current_user_session;"); 
        jdbcTemplate.execute("INSERT INTO university.current_user_session (name) VALUES ('" + username + "')");
    }
}

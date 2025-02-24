package ua.foxminded.tasks.university_cms.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.AuditLog;
import ua.foxminded.tasks.university_cms.repository.AuditLogRepository;

@Service
public class AuditLogService {
	
	private final AuditLogRepository repository;
	
	@Autowired
	public AuditLogService(AuditLogRepository repository) {
		this.repository = repository;
	}
	
	public List<AuditLog> findAll() {
		
		List<AuditLog> logs = repository.findAll();
		
		return logs.stream()
				.sorted(Comparator.comparing(AuditLog :: getId).reversed())
				.collect(Collectors.toList());
	}

}

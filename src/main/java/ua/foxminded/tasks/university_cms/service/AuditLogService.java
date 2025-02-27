package ua.foxminded.tasks.university_cms.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.entity.AuditLog;
import ua.foxminded.tasks.university_cms.repository.AuditLogRepository;
import ua.foxminded.tasks.university_cms.specification.AuditLogSpecification;
import ua.foxminded.tasks.university_cms.util.DateUtil;

@Service
public class AuditLogService {
	
	private final AuditLogRepository repository;
	private final DateUtil dateUtil;
	
	@Autowired
	public AuditLogService(AuditLogRepository repository, DateUtil dateUtil) {
		this.repository = repository;
		this.dateUtil = dateUtil;
	}
	
	public List<AuditLog> findAll() {
		
		List<AuditLog> logs = repository.findAll();
		
		return logs.stream()
				.sorted(Comparator.comparing(AuditLog :: getId).reversed())
				.collect(Collectors.toList());
	}
	
	public List<AuditLog> filterAuditLogs(String startDate, 
										  String endDate, 
										  String username, 
										  String tableName, 
										  String operationType) {
		LocalDate start = null;
		LocalDate end = null;
		List<AuditLog> auditLogsList = new ArrayList<>();
		
    	if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
    		start = LocalDate.parse(startDate);
    		end = LocalDate.parse(endDate);
    		
        	List<LocalDate> dates = dateUtil.getDateListOfInterval(start, end);
        	
        	for (LocalDate date : dates) {
        		Specification<AuditLog> specification = Specification.where(AuditLogSpecification.filterByDate(date))
        															  .and(AuditLogSpecification.filterByUser(username))
        															  .and(AuditLogSpecification.filterByTable(tableName))
        															  .and(AuditLogSpecification.filterByOperation(operationType));
        		List<AuditLog> logs = repository.findAll(specification);
        		auditLogsList.addAll(logs);
        		}
        	return auditLogsList.stream()
        			.sorted(Comparator.comparing(AuditLog :: getId).reversed())
    				.collect(Collectors.toList());
        	
        	} else {
        		LocalDate date = null;
        		Specification<AuditLog> specification = Specification.where(AuditLogSpecification.filterByDate(date))
						  												.and(AuditLogSpecification.filterByUser(username))
						  												.and(AuditLogSpecification.filterByTable(tableName))
						  												.and(AuditLogSpecification.filterByOperation(operationType));
        		return repository.findAll(specification).stream()
        							.sorted(Comparator.comparing(AuditLog :: getId).reversed())
        							.collect(Collectors.toList());
        	}
	}
	
	public List<String> findUsernames() {
		
		return repository.findDistinctUsernames();
	}
	
	public List<String> findTableNames() {
		
		return repository.findDistinctTableNames();
	}
	
	public List<String> findOperations() {
		
		return repository.findDistinctOperations();
	}

}

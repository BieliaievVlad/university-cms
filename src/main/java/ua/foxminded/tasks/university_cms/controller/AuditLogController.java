package ua.foxminded.tasks.university_cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.tasks.university_cms.entity.AuditLog;
import ua.foxminded.tasks.university_cms.service.AuditLogService;

@Controller
public class AuditLogController {
	
	private final AuditLogService service;
	
	@Autowired
	public AuditLogController(AuditLogService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/logs")
	public String showLogsForm(Model model) {
		
		List<AuditLog> logs = service.findAll();
		
		model.addAttribute("logs", logs);
		return "logs";
	}

}

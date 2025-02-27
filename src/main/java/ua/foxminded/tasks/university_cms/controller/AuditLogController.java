package ua.foxminded.tasks.university_cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.tasks.university_cms.entity.AuditLog;
import ua.foxminded.tasks.university_cms.form.LogsFormData;
import ua.foxminded.tasks.university_cms.service.AuditLogService;
import ua.foxminded.tasks.university_cms.service.FormService;

@Controller
public class AuditLogController {
	
	private final AuditLogService service;
	private final FormService formService;
	
	@Autowired
	public AuditLogController(AuditLogService service, FormService formService) {
		this.service = service;
		this.formService = formService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/logs")
	public String showLogsForm(@RequestParam(value = "startDate", required = false) String startDate,
							   @RequestParam(value = "endDate", required = false) String endDate,
							   @RequestParam(value = "username", required = false) String username,
							   @RequestParam(value = "tableName", required = false) String tableName, 
							   @RequestParam(value = "operationType", required = false) String operationType, 
							   Model model) {
		
	    if ((startDate == null ||startDate.isEmpty()) || (endDate == null || endDate.isEmpty())) {
            model.addAttribute("dateAlert", "Please choose both dates or leave it empty to list all Logs");
        }
		
		List<AuditLog> logs = service.filterAuditLogs(startDate, endDate, username, tableName, operationType);
		LogsFormData data = formService.prepareLogsForm();
		
		model.addAttribute("logs", logs);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("username", username);
		model.addAttribute("tableName", tableName);
		model.addAttribute("operationType", operationType);
		model.addAttribute("usernames", data.getUsernames());
		model.addAttribute("tableNames", data.getTableNames());
		model.addAttribute("operations", data.getOperations());
		
		return "logs";
	}

}

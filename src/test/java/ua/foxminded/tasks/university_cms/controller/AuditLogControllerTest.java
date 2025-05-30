package ua.foxminded.tasks.university_cms.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.foxminded.tasks.university_cms.entity.AuditLog;
import ua.foxminded.tasks.university_cms.form.LogsFormData;
import ua.foxminded.tasks.university_cms.service.AuditLogService;
import ua.foxminded.tasks.university_cms.service.DataGeneratorService;
import ua.foxminded.tasks.university_cms.service.FormService;

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	AuditLogService service;
	
	@MockBean
	FormService formService;
	
	@MockBean
	DataGeneratorService dataService;
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void showLogsForm_LogsPageRequest_ReturnLogsView() throws Exception {

		AuditLog log = new AuditLog();
		
		when(formService.prepareLogsForm()).thenReturn(new LogsFormData());
		when(service.filterAuditLogs(anyString(), anyString(), anyString(), anyString(), anyString()))
							.thenReturn(List.of(log));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/logs"))
	   	   		.andExpect(MockMvcResultMatchers.status().isOk())
	   	   		.andExpect(MockMvcResultMatchers.view().name("logs"));
	}

}

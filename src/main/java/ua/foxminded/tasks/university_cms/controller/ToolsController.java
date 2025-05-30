package ua.foxminded.tasks.university_cms.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToolsController {

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/tools")
	public String showToolsForm() {
		return "tools";
	}
}

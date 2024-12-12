package ua.foxminded.tasks.university_cms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
}


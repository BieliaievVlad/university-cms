package ua.foxminded.tasks.university_cms.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.tasks.university_cms.entity.Role;
import ua.foxminded.tasks.university_cms.entity.User;
import ua.foxminded.tasks.university_cms.service.RoleService;
import ua.foxminded.tasks.university_cms.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public String showUsers(Model model) {
		
		List<User> users = userService.findAll();
		model.addAttribute("users" ,users);
		
		return "users";
	}
	
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add-user")
    public String showAddUserForm() {
        return "add-user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-user")
    public String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        Role newUserRole = roleService.findByName(role);
        newUser.setRoles(Collections.singleton(newUserRole));

        userService.save(newUser);

        return "redirect:/users";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {

        userService.delete(id);

        return "redirect:/users";
    }

}

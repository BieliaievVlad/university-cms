package ua.foxminded.tasks.university_cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ua.foxminded.tasks.university_cms.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        	.userDetailsService(userService)
            .csrf().disable()
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            	.and()
            .authorizeRequests()
            	.requestMatchers("/", "/login").permitAll()
            	.requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/users").hasRole("ADMIN")
                .requestMatchers("/students", "/teachers", "/courses").hasAnyRole("ADMIN", "STAFF", "TEACHER", "STUDENT")
                .anyRequest().authenticated();

        httpSecurity
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll();

        httpSecurity
            .logout()
                .permitAll()
                .logoutSuccessUrl("/");

        return httpSecurity.build();
    }
}

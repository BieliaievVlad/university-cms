package ua.foxminded.tasks.university_cms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import ua.foxminded.tasks.university_cms.service.DataGeneratorService;

@SpringBootApplication
public class UniversityCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityCmsApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner commandLineRunner(DataGeneratorService dataGeneratorService) {
		return args -> {
			dataGeneratorService.generateData();
		};
	}

}

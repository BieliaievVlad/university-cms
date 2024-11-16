package ua.foxminded.tasks.university_cms;

import org.springframework.boot.SpringApplication;

public class TestUniversityCmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(UniversityCmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

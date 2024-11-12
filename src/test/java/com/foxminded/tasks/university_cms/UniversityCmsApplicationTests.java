package com.foxminded.tasks.university_cms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class UniversityCmsApplicationTests {

	@Test
	void contextLoads() {
	}

}

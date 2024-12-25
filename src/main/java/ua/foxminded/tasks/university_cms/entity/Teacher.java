package ua.foxminded.tasks.university_cms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers", schema = "university")
@NoArgsConstructor
public class Teacher extends Person {

	public Teacher(Long id, String firstName, String lastName, User user) {
		super(id, firstName, lastName, user);
	}

	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public Teacher(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}

}

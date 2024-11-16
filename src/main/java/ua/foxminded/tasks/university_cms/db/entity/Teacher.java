package ua.foxminded.tasks.university_cms.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers", schema = "university")
public class Teacher extends Person {
	
	public Teacher() {
		super();
	}
	
	public Teacher(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
}

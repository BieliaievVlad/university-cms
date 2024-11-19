package ua.foxminded.tasks.university_cms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table (name = "students", schema = "university")
@Getter
@Setter
@NoArgsConstructor
public class Student extends Person {
	
	@ManyToOne
	@JoinColumn (name = "group_id", nullable = true)
	private Group group;
	
	public Student(Long id, String firstName, String lastName, Group group) {
		super(id, firstName, lastName);
		this.group = group;
	}
	
	public Student(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
	public Student(String firstName, String lastName, Group group) {
		super(firstName, lastName);
		this.group = group;
	}

	public Student(String firstName, String lastName) {
		super(firstName, lastName);
	}

}

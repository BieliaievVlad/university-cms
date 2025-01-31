package ua.foxminded.tasks.university_cms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "groups", schema = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	@NonNull
	private String name;

	@Column(name = "num_students", nullable = true)
	private Long numStudents;
	
	public Group(String name) {
		this.name = name;
	}
	public Group(Long id, String name) {
		this.id = id;
		this.name = name;
	}

}

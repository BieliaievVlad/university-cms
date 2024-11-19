package ua.foxminded.tasks.university_cms.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "schedule", schema = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "date_time", nullable = false)
	@NonNull
	private LocalDateTime dateTime;
	
	@ManyToOne
	@JoinColumn(name = "group_id")
	@NonNull
	private Group group;
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	@NonNull
	private Course course;

}

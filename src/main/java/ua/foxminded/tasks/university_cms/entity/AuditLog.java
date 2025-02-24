package ua.foxminded.tasks.university_cms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_log", schema = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "timestamp")
	private LocalDateTime timestamp;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "operation_type")
	private String operationType;
	
	@Column(name = "old_data")
	private String oldData;
	
	@Column(name = "new_data")
	private String newData;
}

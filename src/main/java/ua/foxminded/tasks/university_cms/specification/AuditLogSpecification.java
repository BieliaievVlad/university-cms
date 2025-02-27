package ua.foxminded.tasks.university_cms.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import ua.foxminded.tasks.university_cms.entity.AuditLog;

public class AuditLogSpecification {
	
	public static Specification<AuditLog> filterByDate(LocalDate date) {
		return (root, query, criteriaBuilder) -> {
			if (date != null) {
				return criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get("timestamp")), date);
			}
			return criteriaBuilder.conjunction();
		};
	}
	
	public static Specification<AuditLog> filterByUser(String username) {
		return (root, query, criteriaBuilder) -> {
			if (username != null) {
				return criteriaBuilder.like(root.get("username"), "%" + username + "%");
			}
			return criteriaBuilder.conjunction();
		};
	}
	
	public static Specification<AuditLog> filterByTable(String tableName) {
		return (root, query, criteriaBuilder) -> {
			if (tableName != null) {
				return criteriaBuilder.equal(root.get("tableName"), tableName);
			}
			return criteriaBuilder.conjunction();
		};
	}
	
	public static Specification<AuditLog> filterByOperation(String operationType) {
		return (root, query, criteriaBuilder) -> {
			if (operationType != null) {
				return criteriaBuilder.like(root.get("operationType"), "%" + operationType + "%");
			}
			return criteriaBuilder.conjunction();
		};
	}
}

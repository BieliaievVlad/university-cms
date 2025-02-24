package ua.foxminded.tasks.university_cms.specification;

import org.springframework.data.jpa.domain.Specification;

import ua.foxminded.tasks.university_cms.entity.Student;

public class StudentSpecification {

    public static Specification<Student> filterByGroupId(Long groupId) {
        return (root, query, criteriaBuilder) -> {
            if (groupId != null) {
                return criteriaBuilder.equal(root.get("group").get("id"), groupId);
            }
            return criteriaBuilder.conjunction();
        };
    }
}

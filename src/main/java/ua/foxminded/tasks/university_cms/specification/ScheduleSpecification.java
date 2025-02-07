package ua.foxminded.tasks.university_cms.specification;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import ua.foxminded.tasks.university_cms.entity.Schedule;
import ua.foxminded.tasks.university_cms.entity.Student;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;

public class ScheduleSpecification {

    public static Specification<Schedule> filterByCourseId(Long courseId) {
        return (root, query, criteriaBuilder) -> {
            if (courseId != null) {
                return criteriaBuilder.equal(root.get("course").get("id"), courseId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Schedule> filterByGroupId(Long groupId) {
        return (root, query, criteriaBuilder) -> {
            if (groupId != null) {
                return criteriaBuilder.equal(root.get("group").get("id"), groupId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Schedule> filterByDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date != null) {
                return criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get("date")), date);
            }
            return criteriaBuilder.conjunction();
        };
    }
    
    public static Specification<Schedule> filterByTeacherId(Long teacherId) {
        return (root, query, criteriaBuilder) -> {
            if (teacherId != null) {

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<TeacherCourse> teacherCourseRoot = subquery.from(TeacherCourse.class);
                subquery.select(teacherCourseRoot.get("course").get("id"));
                subquery.where(criteriaBuilder.equal(teacherCourseRoot.get("teacher").get("id"), teacherId));

                return criteriaBuilder.in(root.get("course").get("id")).value(subquery);
            }
            return criteriaBuilder.conjunction();
        };
    }
    
    public static Specification<Schedule> filterByStudentId(Long studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId != null) {

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Student> studentRoot = subquery.from(Student.class);
                subquery.select(studentRoot.get("group").get("id"));
                subquery.where(criteriaBuilder.equal(studentRoot.get("id"), studentId));

                return criteriaBuilder.in(root.get("group").get("id")).value(subquery);
            }
            return criteriaBuilder.conjunction();
        };
    }
}

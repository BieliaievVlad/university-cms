package ua.foxminded.tasks.university_cms.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.db.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}

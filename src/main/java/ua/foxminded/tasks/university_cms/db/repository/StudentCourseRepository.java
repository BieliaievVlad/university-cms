package ua.foxminded.tasks.university_cms.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.db.entity.StudentCourse;
import ua.foxminded.tasks.university_cms.db.entity.StudentCourseId;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {

}

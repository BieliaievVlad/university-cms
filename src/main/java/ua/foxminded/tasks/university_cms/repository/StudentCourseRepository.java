package ua.foxminded.tasks.university_cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.StudentCourse;
import ua.foxminded.tasks.university_cms.entity.StudentCourseId;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {

}

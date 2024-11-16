package ua.foxminded.tasks.university_cms.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.db.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.db.entity.TeacherCourseId;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, TeacherCourseId> {

}

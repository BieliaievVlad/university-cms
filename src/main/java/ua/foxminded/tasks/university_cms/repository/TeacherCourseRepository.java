package ua.foxminded.tasks.university_cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.entity.TeacherCourseId;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, TeacherCourseId> {
	
	TeacherCourse findByCourseId(Long courseId);
	List<TeacherCourse> findByTeacherId(Long teacherId);
	Optional<TeacherCourse> findById(TeacherCourseId id);

}

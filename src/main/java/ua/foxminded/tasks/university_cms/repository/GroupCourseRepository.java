package ua.foxminded.tasks.university_cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.GroupCourse;
import ua.foxminded.tasks.university_cms.entity.GroupCourseId;

@Repository
public interface GroupCourseRepository extends JpaRepository<GroupCourse, GroupCourseId> {

	List<GroupCourse> findByCourseId(Long courseId);

}

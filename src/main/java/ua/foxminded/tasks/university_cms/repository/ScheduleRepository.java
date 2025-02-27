package ua.foxminded.tasks.university_cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
	
	List<Schedule> findByGroupId(Long groupId);
	List<Schedule> findByCourseId(Long courseId);

}

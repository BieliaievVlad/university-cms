package ua.foxminded.tasks.university_cms.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.db.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	
}

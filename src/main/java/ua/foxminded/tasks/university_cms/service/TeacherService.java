package ua.foxminded.tasks.university_cms.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.entity.Course;
import ua.foxminded.tasks.university_cms.entity.Teacher;
import ua.foxminded.tasks.university_cms.entity.TeacherCourse;
import ua.foxminded.tasks.university_cms.repository.TeacherCourseRepository;
import ua.foxminded.tasks.university_cms.repository.TeacherRepository;

@Service
public class TeacherService {

	private final TeacherRepository teacherRepository;
	private final TeacherCourseRepository teacherCourseRepository;
	Logger logger = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	public TeacherService(TeacherRepository teacherRepository, TeacherCourseRepository teacherCourseRepository) {
		this.teacherRepository = teacherRepository;
		this.teacherCourseRepository = teacherCourseRepository;
	}

	public List<Teacher> findAll() {
		return teacherRepository.findAll();
	}

	public Teacher findById(Long searchId) {

		Optional<Teacher> optTeacher = teacherRepository.findById(searchId);

		if (optTeacher.isPresent()) {

			return optTeacher.get();

		} else {
			throw new EntityNotFoundException("Teacher is not found.");
		}
	}

	public void save(Teacher teacher) {

		if (!isTeacherValid(teacher)) {
			throw new IllegalArgumentException("Teacher is not valid.");
		}

		teacherRepository.save(teacher);
	}

	public void delete(Long teacherId) {

		Optional<Teacher> optTeacher = teacherRepository.findById(teacherId);

		if (optTeacher.isPresent()) {

			teacherRepository.delete(optTeacher.get());

		} else {
			throw new EntityNotFoundException("Teacher is not found.");
		}
	}
	
	public void updateTeacherName(Long id, String firstName, String lastName) {

		Teacher teacher = findById(id);
		teacher.setFirstName(firstName);
		teacher.setLastName(lastName);
		save(teacher);
	}
	
	public Teacher findByCourse(Course course) {
		
		Long courseId = course.getId();
		TeacherCourse teacherCourse = teacherCourseRepository.findByCourseId(courseId);
		
		if (teacherCourse != null && teacherCourse.getTeacher() != null) {
			
			Long teacherId = teacherCourse.getTeacher().getId();
			Optional<Teacher> optTeacher = teacherRepository.findById(teacherId);
			
			if (optTeacher.isPresent()) {
				return optTeacher.get();
				
			} else {
				throw new IllegalArgumentException("Teacher not found.");
			}
		} else {
			logger.error("Teacher is not assigned to a course.");
			Teacher dummyTeacher = new Teacher(0L, "dummy", "dummy");
			teacherCourseRepository.save(new TeacherCourse(dummyTeacher, course));
			return dummyTeacher;
			
		}	
	}

	private boolean isTeacherValid(Teacher teacher) {
		return teacher != null && teacher.getFirstName() != null
				&& teacher.getLastName() != null;
	}

}

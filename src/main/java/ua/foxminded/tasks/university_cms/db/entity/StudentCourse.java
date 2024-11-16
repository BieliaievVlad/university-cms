package ua.foxminded.tasks.university_cms.db.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "students_courses", schema = "university")
public class StudentCourse {

    @EmbeddedId
    private StudentCourseId id;
    
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
    
    public StudentCourse() {
    	
    }
    
    public StudentCourse(Student student, Course course) {
    	this.id = new StudentCourseId(student.getId(), course.getId());
    	this.student = student;
    	this.course = course;
    }

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	} 
    
}

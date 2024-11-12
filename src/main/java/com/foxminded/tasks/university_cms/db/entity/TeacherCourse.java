package com.foxminded.tasks.university_cms.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers_courses", schema = "university")
public class TeacherCourse {
	
	@EmbeddedId
	private TeacherCourseId id;
	
	@ManyToOne
	@MapsId("teacherId")
	@Column(name = "teacher_id")
	private Teacher teacher;
	
    @ManyToOne
    @MapsId("courseId")
    @Column(name = "course_id")
    private Course course;
    
    public TeacherCourse() {
    	
    }
    
    public TeacherCourse(Teacher teacher, Course course) {
    	this.id = new TeacherCourseId(teacher.getId(), course.getId());
    	this.teacher = teacher;
    	this.course = course;
    }

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
    
    

}

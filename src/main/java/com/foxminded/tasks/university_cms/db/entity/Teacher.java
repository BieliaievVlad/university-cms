package com.foxminded.tasks.university_cms.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers", schema = "university")
public class Teacher extends Person {

	@ManyToOne
	@JoinColumn(name = "course_id", nullable = true)
	private Course course;
	
	public Teacher() {
		super();
	}
	
	public Teacher(Long id, String firstName, String lastName, Course course) {
		super(id, firstName, lastName);
		this.course = course;
	}
	
	public Teacher(String firstName, String lastName, Course course) {
		super(firstName, lastName);
		this.course = course;
	}
	
	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
}

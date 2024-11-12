package com.foxminded.tasks.university_cms.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups", schema = "university")
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "num_students")
	private Long numStudents;
	
	public Group() {
		
	}
	
	public Group(String name, Long numStudents) {
		this.name = name;
		this.numStudents = numStudents;
	}
	
	public Group(Long id, String name, Long numStudents) {
		this.id = id;
		this.name = name;
		this.numStudents = numStudents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumStudents() {
		return numStudents;
	}

	public void setNumStudents(Long numStudents) {
		this.numStudents = numStudents;
	}

}

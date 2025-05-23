package com.snipe.learning.model;

import java.sql.Timestamp;

public class CourseDTO {
    private Integer Id;
    private String title;
    private String description;
    private Integer instructorId;
    private String instructorName;
    private String status;      
    private Timestamp createdAt;
    
    
	public CourseDTO() {
	}
	
	public CourseDTO(String title, String description, Integer instructorId, String status) {
		this.title = title;
		this.description = description;
		this.instructorId = instructorId;
		this.status = status;
	}


	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getInstructorId() {
		return instructorId;
	}
	public void setInstructorId(Integer instructorId) {
		this.instructorId = instructorId;
	}
	public String getInstructorName() {
		return instructorName;
	}
	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
    
    
}


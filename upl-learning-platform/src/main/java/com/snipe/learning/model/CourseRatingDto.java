package com.snipe.learning.model;

import java.time.LocalDateTime;

public class CourseRatingDto {
	
	  private Integer id;
	  private Integer courseId;
	  private int rating;
	  private String comment;
	  private String name;
	  private LocalDateTime createdAt = LocalDateTime.now();
	  
	  
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CourseRatingDto() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	  
}

package com.snipe.learning.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CourseRating {

	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;
	  private Integer courseId;
	  private int rating;
	  private String comment;
	  private String name;
	  public String getName() {
		return name;
	}
	  
	  @OneToMany(mappedBy = "courseRating", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<CommentReply> replies = new ArrayList<>();
	  
	public void setName(String name) {
		this.name = name;
	}
	private LocalDateTime createdAt = LocalDateTime.now();
	public CourseRating() {
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

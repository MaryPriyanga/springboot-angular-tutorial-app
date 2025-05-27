package com.snipe.learning.model;

import java.sql.Timestamp;

public class TutorialDTO {
    private Integer Id;
    private Integer courseId;
    private String courseTitle;
    private String title;
    private String content;
    private String youtubeLink;
    private Timestamp createdAt;
    private String status; 
    
	public TutorialDTO() {
	}
	
	public TutorialDTO(Integer courseId, String title, String content, String youtubeLink,String status) {
		this.courseId = courseId;
		this.title = title;
		this.content = content;
		this.youtubeLink = youtubeLink;
		this.status = status;
	}
	
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getYoutubeLink() {
		return youtubeLink;
	}
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}


package com.snipe.learning.model;

public class UserWithCourseDTO {

	private Integer userId;
    private String userName;
    private String courseName;
    
    public UserWithCourseDTO(Integer userId, String userName, String courseTitle) {
        this.userId = userId;
        this.userName = userName;
        this.courseName = courseTitle;
    }
    
    public UserWithCourseDTO() {}
    
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
    
    
    
}

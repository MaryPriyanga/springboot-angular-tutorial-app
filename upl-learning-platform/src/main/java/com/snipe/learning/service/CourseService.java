package com.snipe.learning.service;


import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;
import com.snipe.learning.model.PageResponse;

public interface CourseService {
	
	public CourseDTO addCourse(CourseDTO courseDTO) throws UPLException; 
	public CourseDTO  updateCourse(Integer id,CourseDTO courseDTO) throws UPLException;
	public String  deleteCourse(Integer id) throws UPLException;
	PageResponse<CourseDTO> getPendingCourses(int page, int size) throws UPLException;
	public void updateCourseStatus(Integer id, Status status)throws UPLException;
	PageResponse<CourseDTO> getAllCourses(int page, int size) throws UPLException;
	PageResponse<CourseDTO> searchCourses(String query, int i, int size) throws UPLException;
	

}

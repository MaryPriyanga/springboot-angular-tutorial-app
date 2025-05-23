package com.snipe.learning.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;

public interface CourseService {
	
	public CourseDTO addCourse(CourseDTO courseDTO) throws UPLException; 
	public CourseDTO  updateCourse(Integer id,CourseDTO courseDTO) throws UPLException;
	public String  deleteCourse(Integer id) throws UPLException;
	Page<CourseDTO> getPendingCourses(int page, int size) throws UPLException;
	public void approveCourse(Integer id)throws UPLException;
	public void rejectCourse(Integer id) throws UPLException;
	Page<CourseDTO> getAllCourses(int page, int size) throws UPLException;
	

}

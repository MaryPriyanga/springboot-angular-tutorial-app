package com.snipe.learning.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.UserDTO;
import com.snipe.learning.model.UserWithCourseDTO;
import com.snipe.learning.projection.StatusCount;

@Service
public interface AdminService {

	public void registerInstructor(UserDTO userDTO) throws UPLException;
	public User login(String email, String password ) throws UPLException;
	public void approveInstructor(Integer id) throws UPLException;
	public void rejectInstructor(Integer id) throws UPLException;
	Page<UserDTO> getPendingInstructors(int page, int size) throws UPLException;
	Page<UserWithCourseDTO> getActiveUsersWithCourses(int page, int size) throws UPLException;
	public Map<String, Object> getAnalytics();
	public Map<String, Object> getEditActivity();
	public List<StatusCount> getInstructorStatusCounts() ;

}

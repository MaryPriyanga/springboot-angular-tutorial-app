package com.snipe.learning.service;

import org.springframework.stereotype.Service;

import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.UserDTO;

@Service
public interface AuthService {
	
	public void registerInstructor(UserDTO userDTO) throws UPLException;
	public User login(String email, String password ) throws UPLException;

}

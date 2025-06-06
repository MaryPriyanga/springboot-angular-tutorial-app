package com.snipe.learning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.User;
import com.snipe.learning.entity.User.Status;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.UserDTO;
import com.snipe.learning.repository.UserRepository;
import com.snipe.learning.utility.Mapper;

import jakarta.transaction.Transactional;


@Service
public class AuthServiceImpl implements AuthService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	@Transactional
	public void registerInstructor(UserDTO userDTO) throws UPLException {
		if (userRepository.findByEmail(userDTO.getEmail())
		        .filter(u -> u.getStatus() == Status.Active)
		        .isPresent()) {
		    throw new UPLException("User already registered and active");
		}
		if (userRepository.findByEmail(userDTO.getEmail())
				.filter(u -> u.getStatus() == Status.Pending)
		        .isPresent()) {
		    throw new UPLException("User already registered and awaiting for admin approval");
		}
		User user = mapper.toUserEntity(userDTO);
    	user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userRepository.save(user);
		
		emailService.sendSubmissionConfirmation(user.getEmail(), user.getName());
	}

	@Override
	public User login(String email, String password) throws UPLException {

		if (email == null || password == null) {
		    throw new UPLException("Email and password must be provided");
		}

	    User user = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UPLException("Invalid email or password"));

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new UPLException("Invalid email or password");
	    }

	    switch (user.getStatus()) {
	        case Pending -> throw new UPLException("Awaiting admin approval");
	        case Rejected -> throw new UPLException("Admin rejected your registration");
	        case Active -> {
	            return user;
	        }
	        default -> throw new UPLException("Invalid account status");
	    }
	}



}

package com.snipe.learning.utility;


import org.springframework.stereotype.Component;

import com.snipe.learning.entity.Course;
import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.entity.Tutorial;
import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;
import com.snipe.learning.model.TutorialDTO;
import com.snipe.learning.model.UserDTO;

@Component
public class Mapper {
	
	public CourseDTO toCourseDto(Course course) {
		CourseDTO dto = new CourseDTO();
	        dto.setId(course.getId());
	        dto.setTitle(course.getTitle());
	        dto.setDescription(course.getDescription());
	        dto.setInstructorId(course.getInstructor().getId());
	        dto.setInstructorName(course.getInstructor().getName());
	        dto.setStatus(course.getStatus().name());
	        dto.setCreatedAt(course.getCreatedAt());
	        return dto;
	    }
	
	public Course toCourseEntity(CourseDTO courseDto, User instructor) throws UPLException {
		Course course = new Course();
		course.setId(courseDto.getId());
		course.setTitle(courseDto.getTitle());
		course.setDescription(courseDto.getDescription());
		course.setInstructor(instructor);
		course.setStatus(Status.Pending);
		course.setCreatedAt(courseDto.getCreatedAt());
	        return course;
	    }
	
	 public Tutorial toTutorialEntity(TutorialDTO dto) {
	        if (dto == null) return null;

	        Tutorial tutorial = new Tutorial();
	        tutorial.setId(dto.getId());
	        tutorial.setTitle(dto.getTitle());
	        tutorial.setContent(dto.getContent());
	        tutorial.setYoutubeLink(dto.getYoutubeLink());
	        tutorial.setCreatedAt(dto.getCreatedAt());
	        tutorial.setStatus(Status.Pending);
	        if (dto.getCourseId() != null) {
	            Course course = new Course();
	            course.setId(dto.getCourseId());
	            tutorial.setCourse(course);
	        }

	        return tutorial;
	    }

	    public TutorialDTO toTutorialDTO(Tutorial tutorial) {
	        if (tutorial == null) return null;

	        TutorialDTO dto = new TutorialDTO();
	        dto.setId(tutorial.getId());
	        dto.setTitle(tutorial.getTitle());
	        dto.setContent(tutorial.getContent());
	        dto.setYoutubeLink(tutorial.getYoutubeLink());
	        dto.setCreatedAt(tutorial.getCreatedAt());
	        dto.setStatus(tutorial.getStatus().name());

	        if (tutorial.getCourse() != null) {
	            dto.setCourseId(tutorial.getCourse().getId());
	            dto.setCourseTitle(tutorial.getCourse().getTitle());
	        }

	        return dto;
	    }
	    
	    public UserDTO toUserDTO(User user) {
	    	
	    	if(user == null) return null;
	    	
	    	UserDTO userDTO =  new UserDTO();
	    	userDTO.setId(user.getId());
	    	userDTO.setName(user.getName());
	    	userDTO.setEmail(user.getEmail());
	    	userDTO.setRole(user.getRole().name());
	    	userDTO.setStatus(user.getStatus().name());
	    	userDTO.setCreatedAt(user.getCreatedAt());
	    	
	    	return userDTO;
	    }
	    
	    public User toUserEntity(UserDTO userDTO) {
	    	if(userDTO ==  null) return null;
	    	
	    	User user =  new User();
	    	user.setId(userDTO.getId());
	    	user.setName(userDTO.getName());
	    	user.setEmail(userDTO.getEmail());
	    	user.setRole(User.Role.Instructor);
	    	user.setStatus(User.Status.Pending);
	    	user.setCreatedAt(userDTO.getCreatedAt());
	    	
	    	return user;
	    	
	    }
}

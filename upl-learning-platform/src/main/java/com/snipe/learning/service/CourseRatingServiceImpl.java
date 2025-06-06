package com.snipe.learning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.Course;
import com.snipe.learning.entity.CourseRating;
import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseRatingDto;
import com.snipe.learning.repository.CourseRatingRepository;
import com.snipe.learning.repository.CourseRepository;
import com.snipe.learning.utility.Mapper;

@Service
public class CourseRatingServiceImpl implements CourseRatingService{
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private CourseRatingRepository courseRatingRepository;

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	EmailService emailService;
	
	@Override
	public CourseRatingDto addRating(CourseRatingDto courseRatingDto) throws UPLException {
		CourseRating courseRating = mapper.toCourseRatingEntity(courseRatingDto);
		courseRatingRepository.save(courseRating);
		
		Optional<Course> courseOpt = courseRepository.findById(courseRatingDto.getCourseId());

		if (courseOpt.isPresent()) {
		    Course course = courseOpt.get();
		    User instructor = course.getInstructor();
		    
		    emailService.sendCommentNotification(instructor.getEmail(), instructor.getName());
		} 
		
		return courseRatingDto;
		
	}

	@Override
	public List<CourseRatingDto> getCommentsForCourse(Integer courseId) throws UPLException {
		
		   List<CourseRating> ratingsWithComments = courseRatingRepository.findByCourseIdAndCommentIsNotNullOrderByCreatedAtDesc(courseId);

		   return ratingsWithComments.stream()
		            .map(mapper::toCourseRatingDto)
		            .toList();
	}

	@Override
	public void deleteCommentById(Integer id) throws UPLException {
		courseRatingRepository.deleteById(id);
		
	}

}

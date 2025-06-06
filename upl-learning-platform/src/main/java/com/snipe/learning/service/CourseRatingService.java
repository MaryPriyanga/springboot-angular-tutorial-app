package com.snipe.learning.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseRatingDto;

@Service
public interface CourseRatingService {

	public CourseRatingDto addRating(CourseRatingDto courseRatingDto) throws UPLException;

	public List<CourseRatingDto> getCommentsForCourse(Integer courseId) throws UPLException;

	public void deleteCommentById(Integer id) throws UPLException;
	
}

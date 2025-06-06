package com.snipe.learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.learning.AOP.HandlerService;
import com.snipe.learning.model.CourseRatingDto;
import com.snipe.learning.service.CourseRatingService;

@RestController
@RequestMapping("/api/ratings")
public class CourseRatingController {

	@Autowired
	private CourseRatingService courseRatingService;
	
	@Autowired
    private HandlerService handlerService;
	
	@PostMapping("/addRating")
	 public ResponseEntity<?> addRating(@RequestBody CourseRatingDto courseRatingDto){

		        return handlerService.handleServiceCall(
		            () -> courseRatingService.addRating(courseRatingDto),
		            "INFO.RATING_INSERT_SUCCESS"
		        );
	} 
	
	
	 @GetMapping("/course/{courseId}")
	    public ResponseEntity<?> getCommentsForCourse(@PathVariable Integer courseId) {
	     
		 return handlerService.handleServiceCall(
		            () -> courseRatingService.getCommentsForCourse(courseId),
		            "INFO.RATING_FETCH_SUCCESS"
		        );
	 }
	 
	 @DeleteMapping("/comments/{id}")
	 public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
		 courseRatingService.deleteCommentById(id);
	     return ResponseEntity.ok("Comment deleted successfully");
	 }
}

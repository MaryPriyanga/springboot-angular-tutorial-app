package com.snipe.learning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.CourseRating;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating , Integer>{
	
	 List<CourseRating> findByCourseId(Integer courseId);
	 
	  @Query("SELECT AVG(r.rating) FROM CourseRating r WHERE r.courseId = :courseId")
	  Double findAverageRatingByCourseId(@Param("courseId") Integer courseId);

	  List<CourseRating> findByCourseIdAndCommentIsNotNullOrderByCreatedAtDesc(Integer courseId);
}

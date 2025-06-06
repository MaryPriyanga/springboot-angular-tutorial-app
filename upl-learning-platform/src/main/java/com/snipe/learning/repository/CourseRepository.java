package com.snipe.learning.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.Course;
import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.entity.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

	Optional<Course> findByTitleIgnoreCase(String title);
	Page<Course> findByStatusIn(List<Status> statuses, Pageable pageable);
	Page<Course> findByStatus(Status active, Pageable pageable);
	Page<Course> findByInstructorAndStatusIn(User currentUser, List<Status> statuses, Pageable pageable);
	Page<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
			Pageable pageable);
	
	 @Query("SELECT c.title AS courseTitle, SUM(c.views) AS totalViews " +
	           "FROM Course c GROUP BY c.title ORDER BY totalViews DESC")
	List<Map<String, Object>> findTrendingCourses(Pageable topTen);

}

package com.snipe.learning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.User;
import com.snipe.learning.entity.User.Status;
import com.snipe.learning.model.UserWithCourseDTO;
import com.snipe.learning.projection.StatusCount;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByEmail(String email);
	
	Page<User> findByStatus(Status status, Pageable pageable);
	
	@Query(value = "SELECT u.user_id as userId, u.name as userName, c.title as courseName " +
            "FROM user u " +
            "JOIN course c ON u.user_id = c.instructor_id " +
            "WHERE u.status = 'Active'", nativeQuery = true)
	
	Page<UserWithCourseDTO> findActiveUsersWithCourses(Pageable pageable);

	@Query("SELECT u.status AS status, COUNT(u) AS count FROM User u WHERE u.role = 'Instructor' GROUP BY u.status")
	List<StatusCount> countByRoleAndStatusGrouped();

	
}

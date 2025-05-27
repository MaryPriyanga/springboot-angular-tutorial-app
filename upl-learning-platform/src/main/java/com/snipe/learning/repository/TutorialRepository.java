package com.snipe.learning.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.entity.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Integer>{


	Page<Tutorial> findByStatus(Status status, Pageable pageable);
	Optional<Tutorial> findByIdAndStatus(Integer id, Status pending);
	List<Tutorial> findByCourseId(Integer id);
	Page<Tutorial> findByCourseIdAndStatusIn(int courseId, List<Status> statuses, Pageable pageable);
	Page<Tutorial> findByCourseIdAndStatus(int courseId, Status active, Pageable pageable);

}

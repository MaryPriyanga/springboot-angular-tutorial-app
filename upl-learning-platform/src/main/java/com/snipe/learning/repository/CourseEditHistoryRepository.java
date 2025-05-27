package com.snipe.learning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.CourseEditHistory;
import com.snipe.learning.projection.EditCountByDate;

@Repository
public interface CourseEditHistoryRepository extends JpaRepository<CourseEditHistory, Integer> {

	@Query("SELECT DATE(e.modifiedAt) AS date, COUNT(e) AS count FROM CourseEditHistory e GROUP BY DATE(e.modifiedAt)")
	List<EditCountByDate> countEditsGroupedByDate();

}

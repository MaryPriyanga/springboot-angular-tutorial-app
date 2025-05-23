package com.snipe.learning.repository;

import com.snipe.learning.entity.InstructorApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorApprovalLogRepository extends JpaRepository<InstructorApprovalLog, Integer> {
}

package com.snipe.learning.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.InstructorApprovalLog;
import com.snipe.learning.entity.User;
import com.snipe.learning.entity.User.Status;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.UserDTO;
import com.snipe.learning.model.UserWithCourseDTO;
import com.snipe.learning.projection.EditCountByDate;
import com.snipe.learning.projection.StatusCount;
import com.snipe.learning.repository.CourseEditHistoryRepository;
import com.snipe.learning.repository.CourseRepository;
import com.snipe.learning.repository.InstructorApprovalLogRepository;
import com.snipe.learning.repository.TutorialEditHistoryRepository;
import com.snipe.learning.repository.TutorialRepository;
import com.snipe.learning.repository.UserRepository;
import com.snipe.learning.utility.Mapper;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private  TutorialRepository tutorialRepository;
	
	@Autowired
	private TutorialEditHistoryRepository tutorialEditHistoryRepository;
	
	@Autowired
	private CourseEditHistoryRepository courseEditHistoryRepository;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private EmailService emailService;	

	@Autowired
    private InstructorApprovalLogRepository logRepository;
		
	
	@Override
    @Transactional
    public void manageInstructor(Integer id, Status status) throws UPLException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UPLException("User Not found"));
        
        // Admin Approval logic
        user.setStatus(status);
        userRepository.save(user);
        
        // Get logged-in Admin's information
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UPLException("Admin Not found"));

        // Create the log entry for instructor approval
        InstructorApprovalLog log = new InstructorApprovalLog();
        log.setUser(user);
        log.setAdmin(admin);
        log.setRemarks("Instructor " +status);
        if(status.equals(User.Status.Active)) {
        	log.setStatus(User.Status.Approved);
        }else {
        log.setStatus(status);}
        logRepository.save(log);
        
        if(status.equals(User.Status.Active)) {
        emailService.sendApprovalNotification(user.getEmail(), user.getName());
        }
        if(status.equals(User.Status.Rejected)){
        	emailService.sendRejectNotification(user.getEmail(), user.getName());
        }
    }

    @Override
    public Page<UserDTO> getPendingInstructors(int page, int size) throws UPLException {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pendingUsersPage = userRepository.findByStatus(Status.Pending, pageable);

        if (pendingUsersPage.isEmpty()) {
            throw new UPLException("No pending instructors found");
        }

        return pendingUsersPage.map(mapper::toUserDTO); 
    }

	
    @Override
    public Page<UserWithCourseDTO> getActiveUsersWithCourses(int page, int size) throws UPLException {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserWithCourseDTO> activeUsersPage = userRepository.findActiveUsersWithCourses(pageable);

        if (activeUsersPage.isEmpty()) {
            throw new UPLException("No active instructors found");
        }

        return activeUsersPage;
    }

	@Override
	public Map<String, Object> getAnalytics() {
		
		Map<String, Object> stats = new HashMap<>();
        stats.put("totalInstructors", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalTutorials", tutorialRepository.count());
        return stats;
	}

	@Override
	public Map<String, Object> getEditActivity() {
		List<EditCountByDate> courseData = courseEditHistoryRepository.countEditsGroupedByDate();
        List<EditCountByDate> tutorialData = tutorialEditHistoryRepository.countEditsGroupedByDate();

        Map<String, Object> result = new HashMap<>();
        result.put("courseEdits", formatResult(courseData));
        result.put("tutorialEdits", formatResult(tutorialData));
        return result;
	}

	@Override
	public List<StatusCount> getInstructorStatusCounts() {
		return userRepository.countByRoleAndStatusGrouped();
	}
	
	private List<Map<String, Object>> formatResult(List<EditCountByDate> courseData) {
	    return courseData.stream().map(obj -> {
	        Map<String, Object> map = new HashMap<>();
	        map.put("date", obj.getDate());
	        map.put("count", obj.getCount());
	        return map;
	    }).collect(Collectors.toList());
	}

}


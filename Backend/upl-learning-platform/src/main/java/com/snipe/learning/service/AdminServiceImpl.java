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
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
    private InstructorApprovalLogRepository logRepository;
		
	@Override
	public void registerInstructor(UserDTO userDTO) throws UPLException {
		if (userRepository.findByEmail(userDTO.getEmail())
		        .filter(u -> u.getStatus() == Status.Active)
		        .isPresent()) {
		    throw new UPLException("User already registered and active");
		}
		if (userRepository.findByEmail(userDTO.getEmail())
				.filter(u -> u.getStatus() == Status.Pending)
		        .isPresent()) {
		    throw new UPLException("User already registered and awaiting for admin approval");
		}
		User user = mapper.toUserEntity(userDTO);
    	user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userRepository.save(user);
	}

	@Override
	public User login(String email, String password) throws UPLException {

		if (email == null || password == null) {
		    throw new UPLException("Email and password must be provided");
		}

	    User user = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UPLException("Invalid email or password"));

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new UPLException("Invalid email or password");
	    }

	    switch (user.getStatus()) {
	        case Pending -> throw new UPLException("Awaiting admin approval");
	        case Rejected -> throw new UPLException("Admin rejected your registration");
	        case Active -> {
	            return user;
	        }
	        default -> throw new UPLException("Invalid account status");
	    }
	}


	@Override
    @Transactional
    public void approveInstructor(Integer id) throws UPLException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UPLException("User Not found"));
        
        // Admin Approval logic
        user.setStatus(User.Status.Active);
        userRepository.save(user);
        
        // Get logged-in Admin's information
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UPLException("Admin Not found"));

        // Create the log entry for instructor approval
        InstructorApprovalLog log = new InstructorApprovalLog();
        log.setUser(user);
        log.setAdmin(admin);
        log.setRemarks("Instructor approved");
        log.setStatus(InstructorApprovalLog.Status.Approved);
        logRepository.save(log);
    }

    @Override
    @Transactional
    public void rejectInstructor(Integer id) throws UPLException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UPLException("User Not found"));

        // Admin Reject logic
        user.setStatus(User.Status.Rejected);
        userRepository.save(user);

        // Get logged-in Admin's information
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UPLException("Admin Not found"));

        // Create the log entry for instructor rejection
        InstructorApprovalLog log = new InstructorApprovalLog();
        log.setUser(user);
        log.setAdmin(admin);
        log.setRemarks("Instructor rejected");
        log.setStatus(InstructorApprovalLog.Status.Rejected);
        logRepository.save(log);
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
        //stats.put("totalCourseEdits", courseEditHistoryRepository.count());
        //stats.put("totalTutorislEdits", tutorialEditHistoryRepository.count());
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


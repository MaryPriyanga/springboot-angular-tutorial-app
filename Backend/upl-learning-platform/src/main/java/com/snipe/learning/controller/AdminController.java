package com.snipe.learning.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.learning.AOP.HandlerService;
import com.snipe.learning.service.AdminService;
import com.snipe.learning.service.CourseService;
import com.snipe.learning.service.TutorialService;

@RestController
@RequestMapping("/api/admin/")
@PreAuthorize("hasRole('Admin')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TutorialService tutorialService;

    @Autowired
    private HandlerService handlerService;

    @GetMapping("/pending-instructors")
    public ResponseEntity<?> pendingInstructors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return handlerService.handleServiceCall(() -> adminService.getPendingInstructors(page-1, size),
                "INFO.PENDING_INSTRUCTORS_FETCHED");
    }

    @GetMapping("/review-courses")
    public ResponseEntity<?> reviewCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return handlerService.handleServiceCall(() -> courseService.getPendingCourses(page-1, size),
                "INFO.PENDING_COURSES_FETCHED");
    }

    @GetMapping("/review-tutorials")
    public ResponseEntity<?> reviewTutorials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return handlerService.handleServiceCall(() -> tutorialService.getPendingTutorials(page-1, size),
                "INFO.PENDING_TUTORIALS_FETCHED");
    }

    @PostMapping("/approve-instructor/{id}")
    public ResponseEntity<?> approve(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            adminService.approveInstructor(id);
            return null;
        }, "INFO.INSTRUCTOR_APPROVED");
    }

    @PostMapping("/reject-instructor/{id}")
    public ResponseEntity<?> reject(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            adminService.rejectInstructor(id);
            return null;
        }, "INFO.INSTRUCTOR_REJECTED");
    }

    @PostMapping("/approve-course/{id}")
    public ResponseEntity<?> approveCourse(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            courseService.approveCourse(id);
            return null;
        }, "INFO.COURSE_APPROVED");
    }

    @PostMapping("/reject-course/{id}")
    public ResponseEntity<?> rejectCourse(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            courseService.rejectCourse(id);
            return null;
        }, "INFO.COURSE_REJECTED");
    }

    @PostMapping("/approve-tutorial/{id}")
    public ResponseEntity<?> approveTutorial(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            tutorialService.approveTutorial(id);
            return null;
        }, "INFO.TUTORIAL_APPROVED");
    }

    @PostMapping("/reject-tutorial/{id}")
    public ResponseEntity<?> rejectTutorial(@PathVariable Integer id) {
        return handlerService.handleServiceCall(() -> {
            tutorialService.rejectTutorial(id);
            return null;
        }, "INFO.TUTORIAL_REJECTED");
    }

    @GetMapping("/active-instructors")
    public ResponseEntity<?> getActiveUsersWithCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return handlerService.handleServiceCall(() -> adminService.getActiveUsersWithCourses(page-1, size),
                "INFO.ACTIVE_INSTRUCTORS_FETCHED");
    }
    
    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {
        Map<String, Object> stats = adminService.getAnalytics();
        return stats;
    }
    
    @GetMapping("/edit-activity")
    public ResponseEntity<?> getEditActivity() {
        return ResponseEntity.ok(adminService.getEditActivity());
    }

    @GetMapping("/instructor-status-counts")
    public ResponseEntity<?> getInstructorStatusCounts() {
        return ResponseEntity.ok(adminService.getInstructorStatusCounts());
    }
    
}

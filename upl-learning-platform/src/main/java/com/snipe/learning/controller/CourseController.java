package com.snipe.learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.snipe.learning.AOP.HandlerService;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;
import com.snipe.learning.service.CourseService;

@RestController
@RequestMapping("/api/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private HandlerService handlerService;

    @GetMapping("fetchAllCourses")
    public ResponseEntity<?> fetchAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return handlerService.handleServiceCall(
                () -> courseService.getAllCourses(page-1, size),
                "INFO.COURSE_FETCH_SUCCESS"
        );
    }

    @PostMapping("/createCourse")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
        return handlerService.handleServiceCall(
                () -> courseService.addCourse(courseDTO),
                "INFO.COURSE_INSERT_SUCCESS"
        );
    }

    @PutMapping("/updateCourse/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @RequestBody CourseDTO courseDTO) {
        return handlerService.handleServiceCall(
                () -> courseService.updateCourse(id, courseDTO),
                "INFO.UPDATE_SUCCESS"
        );
    }

    @DeleteMapping("/deleteCourse/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        return handlerService.handleServiceCall(
                () -> {
                    String result = courseService.deleteCourse(id);
                    if (!"success".equalsIgnoreCase(result)) {
                        throw new UPLException("Delete failed");
                    }
                    return null;
                },
                "INFO.DELETE_SUCCESS"
        );
    }
}

package com.snipe.learning.controller;

import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipe.learning.AOP.HandlerService;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;
import com.snipe.learning.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private HandlerService handlerService;
    
    @Autowired
    private final ObjectMapper objectMapper;

    public CourseController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
    public ResponseEntity<?> createCourse(
        @RequestPart("course") String courseJson,
        @RequestPart(value = "descriptionFile", required = false) MultipartFile descriptionFile
    ) throws IOException, TikaException {

        CourseDTO courseDTO = objectMapper.readValue(courseJson, CourseDTO.class);

        if (descriptionFile != null && !descriptionFile.isEmpty()) {
            Tika tika = new Tika();
            String extractedText = tika.parseToString(descriptionFile.getInputStream());
            courseDTO.setDescription(extractedText);
        }

        return handlerService.handleServiceCall(
            () -> courseService.addCourse(courseDTO),
            "INFO.COURSE_INSERT_SUCCESS"
        );
    }

    @PutMapping("/updateCourse/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> updateCourse(
    		@PathVariable Integer id,
            @RequestPart("course") String courseJson,
            @RequestPart(value = "descriptionFile", required = false) MultipartFile descriptionFile
        ) throws IOException, TikaException {

            CourseDTO courseDTO = objectMapper.readValue(courseJson, CourseDTO.class);

            if (descriptionFile != null && !descriptionFile.isEmpty()) {
                Tika tika = new Tika();
                String extractedText = tika.parseToString(descriptionFile.getInputStream());
                courseDTO.setDescription(extractedText);
            }
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
    
    @GetMapping("/search")
    public ResponseEntity<?> searchCourses(
        @RequestParam String query,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "8") int size
    ) {
    	
    	 return handlerService.handleServiceCall(
                 () -> courseService.searchCourses(query,page-1, size),
                 "INFO.COURSE_FETCH_SUCCESS"
         );
    }
    
    @PutMapping("/courses/{id}/views")
    public ResponseEntity<?> incrementCourseViews(
            @PathVariable Integer id,
            HttpServletRequest request) {
    	
        return handlerService.handleServiceCall(() -> {
            courseService.incrementCourseViews(id, request);
            return null;
        }, "INFO.COURSE_VIEWS");
        
     }

}



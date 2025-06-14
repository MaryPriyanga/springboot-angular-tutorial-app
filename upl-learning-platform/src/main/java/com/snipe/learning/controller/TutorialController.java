package com.snipe.learning.controller;

import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.snipe.learning.model.TutorialDTO;
import com.snipe.learning.service.TutorialService;

@RestController
@RequestMapping("/api/tutorial/")
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @Autowired
    private HandlerService handlerService;
    
    @Autowired
    private final ObjectMapper objectMapper;

    public TutorialController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/fetchAllTutorials")
    public ResponseEntity<?> fetchAllTutorials() {
        return handlerService.handleServiceCall(
                () -> tutorialService.getAllTutorials(),
                "INFO.TUTORIALS_FETCH_SUCCESS"
        );
    }

    @GetMapping("/fetchAllTutorialsByCourseId/{courseId}")
    public ResponseEntity<?> fetchAllTutorialsByCourseId(
            @PathVariable Integer courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	System.out.println(courseId);
        Pageable pageable = PageRequest.of(page-1, size);
        return handlerService.handleServiceCall(
                () -> tutorialService.getAllTutorialsByCourseId(courseId, pageable),
                "INFO.TUTORIALS_FETCH_SUCCESS"
        );
    }

    @GetMapping("/fetchTutorialById/{id}")
    public ResponseEntity<?> fetchTutorialById(@PathVariable Integer id) {
        return handlerService.handleServiceCall(
                () -> tutorialService.getTutorial(id),
                "INFO.TUTORIAL_FETCH_SUCCESS"
        );
    }

    @GetMapping("/fetchPendingTutorialById/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> fetchPendingTutorialById(@PathVariable Integer id) {
        return handlerService.handleServiceCall(
                () -> tutorialService.getPendingTutorial(id),
                "INFO.TUTORIALS_FETCH_SUCCESS"
        );
    }

    @PostMapping("/createTutorial")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> createTutorial(
        @RequestPart("tutorial") String tutorialJson,
        @RequestPart(value = "descriptionFile", required = false) MultipartFile descriptionFile
    ) throws IOException, TikaException {

        TutorialDTO tutorialDTO = objectMapper.readValue(tutorialJson, TutorialDTO.class);

        if (descriptionFile != null && !descriptionFile.isEmpty()) {
            Tika tika = new Tika();
            String extractedText = tika.parseToString(descriptionFile.getInputStream());
            tutorialDTO.setContent(extractedText); // or tutorialDTO.setDescription(...)
        }

        return handlerService.handleServiceCall(
            () -> {
                tutorialService.addTutorial(tutorialDTO);
                return null;
            },
            "INFO.TUTORIAL_INSERT_SUCCESS"
        );
    }

    @PutMapping("/updateTutorial/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> updateTutorial(
        @PathVariable Integer id,
        @RequestPart("tutorial") String tutorialJson,
        @RequestPart(value = "descriptionFile", required = false) MultipartFile descriptionFile
    ) throws IOException, TikaException {

        TutorialDTO tutorialDTO = objectMapper.readValue(tutorialJson, TutorialDTO.class);

        if (descriptionFile != null && !descriptionFile.isEmpty()) {
            Tika tika = new Tika();
            String extractedText = tika.parseToString(descriptionFile.getInputStream());
            tutorialDTO.setContent(extractedText); // or tutorialDTO.setDescription(...)
        }

        return handlerService.handleServiceCall(
            () -> {
                String result = tutorialService.updateTutorial(id, tutorialDTO);
                if (!"success".equalsIgnoreCase(result)) {
                    throw new UPLException("Tutorial update failed");
                }
                return null;
            },
            "INFO.TUTORIAL_UPDATE_SUCCESS"
        );
    }


    @DeleteMapping("/deleteTutorial/{id}")
    @PreAuthorize("hasAnyRole('Admin','Instructor')")
    public ResponseEntity<?> deleteTutorial(@PathVariable Integer id) {
        return handlerService.handleServiceCall(
                () -> {
                    String result = tutorialService.deleteTutorial(id);
                    if (!"success".equalsIgnoreCase(result)) {
                        throw new UPLException("Tutorial delete failed");
                    }
                    return null;
                },
                "INFO.TUTORIAL_DELETE_SUCCESS"
        );
    }
}

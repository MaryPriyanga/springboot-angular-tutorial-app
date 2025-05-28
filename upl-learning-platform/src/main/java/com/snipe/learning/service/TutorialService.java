package com.snipe.learning.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.PageResponse;
import com.snipe.learning.model.TutorialDTO;

@Service
public interface TutorialService {

	public TutorialDTO getTutorial(Integer TutorialId) throws UPLException;
	public void addTutorial(TutorialDTO tutorialDTO) throws UPLException; 
	public String  deleteTutorial(Integer id) throws UPLException;
	public String updateTutorial(Integer id, TutorialDTO tutorialDTO) throws UPLException;
	PageResponse<TutorialDTO> getPendingTutorials(int page, int size) throws UPLException;
	public TutorialDTO getPendingTutorial(Integer id) throws UPLException;
	List<TutorialDTO> getAllTutorialsByCourseId(int courseId, Pageable pageable) throws UPLException;
	void updateTutorialStatus(Integer id, Status status) throws UPLException;
}

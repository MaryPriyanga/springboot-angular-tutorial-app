package com.snipe.learning.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.Course;
import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.entity.Tutorial;
import com.snipe.learning.entity.TutorialEditHistory;
import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.TutorialDTO;
import com.snipe.learning.repository.CourseRepository;
import com.snipe.learning.repository.TutorialEditHistoryRepository;
import com.snipe.learning.repository.TutorialRepository;
import com.snipe.learning.repository.UserRepository;
import com.snipe.learning.utility.Mapper;

import jakarta.transaction.Transactional;

@Service
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TutorialEditHistoryRepository tutorialEditHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public List<TutorialDTO> getAllTutorialsByCourseId(int courseId, Pageable pageable) throws UPLException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new UPLException("Course not found"));

        User loggedInUser = getCurrentUserOrNull();
        Page<Tutorial> tutorialPage;

        if (loggedInUser == null) {
            tutorialPage = tutorialRepository.findByCourseIdAndStatus(courseId, Status.Active, pageable);
        } else if (isAdmin(loggedInUser)) {
        	List<Status> statuses = Arrays.asList(Status.Pending, Status.Rejected, Status.Active);
            tutorialPage = tutorialRepository.findByCourseIdAndStatusIn(courseId, statuses,pageable);
        } else if (isInstructor(loggedInUser) && isInstructorOwner(loggedInUser, course)) {
        	List<Status> statuses = Arrays.asList(Status.Pending, Status.Rejected, Status.Active);
            tutorialPage = tutorialRepository.findByCourseIdAndStatusIn(courseId,statuses, pageable);
        } else {
            throw new UPLException("Unauthorized access to course tutorials");
        }

        if (tutorialPage.isEmpty()) {
            throw new UPLException("No tutorials found for this course");
        }

        return tutorialPage.stream()
                .map(mapper::toTutorialDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void addTutorial(TutorialDTO tutorialDTO) throws UPLException {
        Course course = courseRepository.findById(tutorialDTO.getCourseId())
                .orElseThrow(() -> new UPLException("Course does not exist"));

        if (course.getStatus() != Status.Active) {
            throw new UPLException("Cannot add tutorials under an inactive course");
        }

        User loggedInUser = getLoggedInUser();

        if (!(isAdmin(loggedInUser) || isInstructorOwner(loggedInUser, course))) {
            throw new UPLException("Unauthorized to add tutorial to this course");
        }

        if (loggedInUser.getStatus() != User.Status.Active) {
            throw new UPLException("User is not active");
        }
        if(!isValidYouTubeUrl(tutorialDTO.getYoutubeLink())) {
        	throw new UPLException("Not Valid youtube URL");
        }
        Tutorial tutorial = mapper.toTutorialEntity(tutorialDTO);
        tutorialRepository.save(tutorial);
    }

    public boolean isValidYouTubeUrl(String url) {
        String regex = "^(https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$";
        return url != null && url.matches(regex);
    }
    
    @Override
    @Transactional
    public String updateTutorial(Integer id, TutorialDTO tutorialDTO) throws UPLException {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new UPLException("Tutorial not found"));

        Course course = courseRepository.findById(tutorialDTO.getCourseId())
                .orElseThrow(() -> new UPLException("Course not found"));

        User loggedInUser = getLoggedInUser();

        if (!(isAdmin(loggedInUser) || isInstructorOwner(loggedInUser, course))) {
            throw new UPLException("Unauthorized to update this tutorial");
        }

        if(!isValidYouTubeUrl(tutorialDTO.getYoutubeLink())) {
        	throw new UPLException("Not Valid youtube URL");
        }
        
        // Save old data for logging
        String oldTitle = tutorial.getTitle();
        String oldContent = tutorial.getContent();

        tutorial.setTitle(tutorialDTO.getTitle());
        tutorial.setContent(tutorialDTO.getContent());
        if(tutorialDTO.getYoutubeLink() != null) {
        tutorial.setYoutubeLink(tutorialDTO.getYoutubeLink());
        }
        tutorialRepository.save(tutorial);

        String changes = String.format("Title: '%s' → '%s', Content: '%s' → '%s'",
                oldTitle, tutorialDTO.getTitle(), oldContent, tutorialDTO.getContent());

        TutorialEditHistory history = new TutorialEditHistory();
        history.setTutorial(tutorial);
        history.setInstructor(loggedInUser);
        history.setChanges(changes);
        tutorialEditHistoryRepository.save(history);

        return "success";
    }

    @Override
    @Transactional
    public String deleteTutorial(Integer id) throws UPLException {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new UPLException("Tutorial not found"));

        Course course = tutorial.getCourse();

        User loggedInUser = getLoggedInUser();

        if (!(isAdmin(loggedInUser) || isInstructorOwner(loggedInUser, course))) {
            throw new UPLException("Unauthorized to delete this tutorial");
        }

        tutorial.setStatus(Status.Inactive);
        tutorialRepository.save(tutorial);

        String changes = "Tutorial marked as Inactive (deleted)";
        TutorialEditHistory history = new TutorialEditHistory();
        history.setTutorial(tutorial);
        history.setInstructor(loggedInUser);
        history.setChanges(changes);
        tutorialEditHistoryRepository.save(history);

        return "success";
    }

    @Override
    public TutorialDTO getTutorial(Integer tutorialId) throws UPLException {
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(() -> new UPLException("Tutorial not found"));

        User loggedInUser = getCurrentUserOrNull();

        if (isInstructor(loggedInUser) || loggedInUser == null) {
            if (tutorial.getStatus() != Status.Active) {
                throw new UPLException("Tutorial is not in active status");
            }
            return mapper.toTutorialDTO(tutorial);
        }
        if (isAdmin(loggedInUser)) {
            return mapper.toTutorialDTO(tutorial);
        }

        throw new UPLException("Unauthorized access to tutorial");
    }



    private User getLoggedInUser() throws UPLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UPLException("Logged-in user not found"));
    }

    private User getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    private boolean isAdmin(User user) {
        return user != null && "Admin".equalsIgnoreCase(user.getRole().name());
    }

    private boolean isInstructor(User user) {
        return user != null && "Instructor".equalsIgnoreCase(user.getRole().name());
    }

    private boolean isInstructorOwner(User user, Course course) {
        return isInstructor(user)
                && user.getStatus() == User.Status.Active
                && course.getInstructor() != null
                && user.getId().equals(course.getInstructor().getId());
    }

	
	@Override
	public void approveTutorial(Integer id) throws UPLException {

        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new UPLException("Tutorial Not found"));
        
        tutorial.setStatus(Status.Active);
        tutorialRepository.save(tutorial);
	}

	@Override
	public void rejectTutorial(Integer id) throws UPLException {

        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new UPLException("Tutorial Not found"));
        
        // Admin Approval logic
        tutorial.setStatus(Status.Rejected);
        tutorialRepository.save(tutorial);
	}

	@Override
	public TutorialDTO getPendingTutorial(Integer id) throws UPLException {
	    Tutorial tutorial = tutorialRepository.findByIdAndStatus(id, Status.Pending)
	            .orElseThrow(() -> new UPLException("Tutorial not found"));

	    User loggedInUser = getCurrentUserOrNull();

	    if (loggedInUser == null) {
	        throw new UPLException("Unauthorized access");
	    }

	    if (isAdmin(loggedInUser)) {
	        return mapper.toTutorialDTO(tutorial);
	    }

	    if (isInstructor(loggedInUser)) {
	        Course course = tutorial.getCourse();
	        if (course != null && course.getInstructor().getId().equals(loggedInUser.getId())) {
	            return mapper.toTutorialDTO(tutorial);
	        }
	    }

	    throw new UPLException("Access denied: You are not authorized to view this tutorial");
	}


	@Override
	public Page<TutorialDTO> getPendingTutorials(int page, int size) throws UPLException {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Tutorial> pendingTutorialssObj = tutorialRepository.findByStatus(Status.Pending, pageable);
			
			if(pendingTutorialssObj.isEmpty()) {
				throw new UPLException("No pending Tutorials found");
			}
			
			return pendingTutorialssObj.map(mapper::toTutorialDTO);
	}

}

package com.snipe.learning.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.Course;
import com.snipe.learning.entity.Course.Status;
import com.snipe.learning.entity.CourseEditHistory;
import com.snipe.learning.entity.Tutorial;
import com.snipe.learning.entity.User;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.CourseDTO;
import com.snipe.learning.repository.CourseEditHistoryRepository;
import com.snipe.learning.repository.CourseRepository;
import com.snipe.learning.repository.TutorialRepository;
import com.snipe.learning.repository.UserRepository;
import com.snipe.learning.utility.Mapper;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseEditHistoryRepository courseEditHistoryRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private Mapper mapper;

    @Override
   // @Cacheable(value = "allCourses", key = "'page_' + #page + '_size_' + #size + '_user_' + (#root.target.getCurrentUserOrNull()?.id ?: 'anonymous')")
    public Page<CourseDTO> getAllCourses(int page, int size) throws UPLException {
        User currentUser = getCurrentUserOrNull();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> courses;

        if (currentUser == null) {
            // General public: only active courses
            courses = courseRepository.findByStatus(Status.Active, pageable);
        } else if (isAdmin(currentUser)) {
        	List<Status> statuses = Arrays.asList(Status.Pending, Status.Rejected, Status.Active);
            courses = courseRepository.findByStatusIn(statuses, pageable);
        } else if (isInstructor(currentUser)) {
        	List<Status> statuses = Arrays.asList(Status.Pending, Status.Rejected, Status.Active);
            courses = courseRepository.findByInstructorAndStatusIn(currentUser, statuses, pageable);
        } else {
            throw new UPLException("Unauthorized access to courses");
        }

        if (courses.isEmpty()) {
            throw new UPLException("No courses found");
        }

        return courses.map(mapper::toCourseDto); // maps Course -> CourseDTO
    }

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) throws UPLException {
        if (courseRepository.findByTitleIgnoreCase(courseDTO.getTitle()).isPresent()) {
            throw new UPLException("Course with this title already exists");
        }

        User instructor = userRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new UPLException("Instructor not found"));

        if (!isInstructor(instructor) || instructor.getStatus() != User.Status.Active) {
            throw new UPLException("Only active instructors can be assigned");
        }

        Course course = mapper.toCourseEntity(courseDTO, instructor);
        courseRepository.save(course);
        CourseDTO courseDto = mapper.toCourseDto(course);
        return courseDto;
    }

    @Override
    @Transactional
   // @CachePut(value="courses" , key="#id")
    public CourseDTO updateCourse(Integer id, CourseDTO courseDTO) throws UPLException {
        Course course = findCourseById(id);
       
        User loggedInUser = getLoggedInUser();

        if (!(isAdmin(loggedInUser) || isInstructorOwner(loggedInUser, course))) {
            throw new UPLException("Unauthorized to update this course");
        }

        String oldTitle = course.getTitle();
        String oldDescription = course.getDescription();
        Status oldStatus = course.getStatus();

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setInstructor(loggedInUser);
        courseRepository.save(course);

        String changes = String.format(
            "Title: '%s' → '%s', Description: '%s' → '%s', Status: '%s' → '%s'",
            oldTitle, course.getTitle(),
            oldDescription, course.getDescription(),
            oldStatus, course.getStatus()
        );

        logCourseEdit(course, loggedInUser, changes);
        CourseDTO courseDto = mapper.toCourseDto(course);
        return courseDto;
    }

    @Override
    //@CacheEvict(value="courses" ,key="#id")
    @Transactional
    public String deleteCourse(Integer id) throws UPLException {
        Course course = findCourseById(id);
        User loggedInUser = getLoggedInUser();

        if (!(isAdmin(loggedInUser) || isInstructorOwner(loggedInUser, course))) {
            throw new UPLException("Unauthorized to delete this course");
        }

        course.setStatus(Status.Inactive);
        courseRepository.save(course);

        List<Tutorial> tutorials = tutorialRepository.findByCourseId(course.getId());
        tutorials.forEach(tutorial -> tutorial.setStatus(Status.Inactive));
        tutorialRepository.saveAll(tutorials);

        logCourseEdit(course, loggedInUser, "Course marked as Inactive (soft-deleted)");
        return "success";
    }


    private Course findCourseById(Integer id) throws UPLException {
        return courseRepository.findById(id)
                .orElseThrow(() -> new UPLException("Course not found"));
    }

    private User getLoggedInUser() throws UPLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UPLException("User not found"));
    }

    public User getCurrentUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole().name());
    }

    private boolean isInstructor(User user) {
        return user.getRole() != null && "Instructor".equalsIgnoreCase(user.getRole().name());
    }

    private boolean isInstructorOwner(User user, Course course) {
        return isInstructor(user)
                && user.getStatus() == User.Status.Active
                && course.getInstructor() != null
                && user.getId().equals(course.getInstructor().getId());
    }

    private void logCourseEdit(Course course, User user, String changes) {
        CourseEditHistory history = new CourseEditHistory();
        history.setCourse(course);
        history.setInstructor(user);
        history.setChanges(changes);
        courseEditHistoryRepository.save(history);
    }

	@Override
	//@CachePut(value = "courses", key = "#id")
	public void approveCourse(Integer id) throws UPLException {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new UPLException("Course Not found"));
        
        // Admin Approval logic
        course.setStatus(Status.Active);
        courseRepository.save(course);
	}

	@Override
	public void rejectCourse(Integer id) throws UPLException {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new UPLException("Course Not found"));
        
        // Admin Approval logic
        course.setStatus(Status.Rejected);
        courseRepository.save(course);
	}

	@Override
	public Page<CourseDTO> getPendingCourses(int page, int size) throws UPLException {
		Pageable pageable = PageRequest.of(page, size);

		Page<Course> pendingCoursesObj = courseRepository.findByStatus(Status.Pending, pageable);

		if (pendingCoursesObj.isEmpty()) {
			throw new UPLException("No pending Courses found");
		}

		return pendingCoursesObj.map(mapper::toCourseDto);

	}
}

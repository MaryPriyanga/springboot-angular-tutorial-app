package com.snipe.learning.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_edit_history")
public class CourseEditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String changes;

    @Column(name = "modified_at", updatable = false, nullable = false)
    private Instant modifiedAt;
    
    @PrePersist
    protected void onCreate() {
        this.modifiedAt = Instant.now();
    }
	public Instant getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Instant modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

    
	public CourseEditHistory() {
	}

	public Integer getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}
    
}


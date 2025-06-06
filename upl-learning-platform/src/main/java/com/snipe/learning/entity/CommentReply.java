package com.snipe.learning.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CommentReply {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private Integer commentId;
	    private String reply;
	    private String repliedBy; 
	    private String replierRole; 
	    private LocalDateTime createdAt = LocalDateTime.now();
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "commentId", insertable = false, updatable = false)
	    private CourseRating courseRating;
	   
		public CommentReply() {
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getCommentId() {
			return commentId;
		}
		public void setCommentId(Integer commentId) {
			this.commentId = commentId;
		}
		public String getReply() {
			return reply;
		}
		public void setReply(String reply) {
			this.reply = reply;
		}
		public String getRepliedBy() {
			return repliedBy;
		}
		public void setRepliedBy(String repliedBy) {
			this.repliedBy = repliedBy;
		}
		public String getReplierRole() {
			return replierRole;
		}
		public void setReplierRole(String replierRole) {
			this.replierRole = replierRole;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    

}

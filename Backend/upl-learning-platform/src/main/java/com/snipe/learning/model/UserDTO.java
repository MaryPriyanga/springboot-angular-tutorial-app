package com.snipe.learning.model;

import java.time.Instant;

public class UserDTO {

	private Integer Id;
	private String name;
	private String email;
	private String password;
	private String role;
	private String status;
	private Instant createdAt;
	
	
	
	public UserDTO() {
	}
	public UserDTO(String name, String email, String password, String role, String status) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
	}
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant instant) {
		this.createdAt = instant;
	}
	
	
}

package com.myapp.models;

public class User {

	String user_id;
	String role;
	String email;
	String password;

	public User(String user_id, String role) {
		super();
		this.user_id = user_id;
		this.role = role;
	}

	public User(String user_id, String role, String password) {
		super();
		this.user_id = user_id;
		this.role = role;
		this.password = password;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
}

package com.myapp.models;

public class User {

	String user_id;
	String role;

	public User(String user_id, String role) {
		super();
		this.user_id = user_id;
		this.role = role;
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
}

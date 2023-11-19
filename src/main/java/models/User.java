package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {

	int user_id;
	String role;
	String email;
	String password;
	String[] permissions;
	private Map<String, Set<String>> permissionMap = new HashMap<>();

	public User(int user_id, String role) {
		super();
		this.user_id = user_id;
		this.role = role;
	}

	public User(int user_id, String role, String password) {
		super();
		this.user_id = user_id;
		this.role = role;
		this.password = password;
	}

	public User(String[] permissions) {
		super();
		this.permissions = permissions;

		for (Permissions permission : Permissions.values()) {
			String key = permission.getMethod() + permission.getEndpoint();
			permissionMap.computeIfAbsent(key, k -> new HashSet<>()).add(permission.name());
		}
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
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

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	public boolean can(String endpoint, String method) {
		String key = method + endpoint;

		Set<String> allowedPermissions = permissionMap.getOrDefault(key, new HashSet<>());

		for (String userPermission : permissions) {
			if (allowedPermissions.contains(userPermission)) {
				return true;
			}
		}

		return false;
	}
}

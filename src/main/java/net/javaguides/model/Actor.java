package net.javaguides.model;

public abstract class Actor {
	String[] permissions = { "CAN_VIEW_PRODUCTS", "CAN_VIEW_PRODUCT DETAILS" };

	public Actor(String[] permissions) {
		int length1 = permissions.length;
		int length2 = this.permissions.length;
		String[] mergedPermissions = new String[length1 + length2];

		System.arraycopy(permissions, 0, mergedPermissions, 0, length1);
		System.arraycopy(this.permissions, 0, mergedPermissions, length1, length2);

		this.permissions = mergedPermissions;
	}

	public String[] getPermissions() {
		return permissions;
	}
}

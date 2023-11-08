package com.myapp.models;

public class Actor {
	String[] permissions = { "CAN_VIEW_PRODUCTS", "CAN_VIEW_PRODUCT_DETAILS", "CAN_SEE_ORDER_BY_ID",
			"CAN_SEE_USER_ORDERS", "CAN_AUTHENTICATE", "CAN_SIGN_UP", "CAN_INITIALIZE_DATABASE" };

	public Actor(String[] permissions) {

		int length1 = permissions.length;
		int length2 = this.permissions.length;
		String[] mergedPermissions = new String[length1 + length2];

		System.arraycopy(permissions, 0, mergedPermissions, 0, length1);
		System.arraycopy(this.permissions, 0, mergedPermissions, length1, length2);

		this.permissions = mergedPermissions;
	}

	public Actor() {

	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}
}

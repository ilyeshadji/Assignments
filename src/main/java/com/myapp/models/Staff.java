package com.myapp.models;

public class Staff extends Actor {

	public static String[] permissions = { "CAN_MAKE_CHANGES_TO_PRODUCT", "CAN_DOWNLOAD_PRODUCT_LIST" };

	public Staff(String[] permissions) {
		super(permissions);

	}

}

package models;

public class Staff extends Actor {
	public static String[] permissions = { "CAN_UPDATE_PRODUCT", "CAN_DOWNLOAD_PRODUCT_LIST", "CAN_SHIP_ORDER",
			"CAN_SEE_ORDER_LIST", "CAN_CREATE_PRODUCT", "CAN_UPDATE_PASSWORD", "CAN_SEE_ALL_USERS",
			"CAN_CHANGE_USER_ROLE" };

	public Staff() {
		super(permissions);
	}
}

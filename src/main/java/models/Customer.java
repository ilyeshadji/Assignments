package models;

public class Customer extends Actor {
	public static String[] permissions = { "CAN_VIEW_CART", "CAN_ADD_TO_CART", "CAN_REMOVE_FROM_CART",
			"CAN_CHANGE_ITEM_QUANTITY_IN_CART", "CAN_PLACE_ORDER" };

	public Customer() {
		super(permissions);
	}
}

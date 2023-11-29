package models;

public enum Permissions {
	// Product permissions
	CAN_VIEW_PRODUCTS("get", "/product-list"), CAN_VIEW_PRODUCT_DETAILS("get", "/product"),
	CAN_UPDATE_PRODUCT("put", "/product"), CAN_CREATE_PRODUCT("post", "/product/create"),

	// Order permissions
	CAN_SEE_ORDER_BY_ID("get", "/orders/order"), CAN_SEE_USER_ORDERS("get", "/orders/customer"),
	CAN_PLACE_ORDER("post", "/orders/customer"), CAN_SHIP_ORDER("post", "/orders/ship"),
	CAN_SEE_ORDER_LIST("get", "/orders"), CAN_CLAIM_ORDER("put", "/orders/claim"),

	// Cart permissions
	CAN_VIEW_CART("get", "/cart"), CAN_ADD_TO_CART("post", "/cart"), CAN_REMOVE_FROM_CART("delete", "/cart"),
	CAN_CHANGE_ITEM_QUANTITY_IN_CART("put", "/cart"),

	// Authentication permissions
	CAN_UPDATE_PASSWORD("put", "/authentication/update-password"),

	// No auth permissions
	CAN_AUTHENTICATE("post", "/authentication/login"), CAN_SIGN_UP("post", "/authentication/signup"),
	CAN_INITIALIZE_DATABASE("post", "/database-initialization"), CAN_CREATE_ORDER("post", "/orders/no-customer"),

	// Download permissions
	CAN_DOWNLOAD_PRODUCT_LIST("get", "/download"),;

	public String method;
	public String endpoint;

	Permissions(String method, String endpoint) {
		this.method = method;
		this.endpoint = endpoint;
	}

	public String getMethod() {
		return method;
	}

	public String getEndpoint() {
		return endpoint;
	}
}

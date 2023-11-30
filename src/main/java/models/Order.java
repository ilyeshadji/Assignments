package models;

import java.util.ArrayList;

public class Order {
	private int user_id;
	private int order_id;
	private ArrayList<Product> products = new ArrayList<>();
	private Double totalPrice;
	private String shipping_address;
	private int tracking_number;

	public Order() {

	}

	public Order(int user_id, int order_id, ArrayList<Product> products, Double totalPrice, String shipping_address,
			int tracking_number) {
		super();
		this.user_id = user_id;
		this.order_id = order_id;
		this.totalPrice = totalPrice;
		this.shipping_address = shipping_address;
		this.tracking_number = tracking_number;
		this.products = products;
	}

	public Order(int user_id, int order_id, Double totalPrice, String shipping_address,
			int tracking_number) {
		super();
		this.user_id = user_id;
		this.order_id = order_id;
		this.totalPrice = totalPrice;
		this.shipping_address = shipping_address;
		this.tracking_number = tracking_number;
	}

	public Order(int order_id, ArrayList<Product> products, Double totalPrice, String shipping_address,
			int tracking_number) {
		super();
		this.order_id = order_id;
		this.totalPrice = totalPrice;
		this.shipping_address = shipping_address;
		this.tracking_number = tracking_number;
		this.products = products;
	}

	public Order(int orderId, String shippingAddress, int trackingNumber, double totalPrice) {
		this.order_id = orderId;
		this.shipping_address = shippingAddress;
		this.tracking_number = trackingNumber;
		this.totalPrice = totalPrice;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getShipping_address() {
		return shipping_address;
	}

	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}

	public int getTracking_number() {
		return tracking_number;
	}

	public void setTracking_number(int tracking_number) {
		this.tracking_number = tracking_number;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public void addProduct(Product product) {
		this.products.add(product);
	}
}

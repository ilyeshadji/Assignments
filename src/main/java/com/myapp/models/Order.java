package com.myapp.models;

public class Order {
	private int user_id;
	private String user_email;
	private int order_id;
	private String[] product_skus;
	private String[] product_urls;
	private Double totalPrice;
	private String shipping_address;
	private int tracking_number;

	public Order(int user_id, String user_email, int order_id, String[] product_skus, String[] product_urls,
			Double totalPrice, String shipping_address, int tracking_number) {
		super();
		this.user_id = user_id;
		this.user_email = user_email;
		this.order_id = order_id;
		this.product_skus = product_skus;
		this.product_urls = product_urls;
		this.totalPrice = totalPrice;
		this.shipping_address = shipping_address;
		this.tracking_number = tracking_number;
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

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String[] getProduct_skus() {
		return product_skus;
	}

	public void setProduct_skus(String[] product_skus) {
		this.product_skus = product_skus;
	}

	public String[] getProduct_urls() {
		return product_urls;
	}

	public void setProduct_urls(String[] product_urls) {
		this.product_urls = product_urls;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}

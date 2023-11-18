package models;

import java.util.ArrayList;

public class Cart {

	int user_id;
	ArrayList<Product> products;
	int numberOfProducts;

	public Cart(int user_id, ArrayList<Product> products) {
		super();
		this.user_id = user_id;
		this.products = products;
		this.numberOfProducts = products.size();
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public int getNumberOfProducts() {
		return numberOfProducts;
	}

	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}
}

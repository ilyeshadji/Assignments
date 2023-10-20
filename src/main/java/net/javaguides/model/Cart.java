package net.javaguides.model;

public class Cart {

	int user_id;
	Product[] products;

	public Cart(int user_id, Product[] products) {
		super();
		this.user_id = user_id;
		this.products = products;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Product[] getProducts() {
		return products;
	}

	public void setProducts(Product[] products) {
		this.products = products;
	}
}

package models;

public class Product {
	private String name;
	private String description;
	private String vendor;
	private String url;
	private String sku;
	private double price;
	private int quantity;

	public Product() {

	}

	public Product(String sku, String name, String description, String vendor, String url, double price) {
		this.name = name;
		this.description = description;
		this.vendor = vendor;
		this.url = url;
		this.sku = sku;
		this.price = price;
	}

	public Product(String sku, String name, String description, String vendor, String url, double price, int quantity) {
		this.name = name;
		this.description = description;
		this.vendor = vendor;
		this.url = url;
		this.sku = sku;
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}

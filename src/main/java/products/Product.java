package products;

public class Product {
	private String name;
	private double price;
	private String link;

	public Product(String name, double price, String link) {
		this.name = name;
		this.price = price;
		this.link = link;
	}

	@Override
	public String toString() {
		return "{Product: Name: " + name + ", Price: " + price + "vnd, Link: " + link + " }";
	}

	public double getPrice() {
		return price;
	}
}

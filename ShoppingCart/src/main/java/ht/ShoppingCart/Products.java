package ht.ShoppingCart;

public class Products {
	String name;
	double price;
	public Products(String name, double price) {
		this.name = name;
		this.price = price;
	}
	public Products(String name) {
		if (name.equals("Dove")) {
			this.name = "Dove";
			this.price = 39.99;
		}
		if (name.equals("Axe Deo")) {
			this.name = "Axe Deo";
			//this.price = 99.99;
			this.price = 89.99;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}

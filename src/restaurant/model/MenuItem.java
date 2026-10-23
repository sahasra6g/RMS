package restaurant.model;
public class MenuItem {
	private final int id;
	private final String name;
	private double price;
	private boolean available;
	
	public MenuItem(int id, String name, double price) {
		this.id=id;
		this.name=name;
		updatePrice(price);
		this.available=true;
	}
	//when u wanna access these private fields in other classes use getter methods
	public int getId() {
		return id;
	}
	public String getName(){
		return name;
	}
	public double getPrice() {
		return price;
	}
	public boolean isAvailable() {
		return available;
	}
	public void updatePrice(double newPrice) {
		if(newPrice<=0) {
			throw new IllegalArgumentException(
				"Price must be greater than zero"
			);
				
		}
		this.price=newPrice;
	}
	public void markUnavailable() {
		available=false;
	}
	public void markAvailable() {
        available = true;
    }
	public void displayDetails() {
		System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: Rs." + price);
        System.out.println("Available: " + available);
	}
}

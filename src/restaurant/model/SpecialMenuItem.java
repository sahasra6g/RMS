package restaurant.model;

//inheritance
public class SpecialMenuItem extends MenuItem{
	private double discountPercentage;
	public SpecialMenuItem(int id, String name, double price, double discountPercentage) {
		super(id, name, price);
		if(discountPercentage<0||discountPercentage>100) {
			throw new IllegalArgumentException("Discount must be between 0 and 100");
		}
		this.discountPercentage = discountPercentage;
	}
		public double getDiscountPercentage() {
	        return discountPercentage;
	    }

	    public double getDiscountedPrice() {
	        return getPrice()- (getPrice() * discountPercentage / 100);
	    }
}


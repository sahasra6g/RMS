package restaurant.model;

public abstract class Payment {
	private double amount;
    public Payment(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be positive"
            );
        }
        this.amount = amount;
    }
    public double getAmount() {
        return amount;
    }
    public abstract void processPayment();
}

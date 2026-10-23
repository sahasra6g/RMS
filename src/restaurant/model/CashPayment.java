

package restaurant.model;

public class CashPayment extends Payment {
    public CashPayment(double amount) {
        super(amount);
    }
    @Override
    public void processPayment() {
        System.out.println(
            "Cash payment received: Rs." + getAmount()
        );
    }
}



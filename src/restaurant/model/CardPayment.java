
package restaurant.model;

public class CardPayment extends Payment {
    public CardPayment(double amount) {
        super(amount);
    }
    @Override
    public void processPayment() {
        System.out.println(
            "Card payment received: Rs." + getAmount()
        );
    }
}

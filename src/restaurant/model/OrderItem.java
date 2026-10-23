
package restaurant.model;

public class OrderItem {

    private final int itemId;
    private final String itemName;
    private final double price;
    private final int quantity;

    public OrderItem(int itemId, String itemName,
                     double price, int quantity) {

        if (itemId <= 0 || itemName == null
                || itemName.trim().isEmpty()
                || price < 0 || !Double.isFinite(price)
                || quantity <= 0) {
            throw new IllegalArgumentException(
                "Invalid order item details."
            );
        }

        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return price * quantity;
    }
}

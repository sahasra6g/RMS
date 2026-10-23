
package restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final int orderId;
    private final int customerId;
    private final String customerName;
    private final List<OrderItem> items;

    public Order(int orderId, int customerId,
                 String customerName, List<OrderItem> items) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = new ArrayList<>(items);
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalAmount() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    public void displayOrder() {
        System.out.println("\n===== ORDER DETAILS =====");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + customerName);

        for (OrderItem item : items) {
            System.out.printf(
                "%s x %d = Rs. %.2f%n",
                item.getItemName(),
                item.getQuantity(),
                item.getSubtotal()
            );
        }

        System.out.printf("Total Amount: Rs. %.2f%n",
                          getTotalAmount());
    }
}

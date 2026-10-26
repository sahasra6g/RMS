
package restaurant.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import restaurant.model.CardPayment;
import restaurant.model.CashPayment;
import restaurant.model.MenuItem;
import restaurant.model.Order;
import restaurant.model.OrderItem;
import restaurant.model.Payment;
import restaurant.model.SpecialMenuItem;
import restaurant.service.Menu;
import restaurant.storage.OrderStorage;
import restaurant.view.CustomerView;

public class CustomerController {

    private final Menu menu;
    private final OrderStorage orderStorage;
    private final CustomerView customerView;
    private final int customerId;
    private final String customerName;

    private final List<MenuItem> orderedItems = new ArrayList<>();
    private final List<Integer> quantities = new ArrayList<>();

    private double totalBill = 0;
    private boolean paymentCompleted = false;

    public CustomerController(
            Menu menu,
            OrderStorage orderStorage,
            Scanner sc,
            int customerId,
            String customerName) {

        this.menu = menu;
        this.orderStorage = orderStorage;
        this.customerView = new CustomerView(sc);
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public void start() {
        int choice;

        do {
            choice = customerView.showCustomerMenu(
                customerName, customerId
            );

            switch (choice) {
                case 1:
                    menu.displayMenu();
                    break;

                case 2:
                    placeOrder();
                    break;

                case 3:
                    displayOrderSummary();
                    break;

                case 4:
                    makePayment();
                    break;
                case 5:
                    viewPreviousOrders();
                    break;

                case 0:
                    customerView.showMessage("Customer logged out.");
                    break;

                default:
                    customerView.showMessage(
                        "Invalid choice. Please try again."
                    );
            }

        } while (choice != 0);
    }

    private void placeOrder() {
        if (paymentCompleted) {
            customerView.showMessage(
                "Payment is completed. Log in again to start another order."
            );
            return;
        }

        menu.displayMenu();

        int id = customerView.readInt("Enter food ID: ");
        MenuItem item = menu.findItem(id);

        if (item == null) {
            customerView.showMessage("Food item not found.");
            return;
        }

        if (!item.isAvailable()) {
            customerView.showMessage(
                "Sorry, this food item is unavailable."
            );
            return;
        }

        int quantity = customerView.readInt("Enter quantity: ");

        if (quantity <= 0) {
            customerView.showMessage(
                "Quantity must be greater than zero."
            );
            return;
        }

        orderedItems.add(item);
        quantities.add(quantity);

        double itemTotal = getItemPrice(item) * quantity;
        totalBill += itemTotal;

        customerView.showMessage("Item added to your order!");
        customerView.showMessage(
            String.format("Item total: Rs. %.2f", itemTotal)
        );
    }

    private double getItemPrice(MenuItem item) {
        if (item instanceof SpecialMenuItem) {
            SpecialMenuItem specialItem = (SpecialMenuItem) item;
            return specialItem.getDiscountedPrice();
        }

        return item.getPrice();
    }

    private void displayOrderSummary() {
        customerView.showMessage("\n===== ORDER SUMMARY =====");
        customerView.showMessage("Customer ID: " + customerId);
        customerView.showMessage("Customer Name: " + customerName);

        if (orderedItems.isEmpty()) {
            customerView.showMessage(
                "You have not ordered any items."
            );
            return;
        }

        for (int i = 0; i < orderedItems.size(); i++) {
            MenuItem item = orderedItems.get(i);
            int quantity = quantities.get(i);
            double price = getItemPrice(item);

            customerView.showMessage(
                String.format("%s x %d = Rs. %.2f",
                    item.getName(), quantity, price * quantity)
            );
        }

        customerView.showMessage(
            String.format("Total Bill: Rs. %.2f", totalBill)
        );

        customerView.showMessage(
            "Payment Status: " +
            (paymentCompleted ? "Paid" : "Pending")
        );
    }

private void viewPreviousOrders() {
    customerView.showMessage("\n===== PREVIOUS ORDERS =====");

    boolean found = false;

    for (Order order : orderStorage.getAllOrders()) {
        if (order.getCustomerId() == customerId) {
            order.displayOrder();
            found = true;
        }
    }

    if (!found) {
        customerView.showMessage("No previous orders found.");
    }
}

    private void makePayment() {
        if (orderedItems.isEmpty()) {
            customerView.showMessage(
                "Please place an order first."
            );
            return;
        }

        if (paymentCompleted) {
            customerView.showMessage(
                "Payment has already been completed."
            );
            return;
        }

        displayOrderSummary();

        customerView.showMessage("\nSelect Payment Method:");
        customerView.showMessage("1. Cash");
        customerView.showMessage("2. Card");

        int paymentChoice =
            customerView.readInt("Enter your choice: ");

        Payment payment;

        switch (paymentChoice) {
            case 1:
                payment = new CashPayment(totalBill);
                break;

            case 2:
                payment = new CardPayment(totalBill);
                break;

            default:
                customerView.showMessage(
                    "Invalid payment method."
                );
                return;
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < orderedItems.size(); i++) {
            MenuItem item = orderedItems.get(i);
            int quantity = quantities.get(i);

            orderItems.add(new OrderItem(
                item.getId(),
                item.getName(),
                getItemPrice(item),
                quantity
            ));
        }

        int nextOrderId = 1001;

        for (Order existingOrder : orderStorage.getAllOrders()) {
            if (existingOrder.getOrderId() >= nextOrderId) {
                nextOrderId = existingOrder.getOrderId() + 1;
            }
        }

        Order order = new Order(
            nextOrderId,
            customerId,
            customerName,
            orderItems
        );

        if (!orderStorage.addOrder(order)) {
            customerView.showMessage(
                "Could not save the order."
            );
            return;
        }

        payment.processPayment();
        paymentCompleted = true;

        customerView.showMessage("Payment successful!");
        customerView.showMessage("Order saved successfully!");
        customerView.showMessage("Your Order ID: " + nextOrderId);
    }
}

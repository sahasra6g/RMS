
package restaurant;

import java.util.ArrayList;
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

public class Customer {

    private final Menu menu;
    private final OrderStorage orderStorage;
    private final int customerId;
    private final String customerName;

    private final ArrayList<MenuItem> orderedItems = new ArrayList<>();
    private final ArrayList<Integer> quantities = new ArrayList<>();

    private double totalBill = 0;
    private boolean paymentCompleted = false;

    // Constructor
    public Customer(Menu menu, OrderStorage orderStorage,
                    int customerId, String customerName) {

        if (menu == null || orderStorage == null) {
            throw new IllegalArgumentException(
                "Menu and order storage cannot be null."
            );
        }

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                "Customer ID must be positive."
            );
        }

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Customer name cannot be empty."
            );
        }

        this.menu = menu;
        this.orderStorage = orderStorage;
        this.customerId = customerId;
        this.customerName = customerName.trim();
    }

    // Display customer options
    public void displayOptions() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("Welcome, " + customerName
                    + " (ID: " + customerId + ")");
            System.out.println("1. View Food Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Order Summary");
            System.out.println("4. Make Payment");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            choice = readInt(sc);

            switch (choice) {
                case 1:
                    menu.displayMenu();
                    break;

                case 2:
                    placeOrder(sc);
                    break;

                case 3:
                    displayOrderSummary();
                    break;

                case 4:
                    makePayment(sc);
                    break;

                case 0:
                    System.out.println("Customer logged out.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }

    // Place an order using food ID and quantity
    private void placeOrder(Scanner sc) {
        if (paymentCompleted) {
            System.out.println(
                "Your previous order is paid. Start a new session to order again."
            );
            return;
        }

        menu.displayMenu();

        System.out.print("Enter food ID: ");
        int id = readInt(sc);

        MenuItem item = menu.findItem(id);

        if (item == null) {
            System.out.println("Food item not found.");
            return;
        }

        if (!item.isAvailable()) {
            System.out.println("Sorry, this food item is unavailable.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = readInt(sc);

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        orderedItems.add(item);
        quantities.add(quantity);

        double itemTotal = getItemPrice(item) * quantity;
        totalBill += itemTotal;

        System.out.println("Item added to your order!");
        System.out.printf("Item total: Rs. %.2f%n", itemTotal);
    }

    // Calculate price, applying a special-item discount when applicable
    private double getItemPrice(MenuItem item) {
        if (item instanceof SpecialMenuItem) {
            SpecialMenuItem specialItem = (SpecialMenuItem) item;
            return specialItem.getDiscountedPrice();
        }

        return item.getPrice();
    }

    // Display order summary
    private void displayOrderSummary() {
        System.out.println("\n===== ORDER SUMMARY =====");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + customerName);

        if (orderedItems.isEmpty()) {
            System.out.println("You have not ordered any items.");
            return;
        }

        for (int i = 0; i < orderedItems.size(); i++) {
            MenuItem item = orderedItems.get(i);
            int quantity = quantities.get(i);
            double price = getItemPrice(item);

            System.out.printf(
                "%s x %d = Rs. %.2f%n",
                item.getName(),
                quantity,
                price * quantity
            );
        }

        System.out.printf("Total Bill: Rs. %.2f%n", totalBill);

        if (paymentCompleted) {
            System.out.println("Payment Status: Paid");
        } else {
            System.out.println("Payment Status: Pending");
        }
    }

    // Make payment and save the order
    private void makePayment(Scanner sc) {
        if (orderedItems.isEmpty()) {
            System.out.println("Please place an order first.");
            return;
        }

        if (paymentCompleted) {
            System.out.println("Payment has already been completed.");
            return;
        }

        displayOrderSummary();

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Cash");
        System.out.println("2. Card");
        System.out.print("Enter your choice: ");

        int paymentChoice = readInt(sc);

        Payment payment;

        switch (paymentChoice) {
            case 1:
                payment = new CashPayment(totalBill);
                break;

            case 2:
                payment = new CardPayment(totalBill);
                break;

            default:
                System.out.println("Invalid payment method.");
                return;
        }

        // Create a separate list of order items
        ArrayList<OrderItem> orderItems = new ArrayList<>();

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

        // Generate the next order ID
        int nextOrderId = 1001;

        for (Order existingOrder : orderStorage.getAllOrders()) {
            if (existingOrder.getOrderId() >= nextOrderId) {
                nextOrderId = existingOrder.getOrderId() + 1;
            }
        }

        // Create the order with customer details
        Order order = new Order(
            nextOrderId,
            customerId,
            customerName,
            orderItems
        );

        // Save the order before processing payment
        if (!orderStorage.addOrder(order)) {
            System.out.println("Could not save the order.");
            return;
        }

        // Process payment after the order is saved
        payment.processPayment();
        paymentCompleted = true;

        System.out.println("Payment successful!");
        System.out.println("Order saved successfully!");
        System.out.println("Your Order ID: " + nextOrderId);
    }

    // Read integer input safely
    private int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid whole number.");
            sc.next();
            System.out.print("Enter your choice: ");
        }

        return sc.nextInt();
    }
}

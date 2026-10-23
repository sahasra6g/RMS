
package restaurant.view;

import java.util.Scanner;

public class CustomerView {

    private final Scanner sc;

    public CustomerView(Scanner sc) {
        this.sc = sc;
    }

    public int showCustomerMenu(String customerName, int customerId) {
        System.out.println("\n===== CUSTOMER MENU =====");
        System.out.println("Welcome, " + customerName
                + " (ID: " + customerId + ")");
        System.out.println("1. View Food Menu");
        System.out.println("2. Place Order");
        System.out.println("3. View Order Summary");
        System.out.println("4. Make Payment");
        System.out.println("0. Logout");
        return readInt("Enter your choice: ");
    }

    public int readInt(String prompt) {
        System.out.print(prompt);

        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid whole number.");
            sc.next();
            System.out.print(prompt);
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public double readDouble(String prompt) {
        System.out.print(prompt);

        while (!sc.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            sc.next();
            System.out.print(prompt);
        }

        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

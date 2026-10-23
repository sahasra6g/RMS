package restaurant;

import java.util.Scanner;

import restaurant.model.MenuItem;
import restaurant.service.Menu;

public class Admin {

    private Menu menu;

    // Constructor
    public Admin(Menu menu) {
        this.menu = menu;
    }

    // Display admin options
    public void displayOptions() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View Food Menu");
            System.out.println("2. Add Food Item");
            System.out.println("3. Delete Food Item");
            System.out.println("4. Update Food Price");
            System.out.println("5. Update Food Availability");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.next();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    menu.displayMenu();
                    break;

                case 2:
                    addFoodItem(sc);
                    break;

                case 3:
                    System.out.print("Enter food ID to delete: ");
                    int deleteId = readInt(sc);
                    menu.deleteItem(deleteId);
                    break;

                case 4:
                    System.out.print("Enter food ID: ");
                    int updateId = readInt(sc);

                    System.out.print("Enter new price: ");
                    double newPrice = readDouble(sc);

                    if (newPrice <= 0 || !Double.isFinite(newPrice)) {
                        System.out.println("Price must be a valid positive number.");
                    } else {
                        menu.updatePrice(updateId, newPrice);
                    }
                    break;

                case 5:
                    System.out.print("Enter food ID: ");
                    int foodId = readInt(sc);

                    System.out.print("Is the item available? (true/false): ");
                    while (!sc.hasNextBoolean()) {
                        System.out.println("Please enter true or false.");
                        sc.next();
                        System.out.print("Is the item available? (true/false): ");
                    }

                    boolean available = sc.nextBoolean();
                    menu.updateAvailability(foodId, available);
                    break;

                case 0:
                    System.out.println("Admin logged out.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    // Add a new food item
    private void addFoodItem(Scanner sc) {
        System.out.print("Enter food ID: ");
        int id = readInt(sc);

        sc.nextLine(); // Clear the input buffer

        System.out.print("Enter food name: ");
        String name = sc.nextLine().trim();

        if (id <= 0 || name.isEmpty()) {
            System.out.println("Invalid ID or food name.");
            return;
        }

        System.out.print("Enter food price: ");
        double price = readDouble(sc);

        if (price <= 0 || !Double.isFinite(price)) {
            System.out.println("Price must be a valid positive number.");
            return;
        }

        MenuItem item = new MenuItem(id, name, price);
        menu.addItem(item);
    }

    // Read an integer safely
    private int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid whole number.");
            sc.next();
        }
        return sc.nextInt();
    }

    // Read a decimal number safely
    private double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            sc.next();
        }
        return sc.nextDouble();
    }
}


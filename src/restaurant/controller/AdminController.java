
package restaurant.controller;

import java.util.Scanner;

import restaurant.model.MenuItem;
import restaurant.service.Menu;
import restaurant.view.AdminView;

public class AdminController {

    private final Menu menu;
    private final AdminView adminView;

    public AdminController(Menu menu, Scanner sc) {
        this.menu = menu;
        this.adminView = new AdminView(sc);
    }

    public void start() {
        int choice;

        do {
            choice = adminView.showAdminMenu();

            switch (choice) {
                case 1:
                    menu.displayMenu();
                    break;

                case 2:
                    addFoodItem();
                    break;

                case 3:
                    deleteFoodItem();
                    break;

                case 4:
                    updateFoodPrice();
                    break;

                case 5:
                    updateFoodAvailability();
                    break;

                case 0:
                    adminView.showMessage("Admin logged out.");
                    break;

                default:
                    adminView.showMessage(
                        "Invalid choice. Please try again."
                    );
            }

        } while (choice != 0);
    }

    private void addFoodItem() {
        int id = adminView.readInt("Enter food ID: ");

        if (id <= 0 || menu.findItem(id) != null) {
            adminView.showMessage(
                "Invalid ID or food ID already exists."
            );
            return;
        }

        String name = adminView.readText("Enter food name: ");

        if (name.isEmpty()) {
            adminView.showMessage("Food name cannot be empty.");
            return;
        }

        double price = adminView.readDouble("Enter food price: ");

        if (!Double.isFinite(price) || price < 0) {
            adminView.showMessage("Invalid food price.");
            return;
        }

        boolean added = menu.addItem(new MenuItem(id, name, price));

        if (added) {
            adminView.showMessage("Food item added successfully!");
        } else {
            adminView.showMessage("Could not add food item.");
        }
    }

    private void deleteFoodItem() {
        int id = adminView.readInt("Enter food ID to delete: ");

        if (menu.deleteItem(id)) {
            adminView.showMessage("Food item deleted successfully!");
        } else {
            adminView.showMessage("Food item not found.");
        }
    }

    private void updateFoodPrice() {
        int id = adminView.readInt("Enter food ID: ");

        double price = adminView.readDouble("Enter new price: ");

        if (!Double.isFinite(price) || price < 0) {
            adminView.showMessage("Invalid food price.");
            return;
        }

        if (menu.updatePrice(id, price)) {
            adminView.showMessage("Food price updated successfully!");
        } else {
            adminView.showMessage("Food item not found.");
        }
    }

    private void updateFoodAvailability() {
        int id = adminView.readInt("Enter food ID: ");

        if (menu.findItem(id) == null) {
            adminView.showMessage("Food item not found.");
            return;
        }

        int status = adminView.readInt(
            "Enter 1 for Available, 0 for Unavailable: "
        );

        if (status != 0 && status != 1) {
            adminView.showMessage("Invalid availability choice.");
            return;
        }

        boolean available = status == 1;

        if (menu.updateAvailability(id, available)) {
            adminView.showMessage(
                "Food availability updated successfully!"
            );
        } else {
            adminView.showMessage("Could not update availability.");
        }
    }
}

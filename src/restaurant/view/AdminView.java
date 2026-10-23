
package restaurant.view;

import java.util.Scanner;

public class AdminView {

    private final Scanner sc;

    public AdminView(Scanner sc) {
        this.sc = sc;
    }

    public int showAdminMenu() {
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

        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
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

    public String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

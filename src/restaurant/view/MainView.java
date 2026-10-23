
package restaurant.view;

import java.util.Scanner;

public class MainView {

    private final Scanner sc;

    public MainView(Scanner sc) {
        this.sc = sc;
    }

    public int showRoleMenu() {
        System.out.println("\n===== RESTAURANT MANAGEMENT SYSTEM =====");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("0. Exit");
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

    public void showMessage(String message) {
        System.out.println(message);
    }

	public int showStorageMenu() {
	    System.out.println("\n===== CHOOSE STORAGE TYPE =====");
	    System.out.println("1. Collections (temporary)");
	    System.out.println("2. JSON Files (persistent)");
	    System.out.println("3. Database (persistent)");
	    System.out.println("0. Exit");
	
	    return readInt("Choose storage type: ");
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
}


package restaurant;

import java.util.Scanner;

import restaurant.controller.MainController;
import restaurant.service.Menu;
import restaurant.storage.MenuStorage;
import restaurant.storage.OrderStorage;
import restaurant.storage.CollectionMenuStorage;
import restaurant.storage.CollectionOrderStorage;
import restaurant.view.MainView;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MainView view = new MainView(sc);

        int choice = view.showStorageMenu();

        if (choice == 0) {
            System.out.println("Goodbye!");
            sc.close();
            return;
        }

        MenuStorage menuStorage;
        OrderStorage orderStorage;

        switch (choice) {
            case 1:
                menuStorage = new CollectionMenuStorage();
                orderStorage = new CollectionOrderStorage();
                break;

            case 2:
                System.out.println(
                    "JSON storage is not implemented yet."
                );
                sc.close();
                return;

            case 3:
                System.out.println(
                    "Database storage is not implemented yet."
                );
                sc.close();
                return;

            default:
                System.out.println("Invalid storage choice.");
                sc.close();
                return;
        }

        Menu menu = new Menu(menuStorage);

        MainController controller = new MainController(
            sc,
            menu,
            orderStorage
        );

        controller.start();
        sc.close();
    }
}

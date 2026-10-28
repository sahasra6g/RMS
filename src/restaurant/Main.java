package restaurant;

import java.util.Scanner;

import restaurant.controller.MainController;
import restaurant.service.Menu;
import restaurant.storage.MenuStorage;
import restaurant.storage.OrderStorage;
import restaurant.storage.CollectionMenuStorage;
import restaurant.storage.CollectionOrderStorage;
import restaurant.storage.JsonMenuStorage;
import restaurant.storage.JsonOrderStorage;
import restaurant.storage.DBMenuStorage;
import restaurant.storage.DBOrderStorage;

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
                menuStorage=new JsonMenuStorage();
                orderStorage=new JsonOrderStorage();
                break;
            case 3:
                menuStorage = new DBMenuStorage();
                orderStorage = new DBOrderStorage();
                break;

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
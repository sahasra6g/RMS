
package restaurant.controller;

import java.util.Scanner;
import restaurant.service.Menu;
import restaurant.storage.CollectionMenuStorage;
import restaurant.storage.CollectionOrderStorage;
import restaurant.storage.MenuStorage;
import restaurant.storage.OrderStorage;
import restaurant.view.MainView;

public class MainController {

    private final Scanner sc;
    private final MainView mainView;

    private final Menu menu;
    private final OrderStorage orderStorage;


	public MainController(
	        Scanner sc,
	        Menu menu,
	        OrderStorage orderStorage) {
	
	    this.sc = sc;
	    this.mainView = new MainView(sc);
	    this.menu = menu;
	    this.orderStorage = orderStorage;
	}


    public void start() {
        int choice;

        do {
            choice = mainView.showRoleMenu();

            switch (choice) {
                case 1:
	                	AdminController adminController =
	                    new AdminController(menu, sc);
	                adminController.start();
	                break;


				case 2:
				    int customerId =
				        mainView.readInt("Enter Customer ID: ");
				
				    String customerName =
				        mainView.readText("Enter Customer Name: ");
				
				    if (customerId <= 0 || customerName.isEmpty()) {
				        mainView.showMessage(
				            "Invalid customer ID or name."
				        );
				        break;
				    }
				
				    CustomerController customerController =
				        new CustomerController(
				            menu,
				            orderStorage,
				            sc,
				            customerId,
				            customerName
				        );
				
				    customerController.start();
				    break;

                case 0:
                    mainView.showMessage(
                        "Exiting Restaurant Management System. Goodbye!"
                    );
                    break;

                default:
                    mainView.showMessage(
                        "Invalid choice. Please try again."
                    );
            }

        } while (choice != 0);
    }
}


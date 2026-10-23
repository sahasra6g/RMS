
package restaurant.service;

import restaurant.model.MenuItem;
import restaurant.storage.CollectionMenuStorage;
import restaurant.storage.MenuStorage;

//import java.util.ArrayList;

public class Menu {

    //private ArrayList<MenuItem> items = new ArrayList<>();
	private final MenuStorage storage;
    public Menu() {
    		this(new CollectionMenuStorage());
        
    }
    public Menu(MenuStorage storage) {
        this.storage = storage;
    }
    // view all food items
    public void displayMenu() {
        System.out.println("\n=== FOOD MENU ===");
        if (storage.getAllItems().isEmpty()) {
            System.out.println("No food items available.");
            return;
        }

        for (MenuItem item : storage.getAllItems()) {
            item.displayDetails();
        }
        
    }

    // Add a food item
    public boolean addItem(MenuItem item) {
    		return storage.addItem(item);	
    }

    // Find a food item using its ID
    public MenuItem findItem(int id) {
    		return storage.findItem(id);
    }

    // Delete a food item
    public boolean deleteItem(int id) {
    		return storage.deleteItem(id);
    }

    // Update price
    public boolean updatePrice(int id, double newPrice) {
    		return storage.updatePrice(id, newPrice);
    }

    // Update availability

public boolean updateAvailability(int id, boolean available) {
    return storage.updateAvailability(id, available);
}

}

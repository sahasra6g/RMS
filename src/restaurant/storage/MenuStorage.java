package restaurant.storage;
import java.util.List;

import restaurant.model.MenuItem;
public interface MenuStorage {
	List<MenuItem> getAllItems();
	MenuItem findItem(int id);
	boolean addItem(MenuItem item);
	boolean deleteItem(int id);
	boolean updatePrice(int id, double newPrice);
	boolean updateAvailability(int id,boolean available);
}

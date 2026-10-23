package restaurant.storage;
import java.util.ArrayList;
import java.util.List;

import restaurant.model.MenuItem;
import restaurant.model.SpecialMenuItem;
public class CollectionMenuStorage implements MenuStorage{
	private final List<MenuItem> items = new ArrayList<>();

    public CollectionMenuStorage() {
        items.add(new MenuItem(1, "Burger", 150));
        items.add(new MenuItem(2, "Pizza", 250));
        items.add(new MenuItem(3, "Biryani", 200));
        items.add(new SpecialMenuItem(4, "Special Pizza", 300, 10));
    }

    @Override
    public List<MenuItem> getAllItems() {
        return new ArrayList<>(items);
    }

    @Override
    public MenuItem findItem(int id) {
        for (MenuItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean addItem(MenuItem item) {
        if (findItem(item.getId()) != null) {
            return false;
        }

        items.add(item);
        return true;
    }

    @Override
    public boolean deleteItem(int id) {
        MenuItem item = findItem(id);

        if (item == null) {
            return false;
        }

        items.remove(item);
        return true;
    }

    @Override
    public boolean updatePrice(int id, double newPrice) {
        MenuItem item = findItem(id);

        if (item == null || newPrice <= 0
                || !Double.isFinite(newPrice)) {
            return false;
        }

        item.updatePrice(newPrice);
        return true;
    }

    @Override
    public boolean updateAvailability(int id, boolean available) {
        MenuItem item = findItem(id);

        if (item == null) {
            return false;
        }

        if (available) {
            item.markAvailable();
        } else {
            item.markUnavailable();
        }

        return true;
    }
}

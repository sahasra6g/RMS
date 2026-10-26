
package restaurant.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import restaurant.model.MenuItem;

public class JsonMenuStorage implements MenuStorage {

    private final Path filePath = Path.of("data", "menu.json");

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private List<MenuItem> items = new ArrayList<>();

    // Used to preserve all MenuItem fields in JSON
    private static class MenuItemData {
        int id;
        String name;
        double price;
        boolean available;

        MenuItemData() {
        }

        MenuItemData(MenuItem item) {
            id = item.getId();
            name = item.getName();
            price = item.getPrice();
            available = item.isAvailable();
        }
    }

    public JsonMenuStorage() {
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            if (Files.exists(filePath)) {
                String json = Files.readString(filePath);

                List<MenuItemData> data = gson.fromJson(
                        json,
                        new TypeToken<List<MenuItemData>>() {}.getType()
                );

                items = new ArrayList<>();

                if (data != null) {
                    for (MenuItemData d : data) {
                        MenuItem item =
                                new MenuItem(d.id, d.name, d.price);

                        if (!d.available) {
                            item.markUnavailable();
                        }

                        items.add(item);
                    }
                }
            } else {
                // First launch: create an initial menu
                items.add(new MenuItem(1, "Burger", 150));
                items.add(new MenuItem(2, "Pizza", 250));
                items.add(new MenuItem(3, "Biryani", 200));

                saveToFile();
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not load menu from JSON file.", e);
        }
    }

    private void saveToFile() {
        try {
            Files.createDirectories(filePath.getParent());

            List<MenuItemData> data = new ArrayList<>();

            for (MenuItem item : items) {
                data.add(new MenuItemData(item));
            }

            Files.writeString(filePath, gson.toJson(data));

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not save menu to JSON file.", e);
        }
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
        saveToFile();
        return true;
    }

    @Override
    public boolean deleteItem(int id) {
        MenuItem item = findItem(id);

        if (item == null) {
            return false;
        }

        items.remove(item);
        saveToFile();
        return true;
    }

    @Override
    public boolean updatePrice(int id, double newPrice) {
        MenuItem item = findItem(id);

        if (item == null) {
            return false;
        }

        item.updatePrice(newPrice);
        saveToFile();
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

        saveToFile();
        return true;
    }
}

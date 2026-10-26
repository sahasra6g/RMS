
package restaurant.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import restaurant.model.Order;
import restaurant.model.OrderItem;

public class JsonOrderStorage implements OrderStorage {

    private final List<Order> orders = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Path filePath = Paths.get("data", "orders.json");

    public JsonOrderStorage() {
        loadOrders();
    }

    private void loadOrders() {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                saveOrders();
                return;
            }

            String json = Files.readString(filePath);

            if (json.trim().isEmpty()) {
                return;
            }

            OrderData[] savedOrders =
                    gson.fromJson(json, OrderData[].class);

            if (savedOrders != null) {
                for (OrderData data : savedOrders) {
                    orders.add(data.toOrder());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not load orders from JSON.", e);
        }
    }

    private void saveOrders() {
        try {
            Files.createDirectories(filePath.getParent());

            List<OrderData> dataList = new ArrayList<>();

            for (Order order : orders) {
                dataList.add(new OrderData(order));
            }

            String json = gson.toJson(dataList);
            Files.writeString(filePath, json);

        } catch (IOException e) {
            throw new RuntimeException("Could not save orders to JSON.", e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public Order findOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    @Override
    public boolean addOrder(Order order) {
        if (order == null || findOrder(order.getOrderId()) != null) {
            return false;
        }

        orders.add(order);
        saveOrders();
        return true;
    }

    // Converts an Order into a JSON-friendly data object.
    private static class OrderData {
        int orderId;
        int customerId;
        String customerName;
        List<OrderItemData> items = new ArrayList<>();

        OrderData() {
        }

        OrderData(Order order) {
            orderId = order.getOrderId();
            customerId = order.getCustomerId();
            customerName = order.getCustomerName();

            for (OrderItem item : order.getItems()) {
                items.add(new OrderItemData(item));
            }
        }

        Order toOrder() {
            List<OrderItem> restoredItems = new ArrayList<>();

            if (items != null) {
                for (OrderItemData item : items) {
                    restoredItems.add(item.toOrderItem());
                }
            }

            return new Order(
                    orderId,
                    customerId,
                    customerName,
                    restoredItems
            );
        }
    }

    // Converts an OrderItem into a JSON-friendly data object.
    private static class OrderItemData {
        int itemId;
        String itemName;
        double price;
        int quantity;

        OrderItemData() {
        }

        OrderItemData(OrderItem item) {
            itemId = item.getItemId();
            itemName = item.getItemName();
            price = item.getPrice();
            quantity = item.getQuantity();
        }

        OrderItem toOrderItem() {
            return new OrderItem(itemId, itemName, price, quantity);
        }
    }
}


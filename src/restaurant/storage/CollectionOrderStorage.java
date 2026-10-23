
package restaurant.storage;

import java.util.ArrayList;
import java.util.List;

import restaurant.model.Order;

public class CollectionOrderStorage implements OrderStorage {

    private final List<Order> orders = new ArrayList<>();

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
        return true;
    }
}

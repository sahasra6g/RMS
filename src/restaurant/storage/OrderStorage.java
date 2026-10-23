package restaurant.storage;
import java.util.List;

import restaurant.model.Order;

public interface OrderStorage {
	List<Order> getAllOrders();

    Order findOrder(int orderId);

    boolean addOrder(Order order);
}

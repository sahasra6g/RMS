
package restaurant.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.model.Order;
import restaurant.model.OrderItem;

public class DBOrderStorage implements OrderStorage {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_id, customer_id, customer_name "
                   + "FROM orders ORDER BY order_id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                List<OrderItem> items = getOrderItems(con, orderId);

                orders.add(new Order(
                        orderId,
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        items
                ));
            }

            return orders;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not load orders from PostgreSQL.", e);
        }
    }

    @Override
    public Order findOrder(int orderId) {
        String sql = "SELECT order_id, customer_id, customer_name "
                   + "FROM orders WHERE order_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        getOrderItems(con, orderId)
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not find order.", e);
        }
    }

    private List<OrderItem> getOrderItems(
            Connection con, int orderId) throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT item_id, item_name, price, quantity "
                   + "FROM order_items WHERE order_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItem(
                            rs.getInt("item_id"),
                            rs.getString("item_name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            }
        }

        return items;
    }

    @Override
    public boolean addOrder(Order order) {
        String orderSql =
                "INSERT INTO orders "
              + "(order_id, customer_id, customer_name) "
              + "VALUES (?, ?, ?)";

        String itemSql =
                "INSERT INTO order_items "
              + "(order_id, item_id, item_name, price, quantity) "
              + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps =
                             con.prepareStatement(orderSql)) {

                    ps.setInt(1, order.getOrderId());
                    ps.setInt(2, order.getCustomerId());
                    ps.setString(3, order.getCustomerName());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(itemSql)) {

                    for (OrderItem item : order.getItems()) {
                        ps.setInt(1, order.getOrderId());
                        ps.setInt(2, item.getItemId());
                        ps.setString(3, item.getItemName());
                        ps.setDouble(4, item.getPrice());
                        ps.setInt(5, item.getQuantity());
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();

                if ("23505".equals(e.getSQLState())) {
                    return false;
                }

                throw e;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not save order to PostgreSQL.", e);
        }
    }
}


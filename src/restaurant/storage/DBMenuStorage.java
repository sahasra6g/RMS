
package restaurant.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.model.MenuItem;

public class DBMenuStorage implements MenuStorage {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public List<MenuItem> getAllItems() {
        List<MenuItem> items = new ArrayList<>();

        String sql = "SELECT id, name, price, available "
                   + "FROM menu_items ORDER BY id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );

                if (!rs.getBoolean("available")) {
                    item.markUnavailable();
                }

                items.add(item);
            }

            return items;

        } catch (SQLException e) {
        	System.out.println(e.getMessage() + " " + e.getErrorCode());
            throw new IllegalStateException(
                    "Could not load menu from PostgreSQL.", e);
        }
    }

    @Override
    public MenuItem findItem(int id) {
        String sql = "SELECT id, name, price, available "
                   + "FROM menu_items WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MenuItem item = new MenuItem(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price")
                    );

                    if (!rs.getBoolean("available")) {
                        item.markUnavailable();
                    }

                    return item;
                }
            }

            return null;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not find menu item.", e);
        }
    }

    @Override
    public boolean addItem(MenuItem item) {
        String sql = "INSERT INTO menu_items "
                   + "(id, name, price, available) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getId());
            ps.setString(2, item.getName());
            ps.setDouble(3, item.getPrice());
            ps.setBoolean(4, item.isAvailable());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return false; // Duplicate ID
            }

            throw new IllegalStateException(
                    "Could not add menu item.", e);
        }
    }

    @Override
    public boolean deleteItem(int id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not delete menu item.", e);
        }
    }

    @Override
    public boolean updatePrice(int id, double newPrice) {
        if (newPrice <= 0 || !Double.isFinite(newPrice)) {
            return false;
        }

        String sql = "UPDATE menu_items SET price = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not update menu price.", e);
        }
    }

    @Override
    public boolean updateAvailability(int id, boolean available) {
        String sql = "UPDATE menu_items SET available = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not update menu availability.", e);
        }
    }
}
